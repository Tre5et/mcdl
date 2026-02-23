package dev.treset.mcdl.servermanagement.incoming;

import dev.treset.mcdl.servermanagement.data.DataProvider;
import dev.treset.mcdl.servermanagement.data.IdentificationProvider;
import dev.treset.mcdl.servermanagement.exception.RpcCommunicationException;
import dev.treset.mcdl.servermanagement.notification.RpcNotification;
import dev.treset.mcdl.servermanagement.data.RpcResponse;
import dev.treset.mcdl.servermanagement.serialization.DataSerializer;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class IncomingReceiver<R extends DataProvider & IdentificationProvider<I>, T, I> implements IdentificationProvider<I> {
    private static final long DEFAULT_WAIT_TIMEOUT = 30_000;

    private final I identification;
    private final DataSerializer<T> serializer;
    private final Consumer<T> resultConsumer;
    private final Consumer<RpcCommunicationException> errorConsumer;
    private final boolean unregisterOnResult;

    private final Set<MapContainer<I, IncomingReceiver<R,?,I>>> registeredOn = new HashSet<>();

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
            for(MapContainer<I, IncomingReceiver<R,?,I>> container : registeredOn) {
                unregister(container);
            }
        }
    }

    public T waitForNext(CheckedAction before, long timeout) throws RpcCommunicationException {
        synchronized (lock) {
            try {
                before.run();
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

    public T waitForNext(CheckedAction before) throws RpcCommunicationException {
        return waitForNext(before, DEFAULT_WAIT_TIMEOUT);
    }

    public void register(MapContainer<I, IncomingReceiver<R,?,I>> container, long timeout) {
        Map<I, IncomingReceiver<R,?,I>> map = container.map();
        map.put(identification(), this);
        registeredOn.add(container);
        if(timeout > 0) {
            new Thread(() -> {
                try {
                    Thread.sleep(timeout);
                    if (unregister(container)) {
                        onError(new RpcCommunicationException.Timeout(timeout));
                    }
                } catch (InterruptedException e) {
                    if (unregister(container)) {
                        onError(new RpcCommunicationException("Failed to wait for response", e));
                    }
                }
            }).start();
        }
    }

    public void register(MapContainer<I, IncomingReceiver<R,?,I>> container) {
        register(container, -1);
    }

    public boolean unregister(MapContainer<I, IncomingReceiver<R,?,I>> container) {
        Map<I, IncomingReceiver<R,?,I>> map = container.map();
        IncomingReceiver<R,?,I> current = map.get(identification());
        if (current != this) {
            return false;
        }
        map.remove(identification());
        registeredOn.remove(container);
        return true;
    }

    @Override
    public I identification() {
        return identification;
    }

    private void onResult(T result) {
        resultConsumer.accept(result);
        synchronized (lock) {
            this.result.set(result);
            this.received.set(true);
            lock.notify();
        }
    }

    private void onError(RpcCommunicationException error) {
        errorConsumer.accept(error);
        synchronized (lock) {
            this.error.set(error);
            this.received.set(true);
            lock.notify();
        }
    }

    public static class Notification<T> extends IncomingReceiver<RpcNotification, T, String> {
        public Notification(String method, DataSerializer<T> serializer, Consumer<T> resultConsumer) {
            super(method, serializer, resultConsumer, e -> {}, false);
        }

        public Notification(String method, DataSerializer<T> serializer, Consumer<T> resultConsumer, boolean unregisterOnReceive) {
            super(method, serializer, resultConsumer, e -> {}, unregisterOnReceive);
        }
    }

    public static class ParameterlessNotification extends Notification<Void> {
        public ParameterlessNotification(String method, Runnable onReceived) {
            super(method, DataSerializer.VOID, r -> onReceived.run());
        }
    }

    public static class Response<T> extends IncomingReceiver<RpcResponse, T, Integer> {
        public Response(int identification, DataSerializer<T> serializer, Consumer<T> resultConsumer, Consumer<RpcCommunicationException> errorConsumer) {
            super(identification, serializer, resultConsumer, errorConsumer, true);
        }
    }

    public interface CheckedAction {
        void run() throws Exception;
    }
}
