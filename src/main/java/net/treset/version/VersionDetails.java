package net.treset.version;

import java.util.List;

public class VersionDetails {
    private String id;
    private String assets;
    private int complianceLevel;
    private VersionArguments command;
    private VersionJavaVersion javaVersion;
    private VersionAssetIndex assetIndex;
    private VersionDownloads downloads;
    private List<VersionLibrary> libraries;

    public VersionDetails(String id, String assets, int complianceLevel, VersionArguments command, VersionJavaVersion javaVersion, VersionAssetIndex assetIndex, VersionDownloads downloads, List<VersionLibrary> libraries) {
        this.id = id;
        this.assets = assets;
        this.complianceLevel = complianceLevel;
        this.command = command;
        this.javaVersion = javaVersion;
        this.assetIndex = assetIndex;
        this.downloads = downloads;
        this.libraries = libraries;
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

    public VersionArguments getCommand() {
        return command;
    }

    public void setCommand(VersionArguments command) {
        this.command = command;
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
}
