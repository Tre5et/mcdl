package dev.treset.mcdl.minecraft;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.json.GenericJsonParsable;
import dev.treset.mcdl.json.JsonUtils;
import dev.treset.mcdl.json.SerializationException;
import dev.treset.mcdl.util.DownloadStatus;
import dev.treset.mcdl.util.HttpUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class MinecraftProfile {
    private String id;
    private String assets;
    private int complianceLevel;
    private String mainClass;
    private int minimumLauncherVersion;
    private String releaseTime;
    private String time;
    private String type;
    private MinecraftLaunchArguments launchArguments;
    private MinecraftJavaVersion javaVersion;
    private MinecraftAssetIndex assetIndex;
    private MinecraftFileDownloads downloads;
    private List<MinecraftLibrary> libraries;
    private MinecraftLogging logging;
    private String minecraftArguments;

    public MinecraftProfile(String id, String assets, int complianceLevel, String mainClass, int minimumLauncherVersion, String releaseTime, String time, String type, MinecraftLaunchArguments command, MinecraftJavaVersion javaVersion, MinecraftAssetIndex assetIndex, MinecraftFileDownloads downloads, List<MinecraftLibrary> libraries, MinecraftLogging logging, String minecraftArguments) {
        this.id = id;
        this.assets = assets;
        this.complianceLevel = complianceLevel;
        this.mainClass = mainClass;
        this.minimumLauncherVersion = minimumLauncherVersion;
        this.releaseTime = releaseTime;
        this.time = time;
        this.type = type;
        this.launchArguments = command;
        this.javaVersion = javaVersion;
        this.assetIndex = assetIndex;
        this.downloads = downloads;
        this.libraries = libraries;
        this.logging = logging;
        this.minecraftArguments = minecraftArguments;
    }

    public static MinecraftProfile fromJson(String jsonData) throws SerializationException {
        MinecraftProfile details = GenericJsonParsable.fromJson(jsonData, MinecraftProfile.class, JsonUtils.getGsonCamelCase());
        modifyFromJson(details, jsonData);
        return details;
    }

    protected static void modifyFromJson(MinecraftProfile profile, String json) throws SerializationException {
        JsonObject versionObj = JsonUtils.getAsJsonObject(JsonUtils.parseJson(json));
        profile.setLaunchArguments(MinecraftLaunchArguments.fromJson(JsonUtils.getAsJsonObject(versionObj, "arguments")));
        ArrayList<MinecraftLibrary> libs = new ArrayList<>(profile.getLibraries());
        JsonArray libArr = JsonUtils.getAsJsonArray(versionObj, "libraries");
        if(libArr != null) {
            int startingIndex = 0;
            for (JsonElement e : libArr) {
                JsonObject classifiersObj = JsonUtils.getAsJsonObject(JsonUtils.getAsJsonObject(JsonUtils.getAsJsonObject(e), "downloads"), "classifiers");
                if (classifiersObj != null) {
                    String name = JsonUtils.getAsString(JsonUtils.getAsJsonObject(e), "name");
                    for (; startingIndex < libs.size(); startingIndex++) {
                        if (Objects.equals(libs.get(startingIndex).getName(), name) && libs.get(startingIndex).getNatives() != null) {
                            libs.get(startingIndex).getDownloads().setClassifiers(MinecraftLibrary.Downloads.Classifiers.from(classifiersObj));
                            break;
                        }
                    }
                }
            }
        }
        profile.setLibraries(libs);
    }

    /**
     * Gets details for a specific minecraft version by an url
     * @param url The url of the version details. Usually found in {@link MinecraftVersion#getUrl()}
     * @return The version details
     * @throws FileDownloadException if there is an error downloading the version manifest
     */
    public static MinecraftProfile get(String url) throws FileDownloadException {
        try {
            return MinecraftProfile.fromJson(HttpUtil.getString(url, MinecraftDL.getCaching()));
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse version manifest", e);
        } catch (IOException e) {
            throw new FileDownloadException("Unable to download version manifest", e);
        }
    }

    /**
     * Gets the profile for a specific minecraft version by its id
     * @param version The version to get details for
     * @return The version details
     * @throws FileDownloadException if there is an error downloading the version
     */
    public static MinecraftProfile getForVersion(String version) throws FileDownloadException {
        return get(MinecraftVersion.get(version).getUrl());
    }

    public List<MinecraftLibrary> getActiveLibraries(List<String> activeFeatures) {
        List<MinecraftLibrary> activeLibraries = new ArrayList<>();
        for(MinecraftLibrary l : getLibraries()) {
            if(l.isApplicable(activeFeatures)) {
                activeLibraries.add(l);
            }
        }
        return activeLibraries;
    }

    public boolean isRelease() {
        return Objects.equals(getType(), "release");
    }

    /**
     * Downloads the client jar for this version
     * @param targetFile The file to download the client to
     * @throws FileDownloadException If there is an error downloading the client
     */
    public void downloadClient(File targetFile) throws FileDownloadException {
        downloads.getClient().download(targetFile);
    }

    /**
     * Downloads all libraries for this version
     * @param librariesDir The directory to download the libraries to
     * @param features The features to check for
     * @param nativesDir The directory to download the natives
     * @param statusCallback The callback to report download status
     * @throws FileDownloadException If there is an error downloading the libraries
     */
    public void downloadLibraries(File librariesDir, List<String> features, File nativesDir, Consumer<DownloadStatus> statusCallback) throws FileDownloadException {
        downloadLibraries(librariesDir, null, features, nativesDir, statusCallback);
    }

    /**
     * Downloads all libraries for this version
     * @param librariesDir The directory to download the libraries to
     * @param localLibraryDir The directory to check for local libraries, null if none
     * @param features The features to check for
     * @param nativesDir The directory to download the natives
     * @param onStatus The callback to report download status
     * @throws FileDownloadException If there is an error downloading the libraries
     */
    public void downloadLibraries(File librariesDir, File localLibraryDir, List<String> features, File nativesDir, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        MinecraftLibrary.downloadAll(getLibraries(), librariesDir, localLibraryDir, features, nativesDir, onStatus);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAssets() {
        return assets;
    }

    public void setAssets(String assets) {
        this.assets = assets;
    }

    public int getComplianceLevel() {
        return complianceLevel;
    }

    public void setComplianceLevel(int complianceLevel) {
        this.complianceLevel = complianceLevel;
    }

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    public int getMinimumLauncherVersion() {
        return minimumLauncherVersion;
    }

    public void setMinimumLauncherVersion(int minimumLauncherVersion) {
        this.minimumLauncherVersion = minimumLauncherVersion;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
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

    public MinecraftLaunchArguments getLaunchArguments() {
        return getLaunchArguments(true);
    }

    public MinecraftLaunchArguments getLaunchArguments(boolean mergeLegacyMinecraftArguments) {
        if(mergeLegacyMinecraftArguments && minecraftArguments != null) {
            String[] legacyArgs = minecraftArguments.split(" ");
            List<MinecraftLaunchArgument> gameArgs = new ArrayList<>(launchArguments.getGame());
            gameArgs.addAll(
                Arrays.stream(legacyArgs).map(a -> new MinecraftLaunchArgument(a, null)).toList()
            );
            return new MinecraftLaunchArguments(gameArgs, launchArguments.getJvm());
        }
        return launchArguments;
    }

    public void setLaunchArguments(MinecraftLaunchArguments launchArguments) {
        this.launchArguments = launchArguments;
    }

    public MinecraftJavaVersion getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(MinecraftJavaVersion javaVersion) {
        this.javaVersion = javaVersion;
    }

    public MinecraftAssetIndex getAssetIndex() {
        return assetIndex;
    }

    public void setAssetIndex(MinecraftAssetIndex assetIndex) {
        this.assetIndex = assetIndex;
    }

    public MinecraftFileDownloads getDownloads() {
        return downloads;
    }

    public void setDownloads(MinecraftFileDownloads downloads) {
        this.downloads = downloads;
    }

    public List<MinecraftLibrary> getLibraries() {
        return libraries;
    }

    public void setLibraries(List<MinecraftLibrary> libraries) {
        this.libraries = libraries;
    }

    public MinecraftLogging getLogging() {
        return logging;
    }

    public void setLogging(MinecraftLogging logging) {
        this.logging = logging;
    }

    public String getMinecraftArguments() {
        return minecraftArguments;
    }

    public void setMinecraftArguments(String minecraftArguments) {
        this.minecraftArguments = minecraftArguments;
    }

    @Override
    public String toString() {
        return getId();
    }
}
