package dev.treset.mcdl.servermanagement.notification;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.Strictness;
import dev.treset.mcdl.json.SerializationException;

import java.io.IOException;

public abstract class NotificationHandler {
    public static final Gson GSON = new GsonBuilder()
            .setStrictness(Strictness.STRICT)
            .create();

    public abstract void handle(RpcNotification notification) throws SerializationException;
}
