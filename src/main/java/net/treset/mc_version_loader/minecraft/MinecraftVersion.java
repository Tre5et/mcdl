package net.treset.mc_version_loader.minecraft;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MinecraftVersion {
    private int complianceLevel;
    private String id;
    private String releaseTime;
    private String cha1;
    private String time;
    private String type;
    private String url;

    public MinecraftVersion(int complianceLevel, String id, String releaseTime, String cha1, String time, String type, String versionManifestUrl) {
        this.complianceLevel = complianceLevel;
        this.id = id;
        this.releaseTime = releaseTime;
        this.cha1 = cha1;
        this.time = time;
        this.type = type;
        this.url = versionManifestUrl;
    }

    public MinecraftVersion(String id, String type, String url) {
        this(-1, id, null, null, null, type, url);
    }

    public static MinecraftVersion fromJson(JsonObject versionObj) {
        return new MinecraftVersion(
                JsonUtils.getAsInt(versionObj, "complianceLevel"),
                JsonUtils.getAsString(versionObj, "id"),
                JsonUtils.getAsString(versionObj, "releaseTime"),
                JsonUtils.getAsString(versionObj, "sha1"),
                JsonUtils.getAsString(versionObj, "time"),
                JsonUtils.getAsString(versionObj, "type"),
                JsonUtils.getAsString(versionObj, "url")
        );
    }

    public static List<MinecraftVersion> parseVersionManifest(String manifestJson) {
        List<MinecraftVersion> out = new ArrayList<>();

        JsonElement manifest = JsonUtils.parseJson(manifestJson);
        JsonObject manifestObj = JsonUtils.getAsJsonObject(manifest);
        JsonArray versionArray = JsonUtils.getAsJsonArray(manifestObj, "versions");
        if(versionArray != null) {
            for (JsonElement v : versionArray) {
                out.add(fromJson(JsonUtils.getAsJsonObject(v)));
            }
        }

        return out;
    }

    public boolean isRelease() {
        return Objects.equals(getType(), "release");
    }

    public int getComplianceLevel() {
        return complianceLevel;
    }

    public void setComplianceLevel(int complianceLevel) {
        this.complianceLevel = complianceLevel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getCha1() {
        return cha1;
    }

    public void setCha1(String cha1) {
        this.cha1 = cha1;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "\nVersion: " + getId() + ", snapshot: "+ !isRelease() + ", url: " + getUrl();
    }
}
