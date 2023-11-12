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

    public static JsonObject getAsJsonObject(JsonElement element) throws SerializationException {
        if(element == null) {
            return null;
        }
        if(element.isJsonObject()) {
            return element.getAsJsonObject();
        }
        throw new SerializationException("Not a json object: " + element);
    }

    public static JsonArray getAsJsonArray(JsonElement element) throws SerializationException {
        if(element == null) {
            return null;
        }
        if(element.isJsonArray()) {
            return element.getAsJsonArray();
        }
        throw new SerializationException("Not a json array: " + element);
    }

    public static String getAsString(JsonElement element) throws SerializationException {
        if(element == null) {
            return null;
        }
        if(element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            return element.getAsJsonPrimitive().getAsString();
        }
        throw new SerializationException("Not a json string: " + element);
    }

    public static int getAsInt(JsonElement element) throws SerializationException {
        if(element == null) {
            return -1;
        }
        if(element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            return element.getAsJsonPrimitive().getAsInt();
        }
        throw new SerializationException("Not a json int: " + element);
    }

    public static boolean getAsBoolean(JsonElement element) throws SerializationException {
        if(element == null) {
            return false;
        }
        if(element.isJsonPrimitive() && element.getAsJsonPrimitive().isBoolean()) {
            return element.getAsJsonPrimitive().getAsBoolean();
        }
        throw new SerializationException("Not a json boolean: " + element);
    }

    public static JsonObject getAsJsonObject(JsonObject obj, String memberName) throws SerializationException {
        if(obj == null || obj.get(memberName) == null) {
            return null;
        }
        if(obj.get(memberName).isJsonObject()) {
            return obj.getAsJsonObject(memberName);
        }
        throw new SerializationException("Not a json object: " + obj.get(memberName));
    }

    public static JsonArray getAsJsonArray(JsonObject obj, String memberName) throws SerializationException {
        if(obj == null || obj.get(memberName) == null) {
            return null;
        }
        if(obj.get(memberName).isJsonArray()) {
            return obj.getAsJsonArray(memberName);
        }
        throw new SerializationException("Not a json array: " + obj.get(memberName));
    }

    public static String getAsString(JsonObject obj, String memberName) throws SerializationException {
        if(obj == null || obj.get(memberName) == null) {
            return null;
        }
        if(obj.get(memberName).isJsonPrimitive() && obj.getAsJsonPrimitive(memberName).isString()) {
            return obj.getAsJsonPrimitive(memberName).getAsString();
        }
        throw new SerializationException("Not a json string: " + obj.get(memberName));
    }

    public static int getAsInt(JsonObject obj, String memberName) throws SerializationException {
        if(obj == null || obj.get(memberName) == null) {
            return -1;
        }
        if(obj.get(memberName).isJsonPrimitive() && obj.getAsJsonPrimitive(memberName).isNumber()) {
            return obj.getAsJsonPrimitive(memberName).getAsInt();
        }
        throw new SerializationException("Not a json int: " + obj.get(memberName));
    }

    public static boolean getAsBoolean(JsonObject obj, String memberName) throws SerializationException {
        if(obj == null || obj.get(memberName) == null) {
            return false;
        }
        if(obj.get(memberName).isJsonPrimitive() && obj.getAsJsonPrimitive(memberName).isBoolean()) {
            return obj.getAsJsonPrimitive(memberName).getAsBoolean();
        }
        throw new SerializationException("Not a json boolean: " + obj.get(memberName));
    }

    public static Set<Map.Entry<String, JsonElement>> getMembers(JsonObject obj) {
        if(obj == null) {
            return null;
        }
        return obj.entrySet();
    }


    public static JsonElement parseJson(String json) throws SerializationException {
        try {
            return JsonParser.parseString(json);
        } catch (JsonSyntaxException e) {
            throw new SerializationException("Unable to parse json", e);
        }
    }

    public static List<String> parseJsonStringArray(JsonArray jsonStringArray) throws SerializationException {
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
        writeJsonToFile(toWrite, path, getGson());
    }

    public static void writeJsonToFile(Object toWrite, String path, Gson gson) throws IOException {
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
