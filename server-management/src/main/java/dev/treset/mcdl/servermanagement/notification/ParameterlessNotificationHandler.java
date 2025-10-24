package dev.treset.mcdl.servermanagement.notification;

import java.io.IOException;

public class ParameterlessNotificationHandler extends NotificationHandler {
    private final String method;
    private final Runnable handler;

    public ParameterlessNotificationHandler(String method, Runnable handler) {
        this.method = method;
        this.handler = handler;
    }

    @Override
    public void handle(RpcNotification notification) throws IOException {
        if(!notification.method().equals(method)) {
            throw new IOException("Unexpected notification method, expected: " + method + ", got: " + notification.method());
        }
        if(notification.params() != null) {
            throw new IOException("Unexpected parameters, expected none, got: " + notification.params().size());
        }

        handler.run();
    }
}
