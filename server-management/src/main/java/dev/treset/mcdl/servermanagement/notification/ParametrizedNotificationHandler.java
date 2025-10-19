package dev.treset.mcdl.servermanagement.notification;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.function.Consumer;

public class ParametrizedNotificationHandler<T> extends NotificationHandler {
    private final String method;
    private final TypeToken<T> token;
    private final Consumer<T> handler;

    public ParametrizedNotificationHandler(String method, TypeToken<T> token, Consumer<T> handler) {
        this.method = method;
        this.token = token;
        this.handler = handler;
    }

    public void handle(RpcNotification notification) throws IOException {
        if(!notification.method().equals(method)) {
            throw new IOException("Unexpected notification method, expected: " + method + ", got: " + notification.method());
        }
        if(notification.params().size() != 1) {
            throw new IOException("Unexpected number of parameters, expected: 1, got: " + notification.params().size());
        }
        String serializedContent = GSON.toJson(notification.params().get(0));

        try {
            T content = GSON.fromJson(serializedContent, token);
            handler.accept(content);
        } catch (JsonSyntaxException e) {
            throw new IOException("Failed to parse notification parameter for method: " + method, e);
        }
    }
}
