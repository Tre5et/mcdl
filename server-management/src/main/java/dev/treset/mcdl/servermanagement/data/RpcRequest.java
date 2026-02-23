package dev.treset.mcdl.servermanagement.data;

import com.google.gson.*;

public record RpcRequest(
        int id,
        String method,
        JsonArray params
) {
    public static final Gson GSON = new Gson();

    public String serialize() {
        JsonObject result =  new JsonObject();
        result.add("id", new JsonPrimitive(id));
        result.add("method", new JsonPrimitive(method));
        result.add("params", params);
        return result.toString();
    }

    public static RpcRequest create(int id, String method, JsonArray params) {
        return new RpcRequest(
                id,
                method,
                params
        );
    }
}
