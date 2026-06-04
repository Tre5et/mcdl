package dev.treset.mcdl.servermanagement.notification;

import dev.treset.mcdl.json.SerializationException;

public class ParameterlessNotificationHandler extends NotificationHandler {
    private final String method;
    private final Runnable handler;

    public ParameterlessNotificationHandler(String method, Runnable handler) {
        this.method = method;
        this.handler = handler;
    }

    @Override
    public void handle(RpcNotification notification) throws SerializationException {
        if(!notification.method().equals(method)) {
            throw new SerializationException("Unexpected notification method, expected: " + method + ", got: " + notification.method());
        }
        if(notification.params() != null) {
            throw new SerializationException("Unexpected parameters, expected none, got: " + notification.params().size());
        }

        handler.run();
    }
}
