package net.treset.mc_version_loader.launcher;

import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;

import java.util.HashMap;
import java.util.Map;

public class LauncherDetails {
    private String activeInstance;
    private String assetsDir;
    private String gamedataDir;
    private String gamedataType;
    private String instanceComponentType;
    private String instancesDir;
    private String instancesType;
    private String javaComponentType;
    private String javasDir;
    private String javasType;
    private String librariesDir;
    private String modsComponentType;
    private String modsType;
    private String optionsComponentType;
    private String optionsDir;
    private String optionsType;
    private String resourcepacksComponentType;
    private String resourcepacksDir;
    private String resourcepacksType;
    private String savesComponentType;
    private String savesType;
    private String versionComponentType;
    private String versionDir;
    private String versionType;

    public LauncherDetails(String activeInstance, String assetsDir, String gamedataDir, String gamedataType, String instanceComponentType, String instancesDir, String instancesType, String javaComponentType, String javasDir, String javasType, String librariesDir, String modsComponentType, String modsType, String optionsComponentType, String optionsDir, String optionsType, String resourcepacksComponentType, String resourcepacksDir, String resourcepacksType, String savesComponentType, String savesType, String versionComponentType, String versionDir, String versionType) {
        this.activeInstance = activeInstance;
        this.assetsDir = assetsDir;
        this.gamedataDir = gamedataDir;
        this.gamedataType = gamedataType;
        this.instanceComponentType = instanceComponentType;
        this.instancesDir = instancesDir;
        this.instancesType = instancesType;
        this.javaComponentType = javaComponentType;
        this.javasDir = javasDir;
        this.javasType = javasType;
        this.librariesDir = librariesDir;
        this.modsComponentType = modsComponentType;
        this.modsType = modsType;
        this.optionsComponentType = optionsComponentType;
        this.optionsDir = optionsDir;
        this.optionsType = optionsType;
        this.resourcepacksComponentType = resourcepacksComponentType;
        this.resourcepacksDir = resourcepacksDir;
        this.resourcepacksType = resourcepacksType;
        this.savesComponentType = savesComponentType;
        this.savesType = savesType;
        this.versionComponentType = versionComponentType;
        this.versionDir = versionDir;
        this.versionType = versionType;
    }

    public static LauncherDetails fromJson(String json) {
        JsonObject launcherDetailsObj = JsonUtils.getAsJsonObject(JsonUtils.parseJson(json));
        return new LauncherDetails(
                JsonUtils.getAsString(launcherDetailsObj, "active_instance"),
                JsonUtils.getAsString(launcherDetailsObj, "assets_dir"),
                JsonUtils.getAsString(launcherDetailsObj, "gamedata_dir"),
                JsonUtils.getAsString(launcherDetailsObj, "gamedata_type"),
                JsonUtils.getAsString(launcherDetailsObj, "instance_component_type"),
                JsonUtils.getAsString(launcherDetailsObj, "instances_dir"),
                JsonUtils.getAsString(launcherDetailsObj, "instances_type"),
                JsonUtils.getAsString(launcherDetailsObj, "java_component_type"),
                JsonUtils.getAsString(launcherDetailsObj, "javas_dir"),
                JsonUtils.getAsString(launcherDetailsObj, "javas_type"),
                JsonUtils.getAsString(launcherDetailsObj, "libraries_dir"),
                JsonUtils.getAsString(launcherDetailsObj, "mods_component_type"),
                JsonUtils.getAsString(launcherDetailsObj, "mods_type"),
                JsonUtils.getAsString(launcherDetailsObj, "options_component_type"),
                JsonUtils.getAsString(launcherDetailsObj, "options_dir"),
                JsonUtils.getAsString(launcherDetailsObj, "options_type"),
                JsonUtils.getAsString(launcherDetailsObj, "resourcepacks_component_type"),
                JsonUtils.getAsString(launcherDetailsObj, "resourcepacks_dir"),
                JsonUtils.getAsString(launcherDetailsObj, "resourcepacks_type"),
                JsonUtils.getAsString(launcherDetailsObj, "saves_component_type"),
                JsonUtils.getAsString(launcherDetailsObj, "saves_type"),
                JsonUtils.getAsString(launcherDetailsObj, "version_component_type"),
                JsonUtils.getAsString(launcherDetailsObj, "versions_dir"),
                JsonUtils.getAsString(launcherDetailsObj, "versions_type")
        );
    }

    public Map<String, LauncherManifestType> getTypeConversion() {
        Map<String, LauncherManifestType> typeConversion = new HashMap<>();
        typeConversion.put("launcher", LauncherManifestType.LAUNCHER);
        typeConversion.put(getGamedataType(), LauncherManifestType.GAME);
        typeConversion.put(getInstanceComponentType(), LauncherManifestType.INSTANCE_COMPONENT);
        typeConversion.put(getInstancesType(), LauncherManifestType.INSTANCES);
        typeConversion.put(getJavaComponentType(), LauncherManifestType.JAVA_COMPONENT);
        typeConversion.put(getJavasType(), LauncherManifestType.JAVAS);
        typeConversion.put(getModsComponentType(), LauncherManifestType.MODS_COMPONENT);
        typeConversion.put(getModsType(), LauncherManifestType.MODS);
        typeConversion.put(getOptionsComponentType(), LauncherManifestType.OPTIONS_COMPONENT);
        typeConversion.put(getOptionsType(), LauncherManifestType.OPTIONS);
        typeConversion.put(getResourcepacksComponentType(), LauncherManifestType.RESOURCEPACKS_COMPONENT);
        typeConversion.put(getResourcepacksType(), LauncherManifestType.RESOURCEPACKS);
        typeConversion.put(getSavesComponentType(), LauncherManifestType.SAVES_COMPONENT);
        typeConversion.put(getSavesType(), LauncherManifestType.SAVES);
        typeConversion.put(getVersionComponentType(), LauncherManifestType.VERSION_COMPONENT);
        typeConversion.put(getVersionType(), LauncherManifestType.VERSIONS);
        return typeConversion;
    }

    public String getActiveInstance() {
        return activeInstance;
    }

    public void setActiveInstance(String activeInstance) {
        this.activeInstance = activeInstance;
    }

    public String getAssetsDir() {
        return assetsDir;
    }

    public void setAssetsDir(String assetsDir) {
        this.assetsDir = assetsDir;
    }

    public String getGamedataDir() {
        return gamedataDir;
    }

    public void setGamedataDir(String gamedataDir) {
        this.gamedataDir = gamedataDir;
    }

    public String getGamedataType() {
        return gamedataType;
    }

    public void setGamedataType(String gamedataType) {
        this.gamedataType = gamedataType;
    }

    public String getInstanceComponentType() {
        return instanceComponentType;
    }

    public void setInstanceComponentType(String instanceComponentType) {
        this.instanceComponentType = instanceComponentType;
    }

    public String getInstancesDir() {
        return instancesDir;
    }

    public void setInstancesDir(String instancesDir) {
        this.instancesDir = instancesDir;
    }

    public String getInstancesType() {
        return instancesType;
    }

    public void setInstancesType(String instancesType) {
        this.instancesType = instancesType;
    }

    public String getJavaComponentType() {
        return javaComponentType;
    }

    public void setJavaComponentType(String javaComponentType) {
        this.javaComponentType = javaComponentType;
    }

    public String getJavasDir() {
        return javasDir;
    }

    public void setJavasDir(String javasDir) {
        this.javasDir = javasDir;
    }

    public String getJavasType() {
        return javasType;
    }

    public void setJavasType(String javasType) {
        this.javasType = javasType;
    }

    public String getLibrariesDir() {
        return librariesDir;
    }

    public void setLibrariesDir(String librariesDir) {
        this.librariesDir = librariesDir;
    }

    public String getModsComponentType() {
        return modsComponentType;
    }

    public void setModsComponentType(String modsComponentType) {
        this.modsComponentType = modsComponentType;
    }

    public String getModsType() {
        return modsType;
    }

    public void setModsType(String modsType) {
        this.modsType = modsType;
    }

    public String getOptionsComponentType() {
        return optionsComponentType;
    }

    public void setOptionsComponentType(String optionsComponentType) {
        this.optionsComponentType = optionsComponentType;
    }

    public String getOptionsDir() {
        return optionsDir;
    }

    public void setOptionsDir(String optionsDir) {
        this.optionsDir = optionsDir;
    }

    public String getOptionsType() {
        return optionsType;
    }

    public void setOptionsType(String optionsType) {
        this.optionsType = optionsType;
    }

    public String getResourcepacksComponentType() {
        return resourcepacksComponentType;
    }

    public void setResourcepacksComponentType(String resourcepacksComponentType) {
        this.resourcepacksComponentType = resourcepacksComponentType;
    }

    public String getResourcepacksDir() {
        return resourcepacksDir;
    }

    public void setResourcepacksDir(String resourcepacksDir) {
        this.resourcepacksDir = resourcepacksDir;
    }

    public String getResourcepacksType() {
        return resourcepacksType;
    }

    public void setResourcepacksType(String resourcepacksType) {
        this.resourcepacksType = resourcepacksType;
    }

    public String getSavesComponentType() {
        return savesComponentType;
    }

    public void setSavesComponentType(String savesComponentType) {
        this.savesComponentType = savesComponentType;
    }

    public String getSavesType() {
        return savesType;
    }

    public void setSavesType(String savesType) {
        this.savesType = savesType;
    }

    public String getVersionComponentType() {
        return versionComponentType;
    }

    public void setVersionComponentType(String versionComponentType) {
        this.versionComponentType = versionComponentType;
    }

    public String getVersionDir() {
        return versionDir;
    }

    public void setVersionDir(String versionDir) {
        this.versionDir = versionDir;
    }

    public String getVersionType() {
        return versionType;
    }

    public void setVersionType(String versionType) {
        this.versionType = versionType;
    }
}
