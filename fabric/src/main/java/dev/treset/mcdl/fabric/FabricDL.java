package dev.treset.mcdl.fabric;

import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.util.DownloadStatus;
import dev.treset.mcdl.util.cache.Caching;

import java.io.File;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FabricDL {
    private static final String FABRIC_MAVEN_URL = "https://maven.fabricmc.net/";
    private static final String FABRIC_INDEX_URL = "https://meta.fabricmc.net/v2/versions/loader/%s"; // MC-Version
    private static final String FABRIC_VERSION_URL = "https://meta.fabricmc.net/v2/versions/loader/%s/%s"; // MC-Version, Fabric Version
    private static final String FABRIC_PROFILE_URL = "https://meta.fabricmc.net/v2/versions/loader/%s/%s/profile/json"; // MC-Versions, Fabric Version

    /**
     * Gets all fabric loader versions for a specified minecraft version.
     * @param mcVersion The minecraft version to get fabric loader versions for
     * @param properties The properties to use
     * @return A list of fabric loader versions
     * @throws FileDownloadException If there is an error loading or parsing the versions
     */
    public static List<FabricVersion> getFabricVersions(String mcVersion, FabricProperties properties) throws FileDownloadException {
        return FabricVersion.getAll(mcVersion, properties);
    }

    /**
     * Gets all fabric loader versions for a specified minecraft version.
     * @param mcVersion The minecraft version to get fabric loader versions for
     * @return A list of fabric loader versions
     * @throws FileDownloadException If there is an error loading or parsing the versions
     */
    public static List<FabricVersion> getFabricVersions(String mcVersion) throws FileDownloadException {
        return FabricVersion.getAll(mcVersion);
    }

    /**
     * Gets the fabric version details for a specified minecraft version and fabric version.
     * @param mcVersion The minecraft version to get fabric version details for
     * @param fabricVersion The fabric version to get details for
     * @return The fabric version details
     * @throws FileDownloadException If there is an error loading or parsing the version details
     */
    public static FabricVersion getFabricVersion(String mcVersion, String fabricVersion) throws FileDownloadException {
        return FabricVersion.get(mcVersion, fabricVersion);
    }

    /**
     * Gets the fabric profile for a specified minecraft version and fabric version.
     * @param mcVersion The minecraft version to get the fabric profile for
     * @param fabricVersion The fabric version to get the profile for
     * @return The fabric profile
     * @throws FileDownloadException If there is an error loading or parsing the profile
     */
    public static FabricProfile getFabricProfile(String mcVersion, String fabricVersion) throws FileDownloadException {
        return FabricProfile.get(mcVersion, fabricVersion);
    }

    /**
     * Downloads the fabric client jar to a specified directory.
     * @param outFile The file to download the fabric client as
     * @param loader The loader to download
     * @throws FileDownloadException If there is an error downloading or writing the loader
     */
    public static void downloadFabricClient(FabricLoaderData loader, File outFile) throws FileDownloadException {
        loader.downloadClient(outFile);
    }

    /**
     * Downloads a list of fabric libraries to a specified directory and returns a list of library paths.
     * @param libraries The libraries to download
     * @param baseDir The directory to download the libraries to
     * @return A list of library paths
     * @throws FileDownloadException If there is an error downloading or writing a library
     */
    public static List<String> downloadFabricLibraries(List<FabricLibrary> libraries, File baseDir) throws FileDownloadException {
        return FabricLibrary.downloadAll(libraries, baseDir, status -> {});
    }

    /**
     * Downloads a list of fabric libraries to a specified directory and returns a list of library paths.
     * @param libraries The libraries to download
     * @param baseDir The directory to download the libraries to
     * @param onStatus A callback to be called when a library is downloaded
     * @return A list of library paths
     * @throws FileDownloadException If there is an error downloading or writing a library
     */
    public static List<String> downloadFabricLibraries(List<FabricLibrary> libraries, File baseDir, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        return FabricLibrary.downloadAll(libraries, baseDir, onStatus);
    }

    /**
     * Downloads a fabric library to a specified directory and adds it to a list of library paths.
     * @param baseDir The directory to download the library to
     * @param library The library to download
     * @param libraryPaths The list of library paths to add the library to
     * @throws FileDownloadException If there is an error downloading or writing the library
     */
    public static void downloadLibrary(File baseDir, FabricLibrary library, ArrayList<String> libraryPaths) throws FileDownloadException {
        library.download(baseDir, libraryPaths);
    }

    /**
     * Downloads a fabric library to a specified directory and returns a list of library paths.
     * @param baseDir The directory to download the library to
     * @param library The library to download
     * @throws FileDownloadException If there is an error downloading or writing the library
     */
    public static List<String> downloadLibrary(File baseDir, FabricLibrary library) throws FileDownloadException {
        ArrayList<String> libraryPaths = new ArrayList<>();
        library.download(baseDir, libraryPaths);
        return libraryPaths;
    }

    /**
     * Gets the properties that apply by default.
     * @return The default properties
     */
    public static FabricProperties getDefaultProperties() {
        return FabricVersion.getDefaultProperties();
    }

    private static Caching<HttpResponse<byte[]>> caching = null;

    /**
     * Sets a caching strategy this module
     * @param caching The caching strategy to use
     */
    public static void setCaching(Caching<HttpResponse<byte[]>> caching) {
        FabricDL.caching = caching;
    }

    public static Caching<HttpResponse<byte[]>> getCaching() {
        return caching;
    }

    public static String getFabricMavenUrl() {
        return FABRIC_MAVEN_URL;
    }

    public static String getFabricIndexUrl(String mcVersion) {
        if(mcVersion == null || mcVersion.isBlank()) {
            throw new IllegalArgumentException("Invalid mcVersion=" + mcVersion);
        }
        return String.format(FABRIC_INDEX_URL, mcVersion);
    }

    public static String getFabricVersionUrl(String mcVersion, String fabricVersion) {
        if(mcVersion == null || fabricVersion == null) {
            throw new IllegalArgumentException("Invalid arguments: mcVersion=" + mcVersion + ", fabricVersion=" + fabricVersion);
        }
        return String.format(FABRIC_VERSION_URL, mcVersion, fabricVersion);
    }

    public static String getFabricProfileUrl(String mcVersion, String fabricVersion) {
        if(mcVersion == null || fabricVersion == null) {
            throw new IllegalArgumentException("Invalid arguments: mcVersion=" + mcVersion + ", fabricVersion=" + fabricVersion);
        }
        return String.format(FABRIC_PROFILE_URL, mcVersion, fabricVersion);
    }
}
