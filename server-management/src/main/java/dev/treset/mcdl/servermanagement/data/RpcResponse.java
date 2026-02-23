package dev.treset.mcdl.servermanagement.data;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import dev.treset.mcdl.json.JsonUtils;
import dev.treset.mcdl.json.SerializationException;
import dev.treset.mcdl.servermanagement.exception.RpcCommunicationException;

public interface RpcResponse extends DataProvider, IdentificationProvider<Integer> {
    Gson GSON = new GsonBuilder().create();

    String jsonrpc();
    Integer id();
    /**
     * The result, set if the request was successfully executed.
     * @return The result.
     */
    JsonElement result();
    /**
     * The error, set if the request failed to execute.
     * @return The error.
     */
    RpcCommunicationException error();

    default boolean hasResult() {
        return result() != null;
    }

    @Override
    default JsonElement data() throws RpcCommunicationException {
        if(error() != null) {
            throw error();
        }
        return result();
    }

    @Override
    default Integer identification() {
        return id();
    }

    /**
     * Parses the result as a specific type.
     * @param typeToken A type token representing the type to be converted to.
     * @return The result content as the specified type.
     * @param <T> The type to be converted to.
     * @throws SerializationException If there is an error converting to the type.
     */
    default <T> T resultAs(TypeToken<T> typeToken) throws SerializationException {
        if(!hasResult()) {
            throw new SerializationException("Response has no result");
        }
        String content = GSON.toJson(result());
        try {
            T result = GSON.fromJson(result(), typeToken);
            if(result == null) {
                throw new SerializationException("Failed to parse result");
            }
            return result;
        } catch (JsonSyntaxException e) {
            throw new SerializationException("Failed to parse content", e);
        }
    }

    /**
     * Parses the result as a specific type.
     * @param type The class to be converted to.
     * @return The result content as the specified type.
     * @param <T> The type to be converted to.
     * @throws SerializationException If there is an error converting to the type.
     */
    default <T> T resultAs(Class<T> type) throws SerializationException {
        return resultAs(TypeToken.get(type));
    }

    /**
     * Parses the result as a boolean.
     * @return The result as a boolean.
     * @throws SerializationException If there is an error converting the result to a boolean.
     */
    default boolean resultAsBoolean() throws SerializationException {
        if(!hasResult()) {
            throw new SerializationException("Response has no result");
        }
        return JsonUtils.getAsBoolean(result());
    }

    /**
     * Parses the result as a double.
     * @return The result as a double.
     * @throws SerializationException If there is an error converting the result to a double.
     */
    default double resultAsDouble() throws SerializationException {
        if(!hasResult()) {
            throw new SerializationException("Response has no result");
        }
        return JsonUtils.getAsDouble(result());
    }

    /**
     * Parses the result as an integer.
     * @return The result as an integer.
     * @throws SerializationException If there is an error converting the result to an integer.
     */
    default int resultAsInt() throws SerializationException {
        double number = resultAsDouble();
        if(number % 1 != 0) {
            throw new SerializationException("Result is a number but not a integer: " + number);
        }
        return (int)Math.round(number);
    }

    /**
     * Parses the result as a string.
     * @return The result as a string.
     * @throws SerializationException If there is an error converting the result to a string.
     */
    default String resultAsString() throws SerializationException {
        if (!hasResult()) {
            throw new SerializationException("Response has no result");
        }
        return JsonUtils.getAsString(result());
    }
}
