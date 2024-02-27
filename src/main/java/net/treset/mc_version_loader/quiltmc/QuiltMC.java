package net.treset.mc_version_loader.quiltmc;

import net.treset.mc_version_loader.exception.FileDownloadException;
import net.treset.mc_version_loader.json.SerializationException;
import net.treset.mc_version_loader.util.DownloadStatus;
import net.treset.mc_version_loader.util.FileUtil;
import net.treset.mc_version_loader.util.MavenPom;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class QuiltMC {
    private static String quiltMetaUrl = "https://meta.quiltmc.org/v3/versions/";
    public static String getQuiltMetaUrl() {
        return quiltMetaUrl;
    }

    private static boolean cacheVersions = false;
    private static Map<String, List<QuiltVersion>> versionCache = new HashMap<>();

    /**
     * Get a list of QuiltMC versions for a specific Minecraft version.
     * @param mcVersion The Minecraft version to get QuiltMC versions for
     * @return A list of QuiltMC versions
     * @throws FileDownloadException If the versions could not be fetched
     */
    public static List<QuiltVersion> getQuiltVersions(String mcVersion) throws FileDownloadException {
        if(cacheVersions && versionCache.containsKey(mcVersion)) {
            return versionCache.get(mcVersion);
        }
        try {
            List<QuiltVersion> v = QuiltVersion.fromJsonArray(FileUtil.getStringFromUrl(quiltMetaUrl + "loader/" + mcVersion));
            if(cacheVersions) {
                versionCache.put(mcVersion, v);
            }
            return v;
        } catch (SerializationException e) {
            throw new FileDownloadException("Failed to parse QuiltMC version JSON", e);
        }
    }

    /**
     * Get a list of QuiltMC release versions for a specific Minecraft version.
     * @param mcVersion The Minecraft version to get QuiltMC versions for
     * @return A list of QuiltMC versions
     * @throws FileDownloadException If the versions could not be fetched
     */
    public static List<QuiltVersion> getQuiltReleases(String mcVersion) throws FileDownloadException {
        return getQuiltVersions(mcVersion).stream()
                .filter(v ->
                        v != null && v.getLoader() != null && v.getLoader().getVersion() != null && !v.getLoader().getVersion().contains("-beta") && !v.getLoader().getVersion().contains("-alpha") && !v.getLoader().getVersion().contains("-pre")
                )
                .toList();
    }

    /**
     * Get the QuiltMC profile for a specific Minecraft version and QuiltMC version.
     * @param mcVersion The Minecraft version
     * @param quiltVersion The QuiltMC version
     * @return The QuiltMC profile
     * @throws FileDownloadException If the profile could not be fetched
     */
    public static QuiltProfile getQuiltProfile(String mcVersion, String quiltVersion) throws FileDownloadException {
        try {
            return QuiltProfile.fromJson(FileUtil.getStringFromUrl(quiltMetaUrl + "loader/" + mcVersion + "/" + quiltVersion + "/profile/json"));
        } catch (SerializationException e) {
            throw new FileDownloadException("Failed to parse QuiltMC profile JSON", e);
        }
    }

    /**
     * Downloads a list of quilt libraries to a specified directory and returns a list of library paths.
     * @param baseDir The directory to download the libraries to
     * @param libraries The libraries to download
     * @param statusCallback A callback to report download status
     * @return A list of library paths
     * @throws FileDownloadException If there is an error downloading or writing a library
     */
    public static List<String> downloadQuiltLibraries(File baseDir, List<QuiltLibrary> libraries, Consumer<DownloadStatus> statusCallback) throws FileDownloadException {
        statusCallback.accept(new DownloadStatus(0, libraries.size(), "", false));
        ArrayList<String> result = new ArrayList<>();
        int size = libraries.size();
        int current = 0;
        for(QuiltLibrary lib : libraries) {
            statusCallback.accept(new DownloadStatus(++current, size, lib.getName(), false));
            addQuiltLibrary(baseDir, lib, result);
        }
        return result;
    }

    /**
     * Downloads a list of quilt libraries to a specified directory and returns a list of library paths.
     * @param baseDir The directory to download the libraries to
     * @param libraries The libraries to download
     * @return A list of library paths
     * @throws FileDownloadException If there is an error downloading or writing a library
     */
    public static List<String> downloadQuiltLibraries(File baseDir, List<QuiltLibrary> libraries) throws FileDownloadException {
        return downloadQuiltLibraries(baseDir, libraries, status -> {});
    }

    private static void addQuiltLibrary(File baseDir, QuiltLibrary library, ArrayList<String> result) throws FileDownloadException {
        if(library == null || library.getUrl() == null || library.getUrl().isBlank() || library.getName() == null || library.getName().isBlank() || baseDir == null || !baseDir.isDirectory()) {
            throw new FileDownloadException("Unmet requirements for quilt library download: library=" + library);
        }

        MavenPom mavenPom;
        try {
            mavenPom = FileUtil.parseMavenUrl(library.getUrl(), library.getName());
        } catch (MalformedURLException | FileDownloadException e) {
            throw new FileDownloadException("Unable to parse quilt library maven file Url: library=" + library.getName(), e);
        }

        String mavenUrl = mavenPom.getMavenUrl();
        if(mavenUrl == null || mavenUrl.isBlank()) {
            throw new FileDownloadException("Unable to parse quilt library maven file: library=" + library.getName());
        }

        URL downloadUrl;
        try {
            downloadUrl = new URL(library.getUrl() + mavenUrl);
        } catch (MalformedURLException e) {
            throw new FileDownloadException("Unable to parse quilt library download Url: library=" + library.getName(), e);
        }

        File libraryDir = new File(baseDir, mavenPom.getMavenDir());
        if(!libraryDir.isDirectory() && !libraryDir.mkdirs()) {
            throw new FileDownloadException("Unable to create library dir: library=" + library.getName());
        }

        File libraryFile = new File(libraryDir, mavenPom.getMavenFileName());

        FileUtil.downloadFile(downloadUrl, libraryFile);

        result.add(mavenPom.getMavenDir() + mavenPom.getMavenFileName());
    }

    public static void useVersionCache(boolean useCache) {
        cacheVersions = useCache;
    }
}
