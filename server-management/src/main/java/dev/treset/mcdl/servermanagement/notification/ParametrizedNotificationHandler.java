package dev.treset.mcdl.servermanagement.notification;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import dev.treset.mcdl.json.SerializationException;

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

    public void handle(RpcNotification notification) throws SerializationException {
        if(!notification.method().equals(method)) {
            throw new SerializationException("Unexpected notification method, expected: " + method + ", got: " + notification.method());
        }
        if(notification.params().size() != 1) {
            throw new SerializationException("Unexpected number of parameters, expected: 1, got: " + notification.params().size());
        }

        try {
            T content = GSON.fromJson(notification.params().get(0), token);
            handler.accept(content);
        } catch (JsonSyntaxException e) {
            throw new SerializationException("Failed to parse notification parameter for method: " + method, e);
        }
    }
}
