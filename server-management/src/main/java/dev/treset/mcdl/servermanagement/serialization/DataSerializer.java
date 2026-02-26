package dev.treset.mcdl.servermanagement.serialization;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import dev.treset.mcdl.servermanagement.exception.RpcCommunicationException;

import java.time.Instant;

public interface DataSerializer<T> {
    JsonElement serialize(T data);

    T deserialize(JsonElement json) throws RpcCommunicationException;

    DataSerializer<Void> VOID = new DataSerializer<>() {
        @Override
        public JsonElement serialize(Void data) {
            return null;
        }

        @Override
        public Void deserialize(JsonElement json) {
            return null;
        }
    };

    DataSerializer<JsonElement> JSON_ELEMENT = new DataSerializer<>() {
        @Override
        public JsonElement serialize(JsonElement data) {
            return data;
        }

        @Override
        public JsonElement deserialize(JsonElement json) {
            return json;
        }
    };

    DataSerializer<Integer> INTEGER = new DataSerializer<>() {
        @Override
        public JsonElement serialize(Integer data) {
            return new JsonPrimitive(data);
        }

        @Override
        public Integer deserialize(JsonElement json) throws RpcCommunicationException {
            if (!json.isJsonPrimitive() || !json.getAsJsonPrimitive().isNumber() || json.getAsInt() != json.getAsDouble()) {
                throw new RpcCommunicationException("Data is not an integer", json);
            }
            return json.getAsInt();
        }
    };

    Gson GSON = gsonBuilder()
            .setFieldNamingStrategy(FieldNamingPolicy.IDENTITY)
            .enableComplexMapKeySerialization()
            .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
            .create();

    static GsonBuilder gsonBuilder() {
        //System.setProperty("gson.allowCapturingTypeVariables", "true");
        return new GsonBuilder();
    }

    static <T> DataSerializer<T> forType(TypeToken<T> typeToken) {
        return new DataSerializer<>() {
            @Override
            public JsonElement serialize(T data) {
                return GSON.toJsonTree(data);
            }

            @Override
            public T deserialize(JsonElement json) throws RpcCommunicationException {
                try {
                    return GSON.fromJson(json, typeToken);
                } catch (Exception e) {
                    throw new RpcCommunicationException("Failed to deserializer response", json, e);
                }
            }
        };
    }

    static <T> DataSerializer<T> forType(Class<T> clazz) {
        return forType(TypeToken.get(clazz));

    }
}
