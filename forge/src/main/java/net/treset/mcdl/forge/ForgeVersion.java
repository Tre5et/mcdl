package net.treset.mcdl.forge;

import com.google.gson.JsonObject;
import net.treset.mcdl.exception.FileDownloadException;
import net.treset.mcdl.json.GenericJsonParsable;
import net.treset.mcdl.json.JsonUtils;
import net.treset.mcdl.json.SerializationException;
import net.treset.mcdl.minecraft.MinecraftLaunchArguments;
import net.treset.mcdl.minecraft.MinecraftLibrary;
import net.treset.mcdl.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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

    /**
     * Gets a list of all forge version ids for a specific minecraft version
     * @param mcVersion The minecraft version
     * @return A list of forge version ids
     * @throws FileDownloadException If the versions could not be downloaded
     */
    public static List<String> getAll(String mcVersion) throws FileDownloadException {
        List<ForgeMetaVersion> metaVersions = ForgeDL.getForgeVersions();
        ForgeMetaVersion metaVersion = metaVersions.stream().filter(v -> v.getName().equals(mcVersion)).findFirst().orElseThrow(() -> new FileDownloadException("Could not find forge version for " + mcVersion));
        return metaVersion.getVersions();
    }

    /**
     * Gets the version details for a forge version. This is a fairly slow operation as it requires downloading the installer jar and extracting the install profile json.
     * @param versionId The forge version id
     * @return The forge version details
     * @throws FileDownloadException If the version could not be downloaded
     */
    public static ForgeVersion get(String versionId) throws FileDownloadException {
        File jarFile = tempDownloadForgeInstaller(versionId);

        ForgeVersion version;
        try {
            version = ForgeVersion.fromJson(new String(FileUtil.getZipEntry(jarFile, "version.json"), StandardCharsets.UTF_8));
        } catch (SerializationException e) {
            throw new FileDownloadException("Failed to parse forge version", e);
        } catch (IOException e) {
            throw new FileDownloadException("Failed to read versions.json", e);
        }

        return version;
    }

    private static File tempDownloadForgeInstaller(String versionId) throws FileDownloadException {
        File jarFile = new File(FileUtil.getTempDir(),"forge-" + versionId + "-installer.jar");

        if(!jarFile.exists()) {
            if (!jarFile.getParentFile().exists() && !jarFile.getParentFile().mkdirs()) {
                throw new FileDownloadException("Failed to create directory for forge installer");
            }

            try {
                FileUtil.downloadFile(new URL(ForgeDL.getInstallerUrl(versionId)), jarFile);
            } catch (MalformedURLException e) {
                throw new FileDownloadException("Failed to parse forge installer Url", e);
            }
        }

        jarFile.deleteOnExit();

        return jarFile;
    }

    public ForgeInstallData toInstallData() {
        return new ForgeInstallData(
                id,
                inheritsFrom,
                type,
                mainClass,
                libraries,
                arguments
        );
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
