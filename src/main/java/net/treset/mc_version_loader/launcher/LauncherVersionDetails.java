package net.treset.mc_version_loader.launcher;

import net.treset.mc_version_loader.json.GenericJsonParsable;

import java.util.List;

public class LauncherVersionDetails extends GenericJsonParsable {
    private String versionNumber;
    private String versionType;
    private String loaderVersion;
    private String assets;
    private String virtualAssets;
    private String natives;
    private String depends;
    private List<LauncherLaunchArgument> gameArguments;
    private List<LauncherLaunchArgument> jvmArguments;
    private String java;
    private List<String> libraries;
    private String mainClass;
    private String mainFile;
    private String versionId;

    public LauncherVersionDetails(String versionNumber, String versionType, String loaderVersion, String assets, String virtualAssets, String natives, String depends, List<LauncherLaunchArgument> gameArguments, List<LauncherLaunchArgument> jvmArguments, String java, List<String> libraries, String mainClass, String mainFile, String versionId) {
        this.versionNumber = versionNumber;
        this.versionType = versionType;
        this.loaderVersion = loaderVersion;
        this.assets = assets;
        this.virtualAssets = virtualAssets;
        this.natives = natives;
        this.depends = depends;
        this.gameArguments = gameArguments;
        this.jvmArguments = jvmArguments;
        this.java = java;
        this.libraries = libraries;
        this.mainClass = mainClass;
        this.mainFile = mainFile;
        this.versionId = versionId;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getVersionType() {
        return versionType;
    }

    public void setVersionType(String versionType) {
        this.versionType = versionType;
    }

    public String getLoaderVersion() {
        return loaderVersion;
    }

    public void setLoaderVersion(String loaderVersion) {
        this.loaderVersion = loaderVersion;
    }

    public String getAssets() {
        return assets;
    }

    public void setAssets(String assets) {
        this.assets = assets;
    }

    public String getVirtualAssets() {
        return virtualAssets;
    }

    public void setVirtualAssets(String virtualAssets) {
        this.virtualAssets = virtualAssets;
    }

    public String getNatives() {
        return natives;
    }

    public void setNatives(String natives) {
        this.natives = natives;
    }

    public String getDepends() {
        return depends;
    }

    public void setDepends(String depends) {
        this.depends = depends;
    }

    public List<LauncherLaunchArgument> getGameArguments() {
        return gameArguments;
    }

    public void setGameArguments(List<LauncherLaunchArgument> gameArguments) {
        this.gameArguments = gameArguments;
    }

    public List<LauncherLaunchArgument> getJvmArguments() {
        return jvmArguments;
    }

    public void setJvmArguments(List<LauncherLaunchArgument> jvmArguments) {
        this.jvmArguments = jvmArguments;
    }

    public String getJava() {
        return java;
    }

    public void setJava(String java) {
        this.java = java;
    }

    public List<String> getLibraries() {
        return libraries;
    }

    public void setLibraries(List<String> libraries) {
        this.libraries = libraries;
    }

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    public String getMainFile() {
        return mainFile;
    }

    public void setMainFile(String mainFile) {
        this.mainFile = mainFile;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }
}
