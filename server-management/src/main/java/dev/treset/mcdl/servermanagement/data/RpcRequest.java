package dev.treset.mcdl.servermanagement.data;

import com.google.gson.*;
import dev.treset.mcdl.servermanagement.exception.RpcCommunicationException;

public record RpcRequest(
        int id,
        String method,
        JsonArray params
) implements DataProvider, IdentificationProvider<String> {
    public static final Gson GSON = new Gson();

    public String serialize() {
        JsonObject result =  new JsonObject();
        result.add("jsonrpc", new JsonPrimitive("2.0"));
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

    @Override
    public JsonElement data() throws RpcCommunicationException {
        JsonElement data = params() == null ? null : (params().isEmpty() ? null : params().get(0));
        JsonObject returnObject = new JsonObject();
        returnObject.add("id", new JsonPrimitive(id()));
        returnObject.add("data", data);
        return returnObject;
    }

    @Override
    public String identification() {
        return method();
    }
}
