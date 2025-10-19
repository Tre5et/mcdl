package dev.treset.mcdl.servermanagement.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.Strictness;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

public record RpcError(
        int code,
        String message,
        Object data
) {
    public static final Gson GSON = new GsonBuilder().setStrictness(Strictness.STRICT).create();

    public boolean hasData() {
        return data != null;
    }

    public <T> T dataAs(TypeToken<T> typeToken) throws IOException {
        if(data == null) {
            throw new IOException("Error has no data");
        }
        String content = GSON.toJson(data);
        try {
            return GSON.fromJson(content, typeToken);
        } catch (JsonSyntaxException e) {
            throw new IOException("Failed to parse content", e);
        }
    }
}

