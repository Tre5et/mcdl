package dev.treset.mcdl.servermanagement.data;

import com.google.gson.*;
import dev.treset.mcdl.json.JsonUtils;
import dev.treset.mcdl.json.SerializationException;
import dev.treset.mcdl.servermanagement.exception.RpcCommunicationException;
import dev.treset.mcdl.servermanagement.notification.RpcNotification;

import java.util.Objects;

public record RpcMessage(
        String jsonrpc,
        Integer id,
        JsonElement result,
        JsonElement error,
        String method,
        JsonArray params
) {
    private static final Gson GSON = new Gson();

    public boolean isResponse() {
        return Objects.equals(jsonrpc, "2.0") && id != null && (result != null || error != null);
    }

    public RpcResponse asResponse() {
        RpcCommunicationException ex;
        if(error == null) {
            ex = null;
        } else {
            try {
                ex = RpcCommunicationException.fromError(error);
            } catch (SerializationException e) {
                ex = new RpcCommunicationException("Unable to deserialize error", error, e);
            }
        }

        return new RpcResponse(id, result, ex);
    }

    public boolean isNotification() {
        return Objects.equals(jsonrpc, "2.0") && method != null && id == null;
    }

    public RpcNotification asNotification() {
        return new RpcNotification() {
            @Override
            public String jsonrpc() {
                return jsonrpc;
            }

            @Override
            public String method() {
                return method;
            }

            @Override
            public JsonArray params() {
                return params;
            }
        };
    }

    public boolean isRequest() {
        return Objects.equals(jsonrpc, "2.0") && id != null && method != null;
    }

    public RpcRequest asRequest() {
        return new RpcRequest(
                id,
                method,
                params
        );
    }

    public static RpcMessage fromJson(String json) throws SerializationException {
        try {
            JsonElement element = JsonParser.parseString(json);
            if(!element.isJsonObject()) {
                throw new SerializationException("Message is not a JSON object: " + element);
            }
            JsonObject obj = element.getAsJsonObject();
            return new RpcMessage(
                    JsonUtils.getAsString(obj, "jsonrpc"),
                    JsonUtils.getAsInt(obj, "id"),
                    obj.has("result") ? obj.get("result") : null,
                    obj.has("error") ? obj.get("error") : null,
                    JsonUtils.getAsString(obj, "method"),
                    JsonUtils.getAsJsonArray(obj, "params")
            );
        } catch (JsonParseException e) {
            throw new SerializationException(e.getMessage(), e.getCause());
        }
    }
}