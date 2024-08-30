package net.treset.mcdl.minecraft;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import net.treset.mcdl.exception.FileDownloadException;
import net.treset.mcdl.json.GenericJsonParsable;
import net.treset.mcdl.json.JsonUtils;
import net.treset.mcdl.json.SerializationException;
import net.treset.mcdl.util.HttpUtil;
import net.treset.mcdl.util.cache.Caching;
import net.treset.mcdl.util.cache.RuntimeCaching;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class MinecraftVersion extends GenericJsonParsable {
    private static Caching<List<MinecraftVersion>> caching = new RuntimeCaching<>();

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

    /**
     * Gets a list of all minecraft versions
     * @return a list of all minecraft versions
     * @throws FileDownloadException if there is an error downloading the version manifest
     */
    public static List<MinecraftVersion> getVersions() throws FileDownloadException {
        List<MinecraftVersion> versions = caching.get("versions");
        if(versions != null) {
            return versions;
        }
        try {
            List<MinecraftVersion> v = MinecraftVersion.fromVersionManifest(HttpUtil.getString(MinecraftDL.getVersionManifestUrl()));
            caching.put("versions", v);
            return v;
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse version manifest", e);
        } catch (IOException e) {
            throw new FileDownloadException("Unable to download version manifest", e);
        }
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
     * Sets a caching strategy for versions (default: {@link RuntimeCaching})
     * @param caching The caching strategy to use
     */
    public static void setCaching(Caching<List<MinecraftVersion>> caching) {
        MinecraftVersion.caching = caching;
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
