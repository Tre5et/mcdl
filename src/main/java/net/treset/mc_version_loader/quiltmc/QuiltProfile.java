package net.treset.mc_version_loader.quiltmc;

import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.GenericJsonParsable;
import net.treset.mc_version_loader.json.JsonUtils;
import net.treset.mc_version_loader.json.SerializationException;
import net.treset.mc_version_loader.minecraft.MinecraftLaunchArguments;

import java.util.List;

public class QuiltProfile extends GenericJsonParsable {
    private String id;
    private String inheritsFrom;
    private String type;
    private String mainClass;
    private transient MinecraftLaunchArguments arguments;
    private List<QuiltLibrary> libraries;
    private String releaseTime;
    private String time;

    public QuiltProfile(String id, String inheritsFrom, String type, String mainClass, MinecraftLaunchArguments arguments, List<QuiltLibrary> libraries, String releaseTime, String time) {
        this.id = id;
        this.inheritsFrom = inheritsFrom;
        this.type = type;
        this.mainClass = mainClass;
        this.arguments = arguments;
        this.libraries = libraries;
        this.releaseTime = releaseTime;
        this.time = time;
    }

    public static QuiltProfile fromJson(String json) throws SerializationException {
        QuiltProfile profile = fromJson(json, QuiltProfile.class, JsonUtils.getGsonCamelCase());
        JsonObject obj = JsonUtils.getAsJsonObject(JsonUtils.parseJson(json));
        JsonObject argumentsObj = JsonUtils.getAsJsonObject(obj, "arguments");
        profile.arguments = MinecraftLaunchArguments.fromJson(argumentsObj);
        return profile;
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

    public MinecraftLaunchArguments getArguments() {
        return arguments;
    }

    public void setArguments(MinecraftLaunchArguments arguments) {
        this.arguments = arguments;
    }

    public List<QuiltLibrary> getLibraries() {
        return libraries;
    }

    public void setLibraries(List<QuiltLibrary> libraries) {
        this.libraries = libraries;
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
}
