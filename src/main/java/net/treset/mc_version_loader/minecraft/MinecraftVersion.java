package net.treset.mc_version_loader.minecraft;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import net.treset.mc_version_loader.json.GenericJsonParsable;
import net.treset.mc_version_loader.json.JsonUtils;

import java.util.List;
import java.util.Objects;

public class MinecraftVersion extends GenericJsonParsable {
    private int complianceLevel;
    private String id;
    private String releaseTime;
    private String sha1;
    private String time;
    private String type;
    private String url;

    public MinecraftVersion(int complianceLevel, String id, String releaseTime, String sha1, String time, String type, String versionManifestUrl) {
        this.complianceLevel = complianceLevel;
        this.id = id;
        this.releaseTime = releaseTime;
        this.sha1 = sha1;
        this.time = time;
        this.type = type;
        this.url = versionManifestUrl;
    }

    public MinecraftVersion(String id, String type, String url) {
        this(-1, id, null, null, null, type, url);
    }

    public static MinecraftVersion fromJson(String json) {
        return fromJson(json, MinecraftVersion.class, JsonUtils.getGsonCamelCase());
    }

    public static List<MinecraftVersion> fromVersionManifest(String versionManifest) {
        JsonObject versionObj = JsonUtils.getAsJsonObject(JsonUtils.parseJson(versionManifest));
        return JsonUtils.getGsonCamelCase().fromJson(JsonUtils.getAsJsonArray(versionObj, "versions"), new TypeToken<>(){});
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

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
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
