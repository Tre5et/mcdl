package dev.treset.mcdl.json;

import com.google.gson.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonUtils {
    static final Gson GSON_SNAKE_CASE = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();
    static final Gson GSON_CAMEL_CASE = new GsonBuilder().setPrettyPrinting().create();
    private static Gson gson = GSON_SNAKE_CASE;

    /**
     * Converts the element to a json object.
     * @param element The element to convert
     * @return The json object or null if the element is null.
     * @throws SerializationException If the element is not a json object
     */
    public static JsonObject getAsJsonObject(JsonElement element) throws SerializationException {
        if(element == null) {
            return null;
        }
        if(element.isJsonObject()) {
            return element.getAsJsonObject();
        }
        throw new SerializationException("Not a json object: " + element);
    }

    /**
     * Converts the element to a json array.
     * @param element The element to convert
     * @return The json array or null if the element is null
     * @throws SerializationException If the element is not a json array
     */
    public static JsonArray getAsJsonArray(JsonElement element) throws SerializationException {
        if(element == null) {
            return null;
        }
        if(element.isJsonArray()) {
            return element.getAsJsonArray();
        }
        throw new SerializationException("Not a json array: " + element);
    }


    /**
     * Converts the element to a string.
     * @param element The element to convert
     * @return The string or null if the element is null
     * @throws SerializationException If the element is not a json string
     */
    public static String getAsString(JsonElement element) throws SerializationException {
        if(element == null) {
            return null;
        }
        if(element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            return element.getAsJsonPrimitive().getAsString();
        }
        throw new SerializationException("Not a json string: " + element);
    }

    /**
     * Converts the element to an int.
     * @param element The element to convert
     * @return The int or -1 if the element is null
     * @throws SerializationException If the element is not a json int
     */
    public static int getAsInt(JsonElement element) throws SerializationException {
        if(element == null) {
            return -1;
        }
        if(element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            return element.getAsJsonPrimitive().getAsInt();
        }
        throw new SerializationException("Not a json int: " + element);
    }

    /**
     * Converts the element to a boolean.
     * @param element The element to convert
     * @return The string or false if the element is null
     * @throws SerializationException If the element is not a json boolean
     */
    public static boolean getAsBoolean(JsonElement element) throws SerializationException {
        if(element == null) {
            return false;
        }
        if(element.isJsonPrimitive() && element.getAsJsonPrimitive().isBoolean()) {
            return element.getAsJsonPrimitive().getAsBoolean();
        }
        throw new SerializationException("Not a json boolean: " + element);
    }

    /**
     * Converts a member of the json object to a json object.
     * @param obj The json object to get the member from
     * @param memberName The name of the member to be converted
     * @return The json object or null if the member is null
     * @throws SerializationException If the member is not a json object
     */
    public static JsonObject getAsJsonObject(JsonObject obj, String memberName) throws SerializationException {
        if(obj == null || obj.get(memberName) == null) {
            return null;
        }
        if(obj.get(memberName).isJsonObject()) {
            return obj.getAsJsonObject(memberName);
        }
        throw new SerializationException("Not a json object: " + obj.get(memberName));
    }

    /**
     * Converts a member of the json object to a json array.
     * @param obj The json object to get the member from
     * @param memberName The name of the member to be converted
     * @return The json array or null if the member is null
     * @throws SerializationException If the member is not a json array
     */
    public static JsonArray getAsJsonArray(JsonObject obj, String memberName) throws SerializationException {
        if(obj == null || obj.get(memberName) == null) {
            return null;
        }
        if(obj.get(memberName).isJsonArray()) {
            return obj.getAsJsonArray(memberName);
        }
        throw new SerializationException("Not a json array: " + obj.get(memberName));
    }

    /**
     * Converts a member of the json object to a string.
     * @param obj The json object to get the member from
     * @param memberName The name of the member to be converted
     * @return The string or null if the member is null
     * @throws SerializationException If the member is not a json string
     */
    public static String getAsString(JsonObject obj, String memberName) throws SerializationException {
        if(obj == null || obj.get(memberName) == null) {
            return null;
        }
        if(obj.get(memberName).isJsonPrimitive() && obj.getAsJsonPrimitive(memberName).isString()) {
            return obj.getAsJsonPrimitive(memberName).getAsString();
        }
        throw new SerializationException("Not a json string: " + obj.get(memberName));
    }

    /**
     * Converts a member of the json object to an int.
     * @param obj The json object to get the member from
     * @param memberName The name of the member to be converted
     * @return The int or -1 if the member is null
     * @throws SerializationException If the member is not a json int
     */
    public static int getAsInt(JsonObject obj, String memberName) throws SerializationException {
        if(obj == null || obj.get(memberName) == null) {
            return -1;
        }
        if(obj.get(memberName).isJsonPrimitive() && obj.getAsJsonPrimitive(memberName).isNumber()) {
            return obj.getAsJsonPrimitive(memberName).getAsInt();
        }
        throw new SerializationException("Not a json int: " + obj.get(memberName));
    }

    /**
     * Converts a member of the json object to a boolean.
     * @param obj The json object to get the member from
     * @param memberName The name of the member to be converted
     * @return The boolean or false if the member is null
     * @throws SerializationException If the member is not a json boolean
     */
    public static boolean getAsBoolean(JsonObject obj, String memberName) throws SerializationException {
        if(obj == null || obj.get(memberName) == null) {
            return false;
        }
        if(obj.get(memberName).isJsonPrimitive() && obj.getAsJsonPrimitive(memberName).isBoolean()) {
            return obj.getAsJsonPrimitive(memberName).getAsBoolean();
        }
        throw new SerializationException("Not a json boolean: " + obj.get(memberName));
    }

    /**
     * Gets all members from a json object.
     * @param obj The json object to get the members from
     * @return A set of Entries, where the key is the property name and value is the property value as a json element
     */
    public static Set<Map.Entry<String, JsonElement>> getMembers(JsonObject obj) {
        if(obj == null) {
            return null;
        }
        return obj.entrySet();
    }

    /**
     * Converts a string to a json element.
     * @param json The string to convert
     * @return A json element as represented in the string
     * @throws SerializationException If the string is not valid json
     */
    public static JsonElement parseJson(String json) throws SerializationException {
        try {
            return JsonParser.parseString(json);
        } catch (JsonSyntaxException e) {
            throw new SerializationException("Unable to parse json", e);
        }
    }

    /**
     * Converts a json array consisting only of strings to a list of strings.
     * @param jsonStringArray The array to convert
     * @return A list of strings in order of the json array
     * @throws SerializationException If any of the arrays members aren't strings
     */
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

    /**
     * Writes a serialized object to a file.
     * @param toWrite The object to serialize
     * @param path The path to the file
     * @throws IOException If the file can not be created or written to
     */
    public static void writeJsonToFile(Object toWrite, String path) throws IOException {
        writeJsonToFile(toWrite, path, getGson());
    }

    /**
     * Writes a serialized object to a file.
     * @param toWrite The object to serialize
     * @param path The path to the file
     * @param gson The gson instance to use for serialization
     * @throws IOException If the file can not be created or written to
     */
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

    /**
     * Return a gson instance that uses the lower case with underscores naming policy and pretty printing.
     * @return The gson instance
     */
    public static Gson getGson() {
        return gson;
    }

    /**
     * Sets the default Gson implementation. This requires a GsonBuilder, as required properties need to be set.
     * @param builder The Gson builder to derive the default Gson implementation from.
     */
    public static void setDefaultGson(GsonBuilder builder) {
        builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        gson = builder.create();
    }

    /**
     * Returns a gson instance that uses the snake case naming policy and pretty printing.
     * @return The gson instance
     */
    public static Gson getGsonSnakeCase() {
        return GSON_SNAKE_CASE;
    }

    /**
     * Return a gson instance that uses the camel case naming policy and pretty printing.
     * @return The gson instance
     */
    public static Gson getGsonCamelCase() {
        return GSON_CAMEL_CASE;
    }
}
