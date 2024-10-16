package dev.treset.mcdl.quiltmc;

import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.util.DownloadStatus;
import dev.treset.mcdl.util.cache.Caching;

import java.io.File;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class QuiltDL {
    private static final String quiltMetaUrl = "https://meta.quiltmc.org/v3/versions/";
    /**
     * Get all of QuiltMC versions for a specific Minecraft version.
     * @param mcVersion The Minecraft version to get QuiltMC versions for
     * @return A list of QuiltMC versions
     * @throws FileDownloadException If the versions could not be fetched
     */
    public static List<QuiltVersion> getQuiltVersions(String mcVersion) throws FileDownloadException {
        return QuiltVersion.getAll(mcVersion);
    }

    /**
     * Get the QuiltMC profile for a specific Minecraft version and QuiltMC version.
     * @param mcVersion The Minecraft version
     * @param quiltVersion The QuiltMC version
     * @return The QuiltMC profile
     * @throws FileDownloadException If the profile could not be fetched
     */
    public static QuiltProfile getQuiltProfile(String mcVersion, String quiltVersion) throws FileDownloadException {
        return QuiltProfile.get(mcVersion, quiltVersion);
    }

    /**
     * Downloads a list of quilt libraries to a specified directory and returns a list of library paths.
     * @param librariesDir The directory to download the libraries to
     * @param libraries The libraries to download
     * @param statusCallback A callback to report download status
     * @return A list of library paths
     * @throws FileDownloadException If there is an error downloading or writing a library
     */
    public static List<String> downloadQuiltLibraries(List<QuiltLibrary> libraries, File librariesDir, Consumer<DownloadStatus> statusCallback) throws FileDownloadException {
        return QuiltLibrary.downloadAll(libraries, librariesDir, statusCallback);
    }

    /**
     * Downloads a list of quilt libraries to a specified directory and returns a list of library paths.
     * @param baseDir The directory to download the libraries to
     * @param libraries The libraries to download
     * @return A list of library paths
     * @throws FileDownloadException If there is an error downloading or writing a library
     */
    public static List<String> downloadQuiltLibraries(List<QuiltLibrary> libraries, File baseDir) throws FileDownloadException {
        return downloadQuiltLibraries(libraries, baseDir, status -> {});
    }

    /**
     * Downloads a quilt library to a specified directory.
     * @param baseDir The directory to download the libraries to
     * @param library The library to download
     * @param currentLibraries The list of library paths
     * @throws FileDownloadException If there is an error downloading or writing a library
     */
    public static void downloadQuiltLibrary(QuiltLibrary library, File baseDir, ArrayList<String> currentLibraries) throws FileDownloadException {
        library.download(baseDir, currentLibraries);
    }

    private static Caching<HttpResponse<byte[]>> caching = null;

    /**
     * Sets a caching strategy for this module
     * @param caching The caching strategy to use
     */
    public static void setCaching(Caching<HttpResponse<byte[]>> caching) {
        QuiltDL.caching = caching;
    }

    public static Caching<HttpResponse<byte[]>> getCaching() {
        return caching;
    }

    public static String getQuiltMetaUrl() {
        return quiltMetaUrl;
    }
}
