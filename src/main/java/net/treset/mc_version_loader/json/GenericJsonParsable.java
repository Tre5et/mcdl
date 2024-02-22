package net.treset.mc_version_loader.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

public abstract class GenericJsonParsable implements JsonParsable {
    public static <T> T fromJson(String json, Class<? extends T> type, Gson gson) throws SerializationException {
        try {
            return gson.fromJson(json, type);
        } catch (Exception e) {
            throw new SerializationException("Failed to deserialize JSON", e);
        }
    }
    public static <T> T fromJson(String json, Class<? extends T> type) throws SerializationException {
        return fromJson(json, type, JsonUtils.getGson());
    }

    public static <T> List<T> fromJsonArray(String json, TypeToken<List<T>> typeToken, Gson gson) throws SerializationException {
        try {
            return gson.fromJson(json, typeToken);
        } catch (Exception e) {
            throw new SerializationException("Failed to deserialize JSON", e);
        }
    }

    public static <T> List<T> fromJsonArray(String json, TypeToken<List<T>> typeToken) throws SerializationException {
        return fromJsonArray(json, typeToken, JsonUtils.getGson());
    }

    public static <T> T fromJson(String json, TypeToken<T> typeToken) throws SerializationException {
        return fromJson(json, typeToken, JsonUtils.getGson());
    }

    public static <T> T fromJson(String json, TypeToken<T> typeToken, Gson gson) throws SerializationException {
        try {
            return gson.fromJson(json, typeToken);
        } catch (Exception e) {
            throw new SerializationException("Failed to deserialize JSON", e);
        }
    }

    @Override
    public String toJson() {
        return JsonUtils.getGson().toJson(this);
    }

    @Override
    public void writeToFile(String filePath) throws IOException {
        JsonUtils.writeJsonToFile(this, filePath);
    }
}
