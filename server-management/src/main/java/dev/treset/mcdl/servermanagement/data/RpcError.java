package dev.treset.mcdl.servermanagement.data;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import dev.treset.mcdl.json.JsonUtils;
import dev.treset.mcdl.json.SerializationException;

import java.io.IOException;

public record RpcError(
        int code,
        String message,
        JsonElement data
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

    public static RpcError fromJson(JsonElement e) throws SerializationException {
        JsonObject o = JsonUtils.getAsJsonObject(e);
        return new RpcError(
                JsonUtils.getAsInt(o, "code"),
                JsonUtils.getAsString(o, "message"),
                o.has("data") ? o.get("data") : null
        );
    }
}

