package net.treset.mc_version_loader.launcher;

import java.util.List;

public class VersionDetails {
    String assets;
    String depends;
    List<LauncherLaunchArgument> gameArguments;
    List<LauncherLaunchArgument> jvmArguments;
    String java;
    List<String> libraries;
    String mainClass;
    String mainFile;

    public VersionDetails(String assets, String depends, List<LauncherLaunchArgument> gameArguments, List<LauncherLaunchArgument> jvmArguments, String java, List<String> libraries, String mainClass, String mainFile) {
        this.assets = assets;
        this.depends = depends;
        this.gameArguments = gameArguments;
        this.jvmArguments = jvmArguments;
        this.java = java;
        this.libraries = libraries;
        this.mainClass = mainClass;
        this.mainFile = mainFile;
    }

    public String getAssets() {
        return assets;
    }

    public void setAssets(String assets) {
        this.assets = assets;
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
}
