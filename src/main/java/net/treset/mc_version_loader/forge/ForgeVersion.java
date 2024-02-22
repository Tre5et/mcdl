package net.treset.mc_version_loader.forge;

import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.GenericJsonParsable;
import net.treset.mc_version_loader.json.JsonUtils;
import net.treset.mc_version_loader.json.SerializationException;
import net.treset.mc_version_loader.minecraft.MinecraftLaunchArguments;
import net.treset.mc_version_loader.minecraft.MinecraftLibrary;

import java.util.List;

public class ForgeVersion extends GenericJsonParsable {
    private String id;
    private String time;
    private String releaseTime;
    private String inheritsFrom;
    private String type;
    private String mainClass;
    private List<MinecraftLibrary> libraries;
    transient private MinecraftLaunchArguments arguments;

    public ForgeVersion(String id, String time, String releaseTime, String inheritsFrom, String type, String mainClass, List<MinecraftLibrary> libraries, MinecraftLaunchArguments arguments) {
        this.id = id;
        this.time = time;
        this.releaseTime = releaseTime;
        this.inheritsFrom = inheritsFrom;
        this.type = type;
        this.mainClass = mainClass;
        this.libraries = libraries;
        this.arguments = arguments;
    }

    public static ForgeVersion fromJson(String json) throws SerializationException {
        ForgeVersion version = fromJson(json, ForgeVersion.class, JsonUtils.getGsonCamelCase());

        JsonObject obj = JsonUtils.getAsJsonObject(JsonUtils.parseJson(json));
        JsonObject arguments = JsonUtils.getAsJsonObject(obj, "arguments");

        version.arguments = MinecraftLaunchArguments.fromJson(arguments);

        return version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getInheritsFrom() {
        return inheritsFrom;
    }

    public void setInheritsFrom(String inheritsFrom) {
        this.inheritsFrom = inheritsFrom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    public List<MinecraftLibrary> getLibraries() {
        return libraries;
    }

    public void setLibraries(List<MinecraftLibrary> libraries) {
        this.libraries = libraries;
    }

    public MinecraftLaunchArguments getArguments() {
        return arguments;
    }

    public void setArguments(MinecraftLaunchArguments arguments) {
        this.arguments = arguments;
    }
}
