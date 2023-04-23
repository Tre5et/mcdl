package net.treset.mc_version_loader.launcher;

import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;

import java.util.List;

public class LauncherInstanceDetails {
    private List<LauncherFeature> features;
    private List<String> ignoredFiles;
    private List<LauncherLaunchArgument> jvm_arguments;
    private String modsComponent;
    private String optionsComponent;
    private String resourcePacksComponent;
    private String savesComponent;
    private String versionComponent;

    public LauncherInstanceDetails(List<LauncherFeature> features, List<String> ignoredFiles, List<LauncherLaunchArgument> jvm_arguments, String modsComponent, String optionsComponent, String resourcePacksComponent, String savesComponent, String versionComponent) {
        this.features = features;
        this.ignoredFiles = ignoredFiles;
        this.jvm_arguments = jvm_arguments;
        this.modsComponent = modsComponent;
        this.optionsComponent = optionsComponent;
        this.resourcePacksComponent = resourcePacksComponent;
        this.savesComponent = savesComponent;
        this.versionComponent = versionComponent;
    }

    public static LauncherInstanceDetails fromJson(String json) {
        JsonObject instanceObj = JsonUtils.getAsJsonObject(JsonUtils.parseJson(json));
        return new LauncherInstanceDetails(
                LauncherFeature.parseFeatures(JsonUtils.getAsJsonObject(instanceObj, "features")),
                JsonUtils.parseJsonStringArray(JsonUtils.getAsJsonArray(instanceObj, "ignored_files")),
                LauncherLaunchArgument.parseArguments(JsonUtils.getAsJsonArray(instanceObj, "jvm_arguments")),
                JsonUtils.getAsString(instanceObj, "mods_component"),
                JsonUtils.getAsString(instanceObj, "options_component"),
                JsonUtils.getAsString(instanceObj, "resourcepacks_component"),
                JsonUtils.getAsString(instanceObj, "saves_component"),
                JsonUtils.getAsString(instanceObj, "version_component")
        );
    }

    public List<LauncherFeature> getFeatures() {
        return features;
    }

    public void setFeatures(List<LauncherFeature> features) {
        this.features = features;
    }

    public List<String> getIgnoredFiles() {
        return ignoredFiles;
    }

    public void setIgnoredFiles(List<String> ignoredFiles) {
        this.ignoredFiles = ignoredFiles;
    }

    public List<LauncherLaunchArgument> getJvm_arguments() {
        return jvm_arguments;
    }

    public void setJvm_arguments(List<LauncherLaunchArgument> jvm_arguments) {
        this.jvm_arguments = jvm_arguments;
    }

    public String getModsComponent() {
        return modsComponent;
    }

    public void setModsComponent(String modsComponent) {
        this.modsComponent = modsComponent;
    }

    public String getOptionsComponent() {
        return optionsComponent;
    }

    public void setOptionsComponent(String optionsComponent) {
        this.optionsComponent = optionsComponent;
    }

    public String getResourcePacksComponent() {
        return resourcePacksComponent;
    }

    public void setResourcePacksComponent(String resourcePacksComponent) {
        this.resourcePacksComponent = resourcePacksComponent;
    }

    public String getSavesComponent() {
        return savesComponent;
    }

    public void setSavesComponent(String savesComponent) {
        this.savesComponent = savesComponent;
    }

    public String getVersionComponent() {
        return versionComponent;
    }

    public void setVersionComponent(String versionComponent) {
        this.versionComponent = versionComponent;
    }
}
