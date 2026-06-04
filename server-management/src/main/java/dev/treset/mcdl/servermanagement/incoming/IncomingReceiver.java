package dev.treset.mcdl.servermanagement.incoming;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import dev.treset.mcdl.servermanagement.ManagementHandler;
import dev.treset.mcdl.servermanagement.data.DataProvider;
import dev.treset.mcdl.servermanagement.data.IdentificationProvider;
import dev.treset.mcdl.servermanagement.data.RpcRequest;
import dev.treset.mcdl.servermanagement.exception.RpcCommunicationException;
import dev.treset.mcdl.servermanagement.notification.RpcNotification;
import dev.treset.mcdl.servermanagement.data.RpcResponse;
import dev.treset.mcdl.servermanagement.serialization.DataSerializer;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class IncomingReceiver<R extends DataProvider & IdentificationProvider<I>, T, I> implements IdentificationProvider<I> {
    static final long DEFAULT_WAIT_TIMEOUT = 30_000;

    final I identification;
    final DataSerializer<T> serializer;
    final Consumer<T> resultConsumer;
    final Consumer<RpcCommunicationException> errorConsumer;
    final boolean unregisterOnResult;

    final Set<IncomingHandler<R,I>> registeredOn = new HashSet<>();

    private final Object lock = new Object(){};
    private final AtomicReference<Boolean> received = new AtomicReference<>(false);
    private final AtomicReference<T> result = new AtomicReference<>();
    private final AtomicReference<RpcCommunicationException> error = new AtomicReference<>();

    public IncomingReceiver(I identification, DataSerializer<T> serializer, Consumer<T> resultConsumer, Consumer<RpcCommunicationException> errorConsumer, boolean unregisterOnResult) {
        this.identification = identification;
        this.serializer = serializer;
        this.resultConsumer = resultConsumer;
        this.errorConsumer = errorConsumer;
        this.unregisterOnResult = unregisterOnResult;
    }

    public void receive(R message) {
        try {
            onResult(serializer.deserialize(message.data()));
        } catch (RpcCommunicationException e) {
            onError(e);
        }
        if(unregisterOnResult) {
            for(IncomingHandler<R,I> handler : registeredOn) {
                unregister(handler);
            }
        }
    }

    /**
     * Waits for the next message to this receiver. <br><br>
     *
     * Example to wait for saving to complete after triggering it, with a maximum wait time of 10 seconds:
     * <pre>
     * {@code
     *  IncomingReceiver.ParameterlessNotification receiver = RpcNotifications.Server.saved();
     *  receiver.register(managementHandler.notificationHandler);
     *  receiver.waitForNext(
     *      () -> { if(!RpcMethods.Server.SAVE.sendBlocking(true, managementHandler)) throw new IOException("Failed to trigger save"); },
     *      10_000
     *  );
     * }
     * </pre>
     *
     * @param triggerAction A function that may be used to execute any events that trigger the message to wait for. It is guaranteed, that any messages received by this receiver during and after the execution of {@code triggerAction} will be returned here.
     * @param timeout The maximum time to wait for a message in milliseconds.
     * @return The message parameter.
     * @throws RpcCommunicationException If there is an error receiving the message.
     */
    public T waitForNext(CheckedAction triggerAction, long timeout) throws RpcCommunicationException {
        synchronized (lock) {
            try {
                triggerAction.run();
            } catch (Exception e) {
                throw new RpcCommunicationException("Failed to execute action before waiting", e);
            }

            try {
                lock.wait(timeout);
            } catch (InterruptedException e) {
                throw new RpcCommunicationException("Failed to wait for response", e);
            }
        }

        boolean success = received.get();
        if(!success) {
            throw new RpcCommunicationException.Timeout(timeout);
        }

        RpcCommunicationException err = error.get();
        if(err != null) {
            synchronized (lock) {
                received.set(false);
                result.set(null);
                error.set(null);
            }
            throw err;
        }

        T res = result.get();
        synchronized (lock) {
            received.set(false);
            result.set(null);
            error.set(null);
        }
        return res;
    }

    /**
     * Waits for the next message to this receiver. <br><br>
     *
     * Example to wait for saving to complete after triggering it:
     * <pre>
     * {@code
     *  IncomingReceiver.ParameterlessNotification receiver = RpcNotifications.Server.saved();
     *  receiver.register(managementHandler.notificationHandler);
     *  receiver.waitForNext(
     *      () -> { if(!RpcMethods.Server.SAVE.sendBlocking(true, managementHandler)) throw new IOException("Failed to trigger save"); }
     *  );
     * }
     * </pre>
     *
     * @param triggerAction A function that may be used to execute any events that trigger the message to wait for. It is guaranteed, that any messages received by this receiver during and after the execution of {@code triggerAction} will be returned here.
     * @return The message parameter.
     * @throws RpcCommunicationException If there is an error receiving the message.
     */
    public T waitForNext(CheckedAction triggerAction) throws RpcCommunicationException {
        return waitForNext(triggerAction, DEFAULT_WAIT_TIMEOUT);
    }

    /**
     * Registers this receiver on an incoming handler for a specified time, causing it to receive messages from it.
     * @param handler The management handler to register on.
     * @param timeout The time after which this receiver is unregistered, in milliseconds.
     */
    public void register(IncomingHandler<R,I> handler, long timeout) {
        handler.register(this);
        registeredOn.add(handler);
        if(timeout > 0) {
            new Thread(() -> {
                try {
                    Thread.sleep(timeout);
                    if (unregister(handler)) {
                        onError(new RpcCommunicationException.Timeout(timeout));
                    }
                } catch (InterruptedException e) {
                    if (unregister(handler)) {
                        onError(new RpcCommunicationException("Failed to wait for response", e));
                    }
                }
            }).start();
        }
    }

    /**
     * Registers this receiver on an incoming handler, causing it to receive messages from it.
     * @param handler The handler to register on.
     */
    public void register(IncomingHandler<R,I> handler) {
        register(handler, -1);
    }

    /**
     * Unregisters this receiver from an incoming handler, causing it to no longer receive messages from it.
     * @param handler The handler to unregister from.
     * @return {@code true} if the handler was unregistered, {@code false} if the handler was not registered.
     */
    public boolean unregister(IncomingHandler<R,I> handler) {
        return handler.unregister(this);
    }

    @Override
    public I identification() {
        return identification;
    }

    void onResult(T result) {
        resultConsumer.accept(result);
        synchronized (lock) {
            this.result.set(result);
            this.received.set(true);
            lock.notify();
        }
    }

    void onError(RpcCommunicationException error) {
        errorConsumer.accept(error);
        synchronized (lock) {
            this.error.set(error);
            this.received.set(true);
            lock.notify();
        }
    }

    public static class Notification<T> extends IncomingReceiver<RpcNotification, T, String> {
        public Notification(String method, DataSerializer<T> serializer, Consumer<T> resultConsumer, boolean unregisterOnReceive) {
            super(method, serializer, resultConsumer, e -> {}, unregisterOnReceive);
        }
    }

    public static class ParameterlessNotification extends Notification<Void> {
        public ParameterlessNotification(String method, Runnable onReceived, boolean unregisterOnReceive) {
            super(method, DataSerializer.VOID, r -> onReceived.run(), unregisterOnReceive);
        }
    }

    public static class Method<T, R> extends IncomingReceiver<RpcRequest, RequestCompound<T>, String> {
        public Method(String method, DataSerializer<T> incomingSerializer, DataSerializer<R> outgoingSerializer, RpcErrorFunction<T,R> methodFunction, Consumer<RpcCommunicationException> errorConsumer, ManagementHandler handler, boolean unregisterOnResult) {
            super(method, requestSerializer(incomingSerializer), r -> handleRequest(r, outgoingSerializer, methodFunction, errorConsumer, handler), errorConsumer, unregisterOnResult);
        }

        private static <T> DataSerializer<RequestCompound<T>> requestSerializer(DataSerializer<T> dataSerializer) {
            return new DataSerializer<>() {
                @Override
                public JsonElement serialize(RequestCompound<T> data) {
                    JsonObject o = new JsonObject();
                    o.add("id", new JsonPrimitive(data.id()));
                    o.add("data", dataSerializer.serialize(data.data()));
                    return o;
                }

                @Override
                public RequestCompound<T> deserialize(JsonElement json) throws RpcCommunicationException {
                    if (!json.isJsonObject())
                        throw new RpcCommunicationException(-32000, "Data is not a request object");
                    JsonObject o = json.getAsJsonObject();
                    if (!o.has("id")) throw new RpcCommunicationException(-32000, "Request contains no id");
                    if (!o.has("data")) throw new RpcCommunicationException(-32000, "Request contains no data");
                    JsonElement id = o.get("id");
                    if (!id.isJsonPrimitive() || !id.getAsJsonPrimitive().isNumber())
                        throw new RpcCommunicationException(-32000, "Request id is not a number");
                    return new RequestCompound<>(
                            id.getAsInt(),
                            dataSerializer.deserialize(o.get("data"))
                    );
                }
            };
        }

        private static <T,R> void handleRequest(RequestCompound<T> request, DataSerializer<R> outgoingSerializer, RpcErrorFunction<T,R> resultFunction, Consumer<RpcCommunicationException> errorHandler, ManagementHandler handler) {
            R result = null;
            RpcCommunicationException error = null;
            try {
                result = resultFunction.apply(request.data());
            } catch (RpcCommunicationException e) {
                error = e;
            }
            try {
                handler.sendConstructedMessage(new RpcResponse(
                        request.id(),
                        result == null ? null : outgoingSerializer.serialize(result),
                        error
                ).serialize());
            } catch (RpcCommunicationException e) {
                errorHandler.accept(e);
            }
        }
    }

    public static class Response<T> extends IncomingReceiver<RpcResponse, T, Integer> {
        public Response(int identification, DataSerializer<T> serializer, Consumer<T> resultConsumer, Consumer<RpcCommunicationException> errorConsumer) {
            super(identification, serializer, resultConsumer, errorConsumer, true);
        }

        @Override
        public void receive(RpcResponse message) {
            if(message.error() != null) {
                onError(message.error());
                return;
            }
            super.receive(message);
        }
    }

    public interface CheckedAction {
        void run() throws Exception;
    }

    /**
     * Constructs a notification receiver.
     * @param method The method to receiver notifications on.
     * @param serializer The serializer to deserialize the notification parameter with.
     * @param resultConsumer A method that is called with the notification parameter when a notification is received.
     * @param unregisterOnReceive If {@code true}, the handler will be unregistered after the first notification was received.
     * @return The notification receiver.
     * @param <T> The type of the notification parameter.
     */
    public static <T> Notification<T> notification(String method, DataSerializer<T> serializer, Consumer<T> resultConsumer, boolean unregisterOnReceive) {
        return new Notification<>(method, serializer, resultConsumer, unregisterOnReceive);
    }

    /**
     * Constructs a notification receiver without a result consumer. This should only be used for awaiting notifications.
     * @param method The method to receiver notifications on.
     * @param serializer The serializer to deserialize the notification parameter with.
     * @param unregisterOnReceive If {@code true}, the handler will be unregistered after the first notification was received.
     * @return The notification receiver.
     * @param <T> The type of the notification parameter.
     */
    public static <T> Notification<T> notification(String method, DataSerializer<T> serializer, boolean unregisterOnReceive) {
        return notification(method, serializer, r -> {}, unregisterOnReceive);
    }

    /**
     * Constructs a notification receiver.
     * @param method The method to receiver notifications on.
     * @param serializer The serializer to deserialize the notification parameter with.
     * @param resultConsumer A method that is called with the notification parameter when a notification is received.
     * @return The notification receiver.
     * @param <T> The type of the notification parameter.
     */
    public static <T> Notification<T> notification(String method, DataSerializer<T> serializer, Consumer<T> resultConsumer) {
        return notification(method, serializer, resultConsumer, false);
    }

    /**
     * Constructs a notification receiver without a result consumer. This should only be used for awaiting notifications.
     * @param method The method to receiver notifications on.
     * @param serializer The serializer to deserialize the notification parameter with.
     * @return The notification receiver.
     * @param <T> The type of the notification parameter.
     */
    public static <T> Notification<T> notification(String method, DataSerializer<T> serializer) {
        return notification(method, serializer, r -> {});
    }

    /**
     * Constructs a notification receiver.
     * @param method The method to receiver notifications on.
     * @param responseType A TypeToken, which is used to deserialize the notification parameter.
     * @param resultConsumer A method that is called with the notification parameter when a notification is received.
     * @param unregisterOnReceive If {@code true}, the handler will be unregistered after the first notification was received.
     * @return The notification receiver.
     * @param <T> The type of the notification parameter.
     */
    public static <T> Notification<T> notification(String method, TypeToken<T> responseType, Consumer<T> resultConsumer, boolean unregisterOnReceive) {
        return notification(method, DataSerializer.forType(responseType), resultConsumer, unregisterOnReceive);
    }

    /**
     * Constructs a notification receiver without a result consumer. This should only be used for awaiting notifications.
     * @param method The method to receiver notifications on.
     * @param responseType A TypeToken, which is used to deserialize the notification parameter.
     * @param unregisterOnReceive If {@code true}, the handler will be unregistered after the first notification was received.
     * @return The notification receiver.
     * @param <T> The type of the notification parameter.
     */
    public static <T> Notification<T> notification(String method, TypeToken<T> responseType, boolean unregisterOnReceive) {
        return notification(method, responseType, r -> {}, unregisterOnReceive);
    }

    /**
     * Constructs a notification receiver.
     * @param method The method to receiver notifications on.
     * @param responseType A TypeToken, which is used to deserialize the notification parameter.
     * @param resultConsumer A method that is called with the notification parameter when a notification is received.
     * @return The notification receiver.
     * @param <T> The type of the notification parameter.
     */
    public static <T> Notification<T> notification(String method, TypeToken<T> responseType, Consumer<T> resultConsumer) {
        return notification(method, DataSerializer.forType(responseType), resultConsumer, false);
    }

    /**
     * Constructs a notification receiver without a result consumer. This should only be used for awaiting notifications.
     * @param method The method to receiver notifications on.
     * @param responseType A TypeToken, which is used to deserialize the notification parameter.
     * @return The notification receiver.
     * @param <T> The type of the notification parameter.
     */
    public static <T> Notification<T> notification(String method, TypeToken<T> responseType) {
        return notification(method, DataSerializer.forType(responseType), r -> {});
    }

    /**
     * Constructs a notification receiver with an empty notification parameter.
     * @param method The method to receiver notifications on.
     * @param onReceive A method that is called when a notification is received.
     * @param unregisterOnReceive If {@code true}, the handler will be unregistered after the first notification was received.
     * @return The notification receiver.
     */
    public static ParameterlessNotification notification(String method, Runnable onReceive, boolean unregisterOnReceive) {
        return new ParameterlessNotification(method, onReceive, unregisterOnReceive);
    }

    /**
     * Constructs a notification receiver with an empty notification parameter and without a result consumer. This should only be used for awaiting notifications.
     * @param method The method to receiver notifications on.
     * @param unregisterOnReceive If {@code true}, the handler will be unregistered after the first notification was received.
     * @return The notification receiver.
     */
    public static ParameterlessNotification notification(String method, boolean unregisterOnReceive) {
        return notification(method, () -> {}, unregisterOnReceive);
    }

    /**
     * Constructs a notification receiver with an empty notification parameter.
     * @param method The method to receiver notifications on.
     * @param onReceive A method that is called when a notification is received.
     * @return The notification receiver.
     */
    public static ParameterlessNotification notification(String method, Runnable onReceive) {
        return notification(method, onReceive, false);
    }

    /**
     * Constructs a notification receiver with an empty notification parameter and without a result consumer. This should only be used for awaiting notifications.
     * @param method The method to receiver notifications on.
     * @return The notification receiver.
     */
    public static ParameterlessNotification notification(String method) {
        return notification(method, () -> {});
    }

    public static <T> Response<T> response(int identification, DataSerializer<T> serializer, Consumer<T> resultConsumer, Consumer<RpcCommunicationException> errorConsumer) {
        return new Response<>(identification, serializer, resultConsumer, errorConsumer);
    }

    public static <T> Response<T> response(int identification, DataSerializer<T> serializer) {
        return response(identification, serializer, r -> {}, e -> {});
    }

    public static <T> Response<T> response(int identification, TypeToken<T> responseType, Consumer<T> resultConsumer, Consumer<RpcCommunicationException> errorConsumer) {
        return response(identification, DataSerializer.forType(responseType), resultConsumer, errorConsumer);
    }

    public static <T> Response<T> response(int identification, TypeToken<T> responseType) {
        return response(identification, responseType, r -> {}, e -> {});
    }
}
