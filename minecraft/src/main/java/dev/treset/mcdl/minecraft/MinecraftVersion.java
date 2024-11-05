package dev.treset.mcdl.minecraft;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.json.GenericJsonParsable;
import dev.treset.mcdl.json.JsonUtils;
import dev.treset.mcdl.json.SerializationException;
import dev.treset.mcdl.util.HttpUtil;

import java.io.IOException;
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

    public static MinecraftVersion fromJson(String json) throws SerializationException {
        return fromJson(json, MinecraftVersion.class, JsonUtils.getGsonCamelCase());
    }

    public static List<MinecraftVersion> fromVersionManifest(String versionManifest) throws SerializationException {
        JsonObject versionObj = JsonUtils.getAsJsonObject(JsonUtils.parseJson(versionManifest));
        try {
            return JsonUtils.getGsonCamelCase().fromJson(JsonUtils.getAsJsonArray(versionObj, "versions"), new TypeToken<>() {});
        } catch(Exception e) {
            throw new SerializationException("Could not parse version manifest: " + versionManifest, e);
        }
    }

    /**
     * Gets a list of all minecraft versions
     * @return a list of all minecraft versions
     * @throws FileDownloadException if there is an error downloading the version manifest
     */
    public static List<MinecraftVersion> getAll() throws FileDownloadException {
        try {
            String content = HttpUtil.getString(MinecraftDL.getVersionManifestUrl(), MinecraftDL.getCaching());
            return MinecraftVersion.fromVersionManifest(content);
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse version manifest", e);
        } catch (IOException e) {
            throw new FileDownloadException("Unable to download version manifest", e);
        }
    }

    /**
     * Gets a specific version by its id
     * @param id The id of the version to get
     * @return The version with the specified id
     * @throws FileDownloadException if there is an error downloading the version manifest
     */
    public static MinecraftVersion get(String id) throws FileDownloadException {
        try {
            List<MinecraftVersion> versions = getAll();
            for (MinecraftVersion version : versions) {
                if (version.getId().equals(id)) {
                    return version;
                }
            }
            throw new FileDownloadException("Version " + id + " not found");
        } catch (FileDownloadException e) {
            throw new FileDownloadException("Unable to get version " + id, e);
        }
    }

    /**
     * Gets details for this version
     * @return The version details
     * @throws FileDownloadException if there is an error downloading the version details
     */
    public MinecraftProfile getDetails() throws FileDownloadException {
        return MinecraftProfile.get(getUrl());
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
        return getId();
    }
}
