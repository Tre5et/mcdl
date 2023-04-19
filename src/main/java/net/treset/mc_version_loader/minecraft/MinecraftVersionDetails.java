package net.treset.mc_version_loader.minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MinecraftVersionDetails {
    private String id;
    private String assets;
    private int complianceLevel;
    private String mainClass;
    private int minimumLauncherVersion;
    private String releaseTime;
    private String time;
    private String type;
    private MinecraftLaunchArguments arguments;
    private MinecraftJavaVersion javaVersion;
    private MinecraftAssetIndex assetIndex;
    private MinecraftFileDownloads downloads;
    private List<MinecraftLibrary> libraries;
    private MinecraftLogging logging;

    public MinecraftVersionDetails(String id, String assets, int complianceLevel, String mainClass, int minimumLauncherVersion, String releaseTime, String time, String type, MinecraftLaunchArguments command, MinecraftJavaVersion javaVersion, MinecraftAssetIndex assetIndex, MinecraftFileDownloads downloads, List<MinecraftLibrary> libraries, MinecraftLogging logging) {
        this.id = id;
        this.assets = assets;
        this.complianceLevel = complianceLevel;
        this.mainClass = mainClass;
        this.minimumLauncherVersion = minimumLauncherVersion;
        this.releaseTime = releaseTime;
        this.time = time;
        this.type = type;
        this.arguments = command;
        this.javaVersion = javaVersion;
        this.assetIndex = assetIndex;
        this.downloads = downloads;
        this.libraries = libraries;
        this.logging = logging;
    }

    public List<MinecraftLibrary> getActiveLibraries(List<MinecraftLaunchFeature> activeFeatures) {
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

    public MinecraftLaunchArguments getArguments() {
        return arguments;
    }

    public void setArguments(MinecraftLaunchArguments arguments) {
        this.arguments = arguments;
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
}
