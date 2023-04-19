package net.treset.mc_version_loader.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.treset.mc_version_loader.os.OsDetails;
import net.treset.mc_version_loader.java.JavaDownload;
import net.treset.mc_version_loader.java.JavaFile;
import net.treset.mc_version_loader.java.JavaManifest;
import net.treset.mc_version_loader.java.JavaVersion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JavaJsonParser {
    public static List<JavaVersion> parseJavaVersion(String jsonData, String javaComponent, String osIdentifier) {
        JsonObject dataObj = JsonUtils.getAsJsonObject(JsonUtils.parseJson(jsonData));
        JsonObject osObj = JsonUtils.getAsJsonObject(dataObj, osIdentifier);
        JsonArray javaArray = JsonUtils.getAsJsonArray(osObj, javaComponent);
        List<JavaVersion> out = new ArrayList<>();
        if(javaArray != null) {
            for(JsonElement j : javaArray) {
                JsonObject jObj = JsonUtils.getAsJsonObject(j);
                JsonObject availabilityObj = JsonUtils.getAsJsonObject(jObj, "availability");
                JsonObject manifestObj = JsonUtils.getAsJsonObject(jObj, "manifest");
                JsonObject versionObj = JsonUtils.getAsJsonObject(jObj, "version");
                out.add(new JavaVersion(
                        JsonUtils.getAsInt(availabilityObj, "group"),
                        JsonUtils.getAsInt(availabilityObj, "progress"),
                        JsonUtils.getAsString(manifestObj, "sha1"),
                        JsonUtils.getAsInt(manifestObj, "size"),
                        JsonUtils.getAsString(manifestObj, "url"),
                        JsonUtils.getAsString(versionObj, "name"),
                        JsonUtils.getAsString(versionObj, "released")
                ));
            }
        }
        return out;
    }

    public static List<JavaVersion> parseJavaVersion(String jsonData, String javaName) {
        return parseJavaVersion(jsonData, javaName, OsDetails.getJavaIdentifier());
    }

    public static JavaManifest parseJavaManifest(String dataJson) {
        JsonObject dataObj = JsonUtils.getAsJsonObject(JsonUtils.parseJson(dataJson));
        JsonObject filesObj = JsonUtils.getAsJsonObject(dataObj, "files");
        Set<Map.Entry<String, JsonElement>> membersSet = JsonUtils.getMembers(filesObj);
        List<JavaFile> files = new ArrayList<>();
        if(membersSet != null) {
            for(Map.Entry<String, JsonElement> m : membersSet) {
                JsonObject mObj = JsonUtils.getAsJsonObject(m.getValue());
                files.add(parseJavaFile(m.getKey(), mObj));
            }
        }
        return new JavaManifest(files);
    }

    private static JavaFile parseJavaFile(String name, JsonObject fileObj) {
        JsonObject downloadsObj = JsonUtils.getAsJsonObject(fileObj, "downloads");
        JsonObject lzmaObj = JsonUtils.getAsJsonObject(downloadsObj, "lzma");
        JsonObject rawObj = JsonUtils.getAsJsonObject(downloadsObj, "raw");

        return new JavaFile(
                name,
                JsonUtils.getAsBoolean(fileObj, "executable"),
                JsonUtils.getAsString(fileObj, "type"),
                parseJavaDownload(lzmaObj),
                parseJavaDownload(rawObj)
        );
    }

    private static JavaDownload parseJavaDownload(JsonObject downloadsObj) {
        return new JavaDownload(
                JsonUtils.getAsString(downloadsObj, "sha1"),
                JsonUtils.getAsInt(downloadsObj, "size"),
                JsonUtils.getAsString(downloadsObj, "url")
        );
    }
}
