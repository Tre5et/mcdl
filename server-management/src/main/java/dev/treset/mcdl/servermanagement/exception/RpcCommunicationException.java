package dev.treset.mcdl.servermanagement.exception;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import dev.treset.mcdl.json.JsonUtils;
import dev.treset.mcdl.json.SerializationException;

import java.io.IOException;

public class RpcCommunicationException extends IOException {
    public static final Gson GSON = new GsonBuilder().setStrictness(Strictness.STRICT).create();

    private final int code;
    private final JsonElement data;

    public RpcCommunicationException(int code, String message, JsonElement data) {
        super(message);
        this.code = code;
        this.data = data;
    }

    public RpcCommunicationException(int code, String message, JsonElement data, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.data = data;
    }

    public RpcCommunicationException(int code, String message) {
        this(code, message, (JsonElement) null);
    }

    public RpcCommunicationException(int code, String message, Throwable cause) {
        this(code, message, null, cause);
    }

    public RpcCommunicationException(String message) {
        this(-1, message);
    }

    public RpcCommunicationException(String message, Throwable cause) {
        this(-1, message, cause);
    }

    public RpcCommunicationException(String message, JsonElement data) {
        this(-1, message, data);
    }

    public RpcCommunicationException(String message, JsonElement data, Throwable cause) {
        this(-1, message, data, cause);
    }

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

    public static RpcCommunicationException fromError(JsonElement e) throws SerializationException {
        JsonObject o = JsonUtils.getAsJsonObject(e);
        return new RpcCommunicationException(
                JsonUtils.getAsInt(o, "code"),
                JsonUtils.getAsString(o, "message"),
                o.has("data") ? o.get("data") : null
        );
    }

    public static class Timeout extends RpcCommunicationException {
        public Timeout(long timeout) {
            super("Timed out", new JsonPrimitive(String.format("The server took more than %.2f seconds to respond.", timeout / 1000d)));
        }
    }
}
