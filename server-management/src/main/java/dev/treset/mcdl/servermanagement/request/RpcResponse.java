package dev.treset.mcdl.servermanagement.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.Strictness;
import com.google.gson.reflect.TypeToken;
import dev.treset.mcdl.servermanagement.data.RpcError;

import java.io.IOException;

public interface RpcResponse {
    Gson GSON = new GsonBuilder().setStrictness(Strictness.STRICT).create();

    String jsonrpc();
    Integer id();
    /**
     * The result, set if the request was successfully executed.
     * @return The result.
     */
    Object result();
    /**
     * The error, set if the request failed to execute.
     * @return The error.
     */
    RpcError error();

    default boolean hasResult() {
        return result() != null;
    }

    /**
     * Parses the result as a specific type.
     * @param typeToken A type token representing the type to be converted to.
     * @return The result content as the specified type.
     * @param <T> The type to be converted to.
     * @throws IOException If there is an error converting to the type.
     */
    default <T> T resultAs(TypeToken<T> typeToken) throws IOException {
        if(!hasResult()) {
            throw new IOException("Response has no result");
        }
        String content = GSON.toJson(result());
        try {
            return GSON.fromJson(content, typeToken);
        } catch (JsonSyntaxException e) {
            throw new IOException("Failed to parse content", e);
        }
    }

    /**
     * Parses the result as a specific type.
     * @param type The class to be converted to.
     * @return The result content as the specified type.
     * @param <T> The type to be converted to.
     * @throws IOException If there is an error converting to the type.
     */
    default <T> T resultAs(Class<T> type) throws IOException {
        return resultAs(TypeToken.get(type));
    }

    /**
     * Parses the result as a boolean.
     * @return The result as a boolean.
     * @throws IOException If there is an error converting the result to a boolean.
     */
    default boolean resultAsBoolean() throws IOException {
        if(!hasResult()) {
            throw new IOException("Response has no result");
        }
        try {
            return (boolean)result();
        } catch (ClassCastException e) {
            throw new IOException("Unable to convert result to boolean: " + result(), e);
        }
    }

    /**
     * Parses the result as a double.
     * @return The result as a double.
     * @throws IOException If there is an error converting the result to a double.
     */
    default double resultAsDouble() throws IOException {
        if(!hasResult()) {
            throw new IOException("Response has no result");
        }
        try {
            return (double)result();
        } catch (ClassCastException e) {
            throw new IOException("Unable to convert result to double: " + result(), e);
        }
    }

    /**
     * Parses the result as an integer.
     * @return The result as an integer.
     * @throws IOException If there is an error converting the result to an integer.
     */
    default int resultAsInt() throws IOException {
        double number = resultAsDouble();
        if(number % 1 != 0) {
            throw new IOException("Result is a number but not a integer: " + number);
        }
        return (int)Math.round(number);
    }

    /**
     * Parses the result as a string.
     * @return The result as a string.
     * @throws IOException If there is an error converting the result to a string.
     */
    default String resultAsString() throws IOException {
        if(!hasResult()) {
            throw new IOException("Response has no result");
        }
        try {
            return (String)result();
        } catch (ClassCastException e) {
            throw new IOException("Unable to convert result to string: " + result(), e);
        }
    }

    static RpcResponse Timeout(int id) {
        return new RpcResponse() {
            @Override
            public String jsonrpc() {
                return "2.0";
            }

            @Override
            public Integer id() {
                return id;
            }

            @Override
            public Object result() {
                return null;
            }

            @Override
            public RpcError error() {
                return new RpcError(-1, "Timed out", "Server took more than 10 seconds to respond");
            }
        };
    }
}
