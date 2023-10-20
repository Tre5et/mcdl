package net.treset.mc_version_loader.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

public abstract class GenericJsonParsable implements JsonParsable {
    public static <T> T fromJson(String json, Class<? extends T> type, Gson gson) {
        return gson.fromJson(json, type);
    }
    public static <T> T fromJson(String json, Class<? extends T> type) {
        return fromJson(json, type, JsonUtils.getGson());
    }

    public static <T> List<T> fromJson(String json, TypeToken<List<T>> typeToken, Gson gson) {
        return gson.fromJson(json, typeToken);
    }

    public static <T> List<T> fromJson(String json, TypeToken<List<T>> typeToken) {
        return fromJson(json, typeToken, JsonUtils.getGson());
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
