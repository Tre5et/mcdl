package net.treset.mc_version_loader.version;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VersionDetails {
    private String id;
    private String assets;
    private int complianceLevel;
    private String mainClass;
    private int minimumLauncherVersion;
    private String releaseTime;
    private String time;
    private String type;
    private VersionArguments arguments;
    private VersionJavaVersion javaVersion;
    private VersionAssetIndex assetIndex;
    private VersionDownloads downloads;
    private List<VersionLibrary> libraries;
    private VersionLogging logging;

    public VersionDetails(String id, String assets, int complianceLevel, String mainClass, int minimumLauncherVersion, String releaseTime, String time, String type, VersionArguments command, VersionJavaVersion javaVersion, VersionAssetIndex assetIndex, VersionDownloads downloads, List<VersionLibrary> libraries, VersionLogging logging) {
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

    public List<VersionLibrary> getActiveLibraries(List<VersionFeature> activeFeatures) {
        List<VersionLibrary> activeLibraries = new ArrayList<>();
        for(VersionLibrary l : getLibraries()) {
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

    public VersionArguments getArguments() {
        return arguments;
    }

    public void setArguments(VersionArguments arguments) {
        this.arguments = arguments;
    }

    public VersionJavaVersion getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(VersionJavaVersion javaVersion) {
        this.javaVersion = javaVersion;
    }

    public VersionAssetIndex getAssetIndex() {
        return assetIndex;
    }

    public void setAssetIndex(VersionAssetIndex assetIndex) {
        this.assetIndex = assetIndex;
    }

    public VersionDownloads getDownloads() {
        return downloads;
    }

    public void setDownloads(VersionDownloads downloads) {
        this.downloads = downloads;
    }

    public List<VersionLibrary> getLibraries() {
        return libraries;
    }

    public void setLibraries(List<VersionLibrary> libraries) {
        this.libraries = libraries;
    }

    public VersionLogging getLogging() {
        return logging;
    }

    public void setLogging(VersionLogging logging) {
        this.logging = logging;
    }
}
