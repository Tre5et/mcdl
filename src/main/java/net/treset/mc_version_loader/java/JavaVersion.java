package net.treset.mc_version_loader.java;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;
import net.treset.mc_version_loader.os.OsDetails;

import java.util.ArrayList;
import java.util.List;

public class JavaVersion {
    private int availabilityGroup;
    private int availabilityProgress;
    private String manifestSha1;
    private int manifestSize;
    private String manifestUrl;
    private String versionName;
    private String versionReleased;

    public JavaVersion(int availabilityGroup, int availabilityProgress, String manifestSha1, int manifestSize, String manifestUrl, String versionName, String versionReleased) {
        this.availabilityGroup = availabilityGroup;
        this.availabilityProgress = availabilityProgress;
        this.manifestSha1 = manifestSha1;
        this.manifestSize = manifestSize;
        this.manifestUrl = manifestUrl;
        this.versionName = versionName;
        this.versionReleased = versionReleased;
    }

    public static List<JavaVersion> parseJavaVersion(String jsonData, String javaComponent, String osIdentifier) {
        JsonObject dataObj = JsonUtils.getAsJsonObject(JsonUtils.parseJson(jsonData));
        JsonObject osObj = JsonUtils.getAsJsonObject(dataObj, osIdentifier);
        JsonArray javaArray = JsonUtils.getAsJsonArray(osObj, javaComponent);
        List<JavaVersion> out = new ArrayList<>();
        if(javaArray != null) {
            for(JsonElement j : javaArray) {
                fromJson(JsonUtils.getAsJsonObject(j));
            }
        }
        return out;
    }

    public static JavaVersion fromJson(JsonObject javaObj) {
        JsonObject availabilityObj = JsonUtils.getAsJsonObject(javaObj, "availability");
        JsonObject manifestObj = JsonUtils.getAsJsonObject(javaObj, "manifest");
        JsonObject versionObj = JsonUtils.getAsJsonObject(javaObj, "version");
        return new JavaVersion(
                JsonUtils.getAsInt(availabilityObj, "group"),
                JsonUtils.getAsInt(availabilityObj, "progress"),
                JsonUtils.getAsString(manifestObj, "sha1"),
                JsonUtils.getAsInt(manifestObj, "size"),
                JsonUtils.getAsString(manifestObj, "url"),
                JsonUtils.getAsString(versionObj, "name"),
                JsonUtils.getAsString(versionObj, "released")
        );
    }

    public static List<JavaVersion> parseJavaVersion(String jsonData, String javaName) {
        return parseJavaVersion(jsonData, javaName, OsDetails.getJavaIdentifier());
    }

    public int getAvailabilityGroup() {
        return availabilityGroup;
    }

    public void setAvailabilityGroup(int availabilityGroup) {
        this.availabilityGroup = availabilityGroup;
    }

    public int getAvailabilityProgress() {
        return availabilityProgress;
    }

    public void setAvailabilityProgress(int availabilityProgress) {
        this.availabilityProgress = availabilityProgress;
    }

    public String getManifestSha1() {
        return manifestSha1;
    }

    public void setManifestSha1(String manifestSha1) {
        this.manifestSha1 = manifestSha1;
    }

    public int getManifestSize() {
        return manifestSize;
    }

    public void setManifestSize(int manifestSize) {
        this.manifestSize = manifestSize;
    }

    public String getManifestUrl() {
        return manifestUrl;
    }

    public void setManifestUrl(String manifestUrl) {
        this.manifestUrl = manifestUrl;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionReleased() {
        return versionReleased;
    }

    public void setVersionReleased(String versionReleased) {
        this.versionReleased = versionReleased;
    }
}
