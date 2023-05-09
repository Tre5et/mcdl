package net.treset.mc_version_loader.fabric;

import net.treset.mc_version_loader.json.GenericJsonParsable;
import net.treset.mc_version_loader.json.JsonUtils;
import net.treset.mc_version_loader.minecraft.MinecraftLaunchArguments;

import java.util.List;

public class FabricProfile extends GenericJsonParsable {
    private MinecraftLaunchArguments launchArguments;
    private String id;
    private String inheritsFrom;
    private List<FabricLibrary> libraries;
    private String mainClass;
    private String releaseTime;
    private String time;
    private String type;

    public FabricProfile(MinecraftLaunchArguments launchArguments, String id, String inheritsFrom, List<FabricLibrary> libraries, String mainClass, String releaseTime, String time, String type) {
        this.launchArguments = launchArguments;
        this.id = id;
        this.inheritsFrom = inheritsFrom;
        this.libraries = libraries;
        this.mainClass = mainClass;
        this.releaseTime = releaseTime;
        this.time = time;
        this.type = type;
    }

    public static FabricProfile fromJson(String json) {
        return fromJson(json, FabricProfile.class, JsonUtils.getGsonCamelCase());
    }

    public MinecraftLaunchArguments getLaunchArguments() {
        return launchArguments;
    }

    public void setLaunchArguments(MinecraftLaunchArguments launchArguments) {
        this.launchArguments = launchArguments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInheritsFrom() {
        return inheritsFrom;
    }

    public void setInheritsFrom(String inheritsFrom) {
        this.inheritsFrom = inheritsFrom;
    }

    public List<FabricLibrary> getLibraries() {
        return libraries;
    }

    public void setLibraries(List<FabricLibrary> libraries) {
        this.libraries = libraries;
    }

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
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
}
