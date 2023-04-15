package net.treset;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JsonParser {
    public static List<Version> parseVersionManifest(String manifestJson) {
        List<Version> out = new ArrayList<>();

        JsonElement manifest = parseJson(manifestJson);
        if(manifest.isJsonObject()) {
            if(manifest.getAsJsonObject().get("versions").isJsonArray()) {
                JsonArray versionsArray = manifest.getAsJsonObject().getAsJsonArray("versions");
                for(JsonElement e : versionsArray) {
                    if(e.isJsonObject()) {
                        JsonObject versionObject = e.getAsJsonObject();
                        String name = "";
                        String manifestUrl = "";
                        boolean snapshot = true;

                        if(versionObject.get("id").isJsonPrimitive() && versionObject.getAsJsonPrimitive("id").isString()) {
                            name = versionObject.getAsJsonPrimitive("id").getAsString();
                        }
                        if(versionObject.get("url").isJsonPrimitive() && versionObject.getAsJsonPrimitive("url").isString()) {
                            manifestUrl = versionObject.getAsJsonPrimitive("url").getAsString();
                        }
                        if(versionObject.get("type").isJsonPrimitive() && versionObject.getAsJsonPrimitive("type").isString()) {
                            snapshot = !versionObject.getAsJsonPrimitive("type").getAsString().equals("release");
                        }

                        out.add(new Version(name, manifestUrl, snapshot));
                    }
                }
            }
        }
        return out;
    }

    public static JsonElement parseJson(String json) {
        return com.google.gson.JsonParser.parseString(json);
    }
}
