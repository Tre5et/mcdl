package net.treset.mc_version_loader.launcher;

import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.GenericJsonParsable;
import net.treset.mc_version_loader.json.JsonUtils;

import java.util.List;

public class LauncherVersionDetails extends GenericJsonParsable {
    private String assets;
    private String depends;
    private List<LauncherLaunchArgument> gameArguments;
    private List<LauncherLaunchArgument> jvmArguments;
    private String java;
    private List<String> libraries;
    private String mainClass;
    private String mainFile;

    public LauncherVersionDetails(String assets, String depends, List<LauncherLaunchArgument> gameArguments, List<LauncherLaunchArgument> jvmArguments, String java, List<String> libraries, String mainClass, String mainFile) {
        this.assets = assets;
        this.depends = depends;
        this.gameArguments = gameArguments;
        this.jvmArguments = jvmArguments;
        this.java = java;
        this.libraries = libraries;
        this.mainClass = mainClass;
        this.mainFile = mainFile;
    }

    public static LauncherVersionDetails fromJson(String json) {
        return fromJson(json, LauncherVersionDetails.class);
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
