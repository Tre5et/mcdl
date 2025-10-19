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
        if(!notification.params().isEmpty()) {
            throw new IOException("Unexpected number of parameters, expected: 0, got: " + notification.params().size());
        }

        handler.run();
    }
}
