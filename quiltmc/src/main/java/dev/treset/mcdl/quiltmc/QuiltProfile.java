package dev.treset.mcdl.quiltmc;

import com.google.gson.JsonObject;
import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.json.GenericJsonParsable;
import dev.treset.mcdl.json.JsonUtils;
import dev.treset.mcdl.json.SerializationException;
import dev.treset.mcdl.minecraft.MinecraftLaunchArguments;
import dev.treset.mcdl.util.HttpUtil;

import java.io.IOException;
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

    /**
     * Get the QuiltMC profile for a specific Minecraft version and QuiltMC version.
     * @param mcVersion The Minecraft version
     * @param quiltVersion The QuiltMC version
     * @return The QuiltMC profile
     * @throws FileDownloadException If the profile could not be fetched
     */
    public static QuiltProfile get(String mcVersion, String quiltVersion) throws FileDownloadException {
        try {
            String content = HttpUtil.getString(QuiltDL.getQuiltMetaUrl() + "loader/" + mcVersion + "/" + quiltVersion + "/profile/json");
            return QuiltProfile.fromJson(content);
        } catch (SerializationException e) {
            throw new FileDownloadException("Failed to parse QuiltMC profile JSON", e);
        } catch (IOException e) {
            throw new FileDownloadException("Failed to download QuiltMC profile JSON", e);
        }
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
