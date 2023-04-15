package net.treset;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.treset.version.Version;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonParser {
    private static Logger LOGGER = Logger.getLogger(JsonParser.class.toString());

    public static List<Version> parseVersionManifest(String manifestJson) {
        List<Version> out = new ArrayList<>();

        JsonElement manifest = parseJson(manifestJson);
        JsonObject manifestObj = toJsonObject(manifest);
        JsonArray versionArray = resolvePropertyJsonArray(manifestObj, "versions");
        if(versionArray != null) {
            for (JsonElement e : versionArray) {
                JsonObject versionObj = toJsonObject(e);
                out.add(new Version(
                        resolvePropertyInt(versionObj, "complianceLevel"),
                        resolvePropertyString(versionObj, "id"),
                        resolvePropertyString(versionObj, "releaseTime"),
                        resolvePropertyString(versionObj, "sha1"),
                        resolvePropertyString(versionObj, "time"),
                        resolvePropertyString(versionObj, "type"),
                        resolvePropertyString(versionObj, "url")
                ));
            }
        }

        return out;
    }

    public static JsonObject toJsonObject(JsonElement element) {
        if(element != null && element.isJsonObject()) {
            return element.getAsJsonObject();
        }
        LOGGER.log(Level.WARNING, "Unable to convert to json object " + element);
        return null;
    }

    public static JsonArray toJsonArray(JsonElement element) {
        if(element != null && element.isJsonArray()) {
            return element.getAsJsonArray();
        }
        LOGGER.log(Level.WARNING, "Unable to convert to json array " + element);
        return null;
    }

    public static JsonObject resolvePropertyJsonObject(JsonObject obj, String propertyName) {
        if(obj != null && obj.get(propertyName) != null && obj.get(propertyName).isJsonObject()) {
            return obj.getAsJsonObject(propertyName);
        }
        LOGGER.log(Level.WARNING, "Unable to read requested json object " + propertyName);
        return null;
    }

    public static JsonArray resolvePropertyJsonArray(JsonObject obj, String propertyName) {
        if(obj != null && obj.get(propertyName) != null && obj.get(propertyName).isJsonArray()) {
            return obj.getAsJsonArray(propertyName);
        }
        LOGGER.log(Level.WARNING, "Unable to read requested json array " + propertyName);
        return null;
    }

    public static String resolvePropertyString(JsonObject obj, String propertyName) {
        if(obj != null && obj.get(propertyName) != null && obj.get(propertyName).isJsonPrimitive()) {
            if (obj.getAsJsonPrimitive(propertyName).isString()) {
                return obj.getAsJsonPrimitive(propertyName).getAsString();
            }
        }
        LOGGER.log(Level.WARNING, "Unable to read requested string property " + propertyName);
        return "";
    }

    public static int resolvePropertyInt(JsonObject obj, String propertyName) {
        if(obj != null && obj.get(propertyName) != null && obj.get(propertyName).isJsonPrimitive()) {
            if (obj.getAsJsonPrimitive(propertyName).isNumber()) {
                return obj.getAsJsonPrimitive(propertyName).getAsInt();
            }
        }
        LOGGER.log(Level.WARNING, "Unable to read requested int property " + propertyName);
        return -1;
    }

    public static boolean resolvePropertyBool(JsonObject obj, String propertyName) {
        if(obj != null && obj.get(propertyName) != null && obj.get(propertyName).isJsonPrimitive()) {
            if (obj.getAsJsonPrimitive(propertyName).isBoolean()) {
                return obj.getAsJsonPrimitive(propertyName).getAsBoolean();
            }
        }
        LOGGER.log(Level.WARNING, "Unable to read requested boolean property " + propertyName);
        return false;
    }


    public static JsonElement parseJson(String json) {
        try {
            return com.google.gson.JsonParser.parseString(json);
        } catch(Exception e) {
            LOGGER.log(Level.WARNING, "Unable to parse json", e);
        }
        return null;
    }
}
