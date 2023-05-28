package net.treset.mc_version_loader.json;

import com.google.gson.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonUtils {
    private static Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();
    private static Gson gsonCamelCase = new GsonBuilder().setPrettyPrinting().create();

    private static final Logger LOGGER = Logger.getLogger(JsonUtils.class.toString());

    public static JsonObject getAsJsonObject(JsonElement element) {
        if(element != null && element.isJsonObject()) {
            return element.getAsJsonObject();
        }
        LOGGER.log(Level.WARNING, "Unable to convert to json object " + element);
        return null;
    }

    public static JsonArray getAsJsonArray(JsonElement element) {
        if(element != null && element.isJsonArray()) {
            return element.getAsJsonArray();
        }
        LOGGER.log(Level.WARNING, "Unable to convert to json array " + element);
        return null;
    }

    public static String getAsString(JsonElement element) {
        if(element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            return element.getAsJsonPrimitive().getAsString();
        }
        LOGGER.log(Level.WARNING, "Unable to convert to json string " + element);
        return null;
    }

    public static int getAsInt(JsonElement element) {
        if(element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            return element.getAsJsonPrimitive().getAsInt();
        }
        LOGGER.log(Level.WARNING, "Unable to convert to json string " + element);
        return -1;
    }

    public static boolean getAsBoolean(JsonElement element) {
        if(element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isBoolean()) {
            return element.getAsJsonPrimitive().getAsBoolean();
        }
        LOGGER.log(Level.WARNING, "Unable to convert to json string " + element);
        return false;
    }

    public static JsonObject getAsJsonObject(JsonObject obj, String memberName) {
        if(obj != null && obj.get(memberName) != null && obj.get(memberName).isJsonObject()) {
            return obj.getAsJsonObject(memberName);
        }
        LOGGER.log(Level.WARNING, "Unable to read requested json object " + memberName);
        return null;
    }

    public static JsonArray getAsJsonArray(JsonObject obj, String memberName) {
        if(obj != null && obj.get(memberName) != null && obj.get(memberName).isJsonArray()) {
            return obj.getAsJsonArray(memberName);
        }
        LOGGER.log(Level.WARNING, "Unable to read requested json array " + memberName);
        return null;
    }

    public static String getAsString(JsonObject obj, String memberName) {
        if(obj != null && obj.get(memberName) != null && obj.get(memberName).isJsonPrimitive()) {
            if (obj.getAsJsonPrimitive(memberName).isString()) {
                return obj.getAsJsonPrimitive(memberName).getAsString();
            }
        }
        LOGGER.log(Level.WARNING, "Unable to read requested string property " + memberName);
        return null;
    }

    public static int getAsInt(JsonObject obj, String memberName) {
        if(obj != null && obj.get(memberName) != null && obj.get(memberName).isJsonPrimitive()) {
            if (obj.getAsJsonPrimitive(memberName).isNumber()) {
                return obj.getAsJsonPrimitive(memberName).getAsInt();
            }
        }
        LOGGER.log(Level.WARNING, "Unable to read requested int property " + memberName);
        return -1;
    }

    public static boolean getAsBoolean(JsonObject obj, String memberName) {
        if(obj != null && obj.get(memberName) != null && obj.get(memberName).isJsonPrimitive()) {
            if (obj.getAsJsonPrimitive(memberName).isBoolean()) {
                return obj.getAsJsonPrimitive(memberName).getAsBoolean();
            }
        }
        LOGGER.log(Level.WARNING, "Unable to read requested boolean property " + memberName);
        return false;
    }

    public static Set<Map.Entry<String, JsonElement>> getMembers(JsonObject obj) {
        if(obj == null) {
            return null;
        }
        return obj.entrySet();
    }


    public static JsonElement parseJson(String json) {
        try {
            return com.google.gson.JsonParser.parseString(json);
        } catch(Exception e) {
            LOGGER.log(Level.WARNING, "Unable to parse json", e);
        }
        return null;
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

    public static boolean writeJsonToFile(Object toWrite, String path) {
        File file = new File(path);
        try {
            if(!file.isFile() && ((!file.getParentFile().isDirectory() && !file.getParentFile().mkdirs()) || !file.createNewFile())) {
                LOGGER.log(Level.SEVERE, "Unable to create file " + path);
                return false;
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to create file " + path, e);
            return false;
        }
        try (FileWriter writer = new FileWriter(path)) {
            getGson().toJson(toWrite, writer);
            writer.flush();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to write json", e);
            return false;
        }
        return true;
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
