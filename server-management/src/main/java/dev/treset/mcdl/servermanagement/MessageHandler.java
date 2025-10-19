package dev.treset.mcdl.servermanagement;

import com.google.gson.reflect.TypeToken;
import dev.treset.mcdl.servermanagement.data.RpcMessage;
import dev.treset.mcdl.servermanagement.notification.NotificationHandler;
import dev.treset.mcdl.servermanagement.notification.ParameterlessNotificationHandler;
import dev.treset.mcdl.servermanagement.notification.ParametrizedNotificationHandler;
import dev.treset.mcdl.servermanagement.notification.RpcNotification;
import dev.treset.mcdl.servermanagement.request.MessageSender;
import dev.treset.mcdl.servermanagement.request.RpcRequest;
import dev.treset.mcdl.servermanagement.request.RpcResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MessageHandler {
    private final MessageSender sender;
    private BiConsumer<String, Exception> warningHandler = (s,e) -> {};

    private final int RESPONSE_TIMEOUT = 10_000;
    private final Random RANDOM = new Random();

    private final Map<Integer, ResponseHandler> responseHandlers = new HashMap<>();
    private final Map<String, NotificationHandler> notificationHandlers = new HashMap<>();

    public MessageHandler(MessageSender sender) {
        this.sender = sender;
    }

    public void handleMessage(String content) {
        try {
            RpcMessage message = RpcMessage.fromJson(content);
            if(message.isResponse()) {
                handleResponse(message);
                return;
            }
            if(message.isNotification()) {
                handleNotification(message);
                return;
            }
            handleUnexpected(content, null);
        } catch (IOException e) {
            handleUnexpected(content, e);
        }
    }

    public int send(String method, Consumer<RpcResponse> responseCallback, Object... params) throws IOException {
        int id = generateUniqueId();
        responseHandlers.put(id, new ResponseHandler(method, params, responseCallback));
        try {
            sender.send(constructMessage(id, method, params));
        } catch (IOException e) {
            responseHandlers.remove(id);
            throw e;
        }
        return id;
    }

    public RpcResponse request(long timeoutMS, String method, Object... params) throws IOException {
        Object lock = new Object();

        int id = generateUniqueId();

        AtomicReference<RpcResponse> res = new AtomicReference<>();
        responseHandlers.put(id, new ResponseHandler(method, params, r -> {
            synchronized (lock) {
                res.set(r);
                lock.notify();
            }
        }));


        synchronized (lock) {
            try {
                sender.send(constructMessage(id, method, params));
            } catch (IOException e) {
                responseHandlers.remove(id);
                throw e;
            }
            try {
                lock.wait(timeoutMS);
            } catch (InterruptedException e) {
                throw new IOException("Failed to wait for response", e);
            }
        }

        if(res.get() == null) {
            purgeOldResponseHandlers();
            return RpcResponse.Timeout(id);
        }

        return res.get();
    }

    public void addNotificationHandler(String method, NotificationHandler handler) {
        notificationHandlers.put(method, handler);
    }

    public <T> T awaitNotification(Runnable actionBefore, String method, TypeToken<T> type, long timeoutMs) throws IOException {
        Object lock = new Object();

        AtomicReference<T> result = new AtomicReference<>();
        AtomicReference<Boolean> gotten = new AtomicReference<>(false);
        NotificationHandler prevHandler = notificationHandlers.get(method);

        NotificationHandler handler = type == null ?
                new ParameterlessNotificationHandler(method, () -> {
                    gotten.set(true);
                    synchronized (lock) {
                        lock.notify();
                    }
                }) :
                new ParametrizedNotificationHandler<>(method, type, (r) -> {
                    gotten.set(true);
                    result.set(r);
                    synchronized (lock) {
                        lock.notify();
                    }
                });

        addNotificationHandler(method, handler);

        synchronized (lock) {
            actionBefore.run();
            try {
                lock.wait(timeoutMs);
            } catch (InterruptedException e) {
                warningHandler.accept("Interrupted while waiting for notification", e);
            }
        }

        if(!gotten.get()) {
            throw new IOException("Failed to wait for notification: Timeout expired: " + timeoutMs);
        }

        addNotificationHandler(method, prevHandler);
        return result.get();
    }

    private String constructMessage(int id, String method, Object... params) {
        return RpcRequest.create(id, method, params).serialize();
    }

    private int generateUniqueId() {
        int id = RANDOM.nextInt();
        while(responseHandlers.containsKey(id)) {
            id = RANDOM.nextInt();
        }
        return id;
    }

    private void handleResponse(RpcResponse response) {
        ResponseHandler handler = responseHandlers.get(response.id());
        if(handler != null) {
            responseHandlers.remove(response.id());
            handler.callback.accept(response);
        } else {
            warningHandler.accept(String.format("Response with no handler for id = %d; result = %s; error = %s", response.id(), response.result(), response.error()), null);
        }

        purgeOldResponseHandlers();
    }

    private void purgeOldResponseHandlers() {
        for(Map.Entry<Integer, ResponseHandler> entry : responseHandlers.entrySet()) {
            if(entry.getValue().timeoutTime < System.currentTimeMillis()) {
                warningHandler.accept(String.format("Response for id = %d took longer than 10 seconds, assuming lost, method = %s, params = %s", entry.getKey(), entry.getValue().method, Arrays.toString(entry.getValue().params)), null);
                responseHandlers.remove(entry.getKey());
                entry.getValue().callback.accept(RpcResponse.Timeout(entry.getKey()));
            }
        }
    }

    private void handleNotification(RpcNotification notification) {
        if(notificationHandlers.containsKey(notification.method())) {
            try {
                notificationHandlers.get(notification.method()).handle(notification);
            } catch (IOException e) {
                warningHandler.accept("Error in notification handling", e);
            }
        }
    }

    private void handleUnexpected(String content, Exception e) {
        warningHandler.accept(String.format("Unexpected RPC message: '%s'", content), e);
    }

    public void setWarningHandler(BiConsumer<String, Exception> warningHandler) {
        this.warningHandler = warningHandler;
    }

    public class ResponseHandler {
        private final long timeoutTime = System.currentTimeMillis() + RESPONSE_TIMEOUT;
        private final String method;
        private final Object[] params;
        private final Consumer<RpcResponse> callback;

        public ResponseHandler(String method, Object[] params, Consumer<RpcResponse> callback) {
            this.method = method;
            this.params = params;
            this.callback = callback;
        }
    }
}
