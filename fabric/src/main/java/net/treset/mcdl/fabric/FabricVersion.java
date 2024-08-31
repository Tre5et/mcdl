package net.treset.mcdl.fabric;

import com.google.gson.reflect.TypeToken;
import net.treset.mcdl.exception.FileDownloadException;
import net.treset.mcdl.json.GenericJsonParsable;
import net.treset.mcdl.json.JsonUtils;
import net.treset.mcdl.json.SerializationException;
import net.treset.mcdl.util.DownloadStatus;
import net.treset.mcdl.util.HttpUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class FabricVersion extends GenericJsonParsable {
    private static final FabricProperties defaultProperties = new FabricProperties();
    private static final Map<String, List<FabricVersion>> versionCache = new HashMap<>();

    private FabricIntermediaryData intermediary;
    private FabricLauncherMeta launcherMeta;
    private FabricLoaderData loader;

    public FabricVersion(FabricIntermediaryData intermediary, FabricLauncherMeta launcherMeta, FabricLoaderData loader) {
        this.intermediary = intermediary;
        this.launcherMeta = launcherMeta;
        this.loader = loader;
    }

    public static FabricVersion fromJson(String versionJson) throws SerializationException {
        return fromJson(versionJson, FabricVersion.class, JsonUtils.getGsonCamelCase());
    }

    public static List<FabricVersion> fromJsonArray(String jsonArray) throws SerializationException {
        return fromJsonArray(jsonArray, new TypeToken<>(){}, JsonUtils.getGsonCamelCase());
    }

    /**
     * Gets the properties that apply by default.
     * @return The default properties
     */
    public static FabricProperties getDefaultProperties() {
        return defaultProperties;
    }

    /**
     * Gets all fabric loader versions for a specified minecraft version.
     * @param mcVersion The minecraft version to get fabric loader versions for
     * @param properties The properties to use
     * @return A list of fabric loader versions
     * @throws FileDownloadException If there is an error loading or parsing the versions
     */
    public static List<FabricVersion> getAll(String mcVersion, FabricProperties properties) throws FileDownloadException {
        if(properties.isUseVersionCache() && versionCache.containsKey(mcVersion)) {
            return versionCache.get(mcVersion);
        }
        try {
            String url = FabricDL.getFabricIndexUrl(mcVersion);
            String content = HttpUtil.getString(url);
            List<FabricVersion> v = fromJsonArray(content);
            if(properties.isUseVersionCache()) {
                versionCache.put(mcVersion, v);
            }
            return v;
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse fabric version details", e);
        } catch (IOException e) {
            throw new FileDownloadException("Unable to download fabric version details", e);
        }
    }

    /**
     * Gets all fabric loader versions for a specified minecraft version.
     * @param mcVersion The minecraft version to get fabric loader versions for
     * @return A list of fabric loader versions
     * @throws FileDownloadException If there is an error loading or parsing the versions
     */
    public static List<FabricVersion> getAll(String mcVersion) throws FileDownloadException {
        return getAll(mcVersion, defaultProperties);
    }

    /**
     * Gets a fabric version for a specific minecraft version and fabric version.
     * @param mcVersion The minecraft version to get the fabric version for
     * @param fabricVersion The fabric version to get the version for
     * @return The fabric version
     * @throws FileDownloadException If there is an error downloading or parsing the version
     */
    public static FabricVersion get(String mcVersion, String fabricVersion) throws FileDownloadException {
        try {
            String url = FabricDL.getFabricVersionUrl(mcVersion, fabricVersion);
            String content = HttpUtil.getString(url);
            return fromJson(content);
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse fabric version details, version: " + mcVersion + "-" + fabricVersion, e);
        } catch (IOException e) {
            throw new FileDownloadException("Unable to download fabric version details, version: " + mcVersion + "-" + fabricVersion, e);
        }
    }

    /**
     * Downloads the fabric client jar to a specified directory.
     * @param outFile The file to download the fabric client as
     * @throws FileDownloadException If there is an error downloading or writing the loader
     */
    public void downloadClient(File outFile) throws FileDownloadException {
        getLoader().downloadClient(outFile);
    }

    /**
     * Downloads the client libraries to a specified directory and returns a list of library paths.
     * @param baseDir The directory to download the libraries to
     * @param onStatus A callback to be called when a library is downloaded
     * @return A list of library paths
     * @throws FileDownloadException If there is an error downloading or writing a library
     */
    public List<String> downloadClientLibraries(File baseDir, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        return launcherMeta.downloadClientLibraries(baseDir, onStatus);
    }

    /**
     * Downloads the common libraries to a specified directory and returns a list of library paths.
     * @param baseDir The directory to download the libraries to
     * @param onStatus A callback to be called when a library is downloaded
     * @return A list of library paths
     * @throws FileDownloadException If there is an error downloading or writing a library
     */
    public List<String> downloadCommonLibraries(File baseDir, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        return launcherMeta.downloadCommonLibraries(baseDir, onStatus);
    }

    /**
     * Downloads the server libraries to a specified directory and returns a list of library paths.
     * @param baseDir The directory to download the libraries to
     * @param onStatus A callback to be called when a library is downloaded
     * @return A list of library paths
     * @throws FileDownloadException If there is an error downloading or writing a library
     */
    public List<String> downloadServerLibraries(File baseDir, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        return launcherMeta.downloadServerLibraries(baseDir, onStatus);
    }

    public FabricIntermediaryData getIntermediary() {
        return intermediary;
    }

    public void setIntermediary(FabricIntermediaryData intermediary) {
        this.intermediary = intermediary;
    }

    public FabricLauncherMeta getLauncherMeta() {
        return launcherMeta;
    }

    public void setLauncherMeta(FabricLauncherMeta launcherMeta) {
        this.launcherMeta = launcherMeta;
    }

    public FabricLoaderData getLoader() {
        return loader;
    }

    public void setLoader(FabricLoaderData loader) {
        this.loader = loader;
    }

    @Override
    public String toString() {
        return this.getLoader().getVersion();
    }
}
