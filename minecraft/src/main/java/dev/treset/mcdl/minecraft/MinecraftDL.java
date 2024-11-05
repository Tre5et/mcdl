package dev.treset.mcdl.minecraft;

import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.util.DownloadStatus;
import dev.treset.mcdl.util.cache.Caching;

import java.io.File;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.function.Consumer;

public class MinecraftDL {
    private static final String VERSION_MANIFEST_URL = "https://piston-meta.mojang.com/mc/game/version_manifest_v2.json";

    /**
     * Downloads a minecraft download.
     * @param download the download to download
     * @param targetFile the file to download to
     * @throws FileDownloadException if there is an error downloading the download
     */
    public static void downloadVersionDownload(MinecraftFileDownloads.Downloads download, File targetFile) throws FileDownloadException {
        download.download(targetFile);
    }

    /**
     * Downloads a list of minecraft libraries.
     * @param libraries the libraries to download
     * @param librariesDir the directory to download the libraries to
     * @param features the features to check for
     * @param nativesDir the directory to download the natives to
     * @param statusCallback the callback to report download status
     * @return a list of the downloaded libraries
     * @throws FileDownloadException if there is an error downloading the libraries
     */
    public static List<String> downloadVersionLibraries(List<MinecraftLibrary> libraries, File librariesDir, List<String> features, File nativesDir, Consumer<DownloadStatus> statusCallback) throws FileDownloadException {
        return downloadVersionLibraries(libraries, librariesDir, null, features, nativesDir, statusCallback);
    }

    /**
     * Downloads a list of minecraft libraries.
     * @param libraries the libraries to download
     * @param librariesDir the directory to download the libraries to
     * @param localLibraryDir the directory to check for local libraries, null if none
     * @param features the features to check for
     * @param nativesDir the directory to download the natives to
     * @param onStatus the callback to report download status
     * @return a list of the downloaded libraries
     * @throws FileDownloadException if there is an error downloading the libraries
     */
    public static List<String> downloadVersionLibraries(List<MinecraftLibrary> libraries, File librariesDir, File localLibraryDir, List<String> features, File nativesDir, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        return MinecraftLibrary.downloadAll(libraries, librariesDir, localLibraryDir, features, nativesDir, onStatus);
    }

    /**
     * Gets a list of all minecraft versions
     * @return a list of all minecraft versions
     * @throws FileDownloadException if there is an error downloading the version manifest
     */
    public static List<MinecraftVersion> getVersions() throws FileDownloadException {
        return MinecraftVersion.getAll();
    }

    /**
     * Gets a specific version by its id
     * @param id The id of the version to get
     * @return The version with the specified id
     * @throws FileDownloadException if there is an error downloading the version manifest
     */
    public static MinecraftVersion getVersion(String id) throws FileDownloadException {
        return MinecraftVersion.get(id);
    }

    /**
     * Gets details for a specific minecraft version.
     * @param url the url of the details manifest
     * @return the minecraft version
     * @throws FileDownloadException if there is an error downloading the version manifest
     */
    public static MinecraftProfile getVersionDetails(String url) throws FileDownloadException {
        return MinecraftProfile.get(url);
    }

    /**
     * Gets details for a specific minecraft version.
     * @param version the version to get details for
     * @return the minecraft version
     * @throws FileDownloadException if there is an error downloading the version
     */
    public static MinecraftProfile getVersionDetailsForVersion(String version) throws FileDownloadException {
        return MinecraftProfile.getForVersion(version);
    }

    private static Caching<HttpResponse<byte[]>> caching = null;

    /**
     * Sets a caching strategy for this module
     * @param caching The caching strategy to use
     */
    public static void setCaching(Caching<HttpResponse<byte[]>> caching) {
        MinecraftDL.caching = caching;
    }

    public static Caching<HttpResponse<byte[]>> getCaching() {
        return caching;
    }

    public static String getVersionManifestUrl() {
        return VERSION_MANIFEST_URL;
    }
 }
