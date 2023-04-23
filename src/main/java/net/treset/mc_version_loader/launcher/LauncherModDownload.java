package net.treset.mc_version_loader.launcher;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class LauncherModDownload {
    private String date;
    private String provider;
    private String id;
    private String version;

    public LauncherModDownload(String date, String provider, String id, String version) {
        this.date = date;
        this.provider = provider;
        this.id = id;
        this.version = version;
    }

    public static LauncherModDownload fromJson(JsonObject downloadObj) {
        return new LauncherModDownload(
                JsonUtils.getAsString(downloadObj, "date"),
                JsonUtils.getAsString(downloadObj, "provider"),
                JsonUtils.getAsString(downloadObj, "id"),
                JsonUtils.getAsString(downloadObj, "version")
        );
    }

    public static List<LauncherModDownload> parseDownloads(JsonArray downloadsArray) {
        if(downloadsArray == null) {
            return null;
        }
        List<LauncherModDownload> out = new ArrayList<>();
        for(JsonElement e : downloadsArray) {
            out.add(fromJson(JsonUtils.getAsJsonObject(e)));
        }
        return out;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
