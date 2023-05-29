package net.treset.mc_version_loader.json;

import com.google.gson.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonUtils {
    private static Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();
    private static Gson gsonCamelCase = new GsonBuilder().setPrettyPrinting().create();

    public static JsonObject getAsJsonObject(JsonElement element) {
        if(element != null && element.isJsonObject()) {
            return element.getAsJsonObject();
        }
        return null;
    }

    public static JsonArray getAsJsonArray(JsonElement element) {
        if(element != null && element.isJsonArray()) {
            return element.getAsJsonArray();
        }
        return null;
    }

    public static String getAsString(JsonElement element) {
        if(element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            return element.getAsJsonPrimitive().getAsString();
        }
        return null;
    }

    public static int getAsInt(JsonElement element) {
        if(element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            return element.getAsJsonPrimitive().getAsInt();
        }
        return -1;
    }

    public static boolean getAsBoolean(JsonElement element) {
        if(element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isBoolean()) {
            return element.getAsJsonPrimitive().getAsBoolean();
        }
        return false;
    }

    public static JsonObject getAsJsonObject(JsonObject obj, String memberName) {
        if(obj != null && obj.get(memberName) != null && obj.get(memberName).isJsonObject()) {
            return obj.getAsJsonObject(memberName);
        }
        return null;
    }

    public static JsonArray getAsJsonArray(JsonObject obj, String memberName) {
        if(obj != null && obj.get(memberName) != null && obj.get(memberName).isJsonArray()) {
            return obj.getAsJsonArray(memberName);
        }
        return null;
    }

    public static String getAsString(JsonObject obj, String memberName) {
        if(obj != null && obj.get(memberName) != null && obj.get(memberName).isJsonPrimitive()) {
            if (obj.getAsJsonPrimitive(memberName).isString()) {
                return obj.getAsJsonPrimitive(memberName).getAsString();
            }
        }
        return null;
    }

    public static int getAsInt(JsonObject obj, String memberName) {
        if(obj != null && obj.get(memberName) != null && obj.get(memberName).isJsonPrimitive()) {
            if (obj.getAsJsonPrimitive(memberName).isNumber()) {
                return obj.getAsJsonPrimitive(memberName).getAsInt();
            }
        }
        return -1;
    }

    public static boolean getAsBoolean(JsonObject obj, String memberName) {
        if(obj != null && obj.get(memberName) != null && obj.get(memberName).isJsonPrimitive()) {
            if (obj.getAsJsonPrimitive(memberName).isBoolean()) {
                return obj.getAsJsonPrimitive(memberName).getAsBoolean();
            }
        }
        return false;
    }

    public static Set<Map.Entry<String, JsonElement>> getMembers(JsonObject obj) {
        if(obj == null) {
            return null;
        }
        return obj.entrySet();
    }


    public static JsonElement parseJson(String json) throws JsonParseException {
        return com.google.gson.JsonParser.parseString(json);
    }

    public static List<String> parseJsonStringArray(JsonArray jsonStringArray) {
        if(jsonStringArray == null) {
            return null;
        }
        List<String> out = new ArrayList<>();
        for(JsonElement e : jsonStringArray) {
            out.add(getAsString(e));
        }
        return out;
    }

    public static void writeJsonToFile(Object toWrite, String path) throws IOException {
        File file = new File(path);
        if(!file.isFile() && ((!file.getParentFile().isDirectory() && !file.getParentFile().mkdirs()) || !file.createNewFile())) {
            throw new IOException("Unable to create file " + path);
        }
        try (FileWriter writer = new FileWriter(path)) {
            getGson().toJson(toWrite, writer);
            writer.flush();
        } catch (JsonIOException e) {
            throw new IOException("Unable to write to file " + path, e);
        }
    }

    public static Gson getGson() {
        return gson;
    }

    public static void setGson(Gson gson) {
        JsonUtils.gson = gson;
    }

    public static Gson getGsonCamelCase() {
        return gsonCamelCase;
    }

    public static void setGsonCamelCase(Gson gsonCamelCase) {
        JsonUtils.gsonCamelCase = gsonCamelCase;
    }
}
