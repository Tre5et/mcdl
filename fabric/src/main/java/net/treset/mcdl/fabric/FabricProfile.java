package net.treset.mcdl.fabric;

import com.google.gson.JsonObject;
import net.treset.mcdl.exception.FileDownloadException;
import net.treset.mcdl.json.GenericJsonParsable;
import net.treset.mcdl.json.JsonUtils;
import net.treset.mcdl.json.SerializationException;
import net.treset.mcdl.minecraft.MinecraftLaunchArguments;
import net.treset.mcdl.util.FileUtil;

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

    public static FabricProfile fromJson(String json) throws SerializationException {
        FabricProfile result = fromJson(json, FabricProfile.class, JsonUtils.getGsonCamelCase());
        JsonObject argumentsObj = JsonUtils.getAsJsonObject(JsonUtils.getAsJsonObject(JsonUtils.parseJson(json)), "arguments");
        result.setLaunchArguments(MinecraftLaunchArguments.fromJson(argumentsObj));
        return result;
    }

    /**
     * Gets the fabric profile for a specified minecraft version and fabric version.
     * @param mcVersion The minecraft version to get the fabric profile for
     * @param fabricVersion The fabric version to get the profile for
     * @return The fabric profile
     * @throws FileDownloadException If there is an error loading or parsing the profile
     */
    public static FabricProfile get(String mcVersion, String fabricVersion) throws FileDownloadException {
        try {
            String url = FabricMC.getFabricProfileUrl(mcVersion, fabricVersion);
            String content = FileUtil.getStringFromUrl(url);
            return fromJson(content);
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse fabric profile", e);
        }
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
