package net.treset.mc_version_loader.fabric;

import net.treset.mc_version_loader.exception.FileDownloadException;
import net.treset.mc_version_loader.json.SerializationException;
import net.treset.mc_version_loader.util.DownloadStatus;
import net.treset.mc_version_loader.util.FileUtil;
import net.treset.mc_version_loader.util.MavenPom;
import net.treset.mc_version_loader.util.Sources;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class FabricLoader {
    private static boolean cacheVersions = false;
    private static final Map<String, List<FabricVersionDetails>> versionCache = new HashMap<>();

    /**
     * Downloads the fabric client jar to a specified directory.
     * @param outFile The file to download the fabric client as
     * @param loader The loader to download
     * @throws FileDownloadException If there is an error downloading or writing the loader
     */
    public static void downloadFabricClient(File outFile, FabricLoaderData loader) throws FileDownloadException {
        if(loader == null || loader.getMaven() == null || loader.getMaven().isBlank() || outFile == null || !outFile.getParentFile().isDirectory()) {
            throw new FileDownloadException("Unmet requirements for fabric download");
        }

        MavenPom mavenPom;
        try {
            mavenPom = FileUtil.parseMavenUrl(Sources.getFabricMavenUrl(), loader.getMaven());
        } catch (MalformedURLException | FileDownloadException e) {
            throw new FileDownloadException("Unable to parse fabric version maven file Url", e);
        }

        String mavenUrl = mavenPom.getMavenUrl();
        if(mavenUrl == null || mavenUrl.isBlank()) {
            throw new FileDownloadException("Unable to parse fabric version maven file");
        }

        URL downloadUrl;
        try {
            downloadUrl = new URL(Sources.getFabricMavenUrl() + mavenUrl);
        } catch (MalformedURLException e) {
            throw new FileDownloadException("Unable to parse fabric download Url", e);
        }

        FileUtil.downloadFile(downloadUrl, outFile);
    }

    /**
     * Downloads a list of fabric libraries to a specified directory and returns a list of library paths.
     * @param baseDir The directory to download the libraries to
     * @param libraries The libraries to download
     * @return A list of library paths
     * @throws FileDownloadException If there is an error downloading or writing a library
     */
    public static List<String> downloadFabricLibraries(File baseDir, List<FabricLibrary> libraries) throws FileDownloadException {
        return downloadFabricLibraries(baseDir, libraries, status -> {});
    }

    /**
     * Downloads a list of fabric libraries to a specified directory and returns a list of library paths.
     * @param baseDir The directory to download the libraries to
     * @param libraries The libraries to download
     * @param statusCallback A callback to be called when a library is downloaded
     * @return A list of library paths
     * @throws FileDownloadException If there is an error downloading or writing a library
     */
    public static List<String> downloadFabricLibraries(File baseDir, List<FabricLibrary> libraries, Consumer<DownloadStatus> statusCallback) throws FileDownloadException {
        statusCallback.accept(new DownloadStatus(0, libraries.size(), "", false));
        ArrayList<String> result = new ArrayList<>();
        ArrayList<Exception> exceptionQueue = new ArrayList<>();
        int size = libraries.size();
        int current = 0;
        boolean failed = false;
        for(FabricLibrary library : libraries) {
            statusCallback.accept(new DownloadStatus(++current, size, library.getName(), failed));
            try {
                addFabricLibrary(baseDir, library, result);
            } catch (FileDownloadException e) {
                exceptionQueue.add(e);
                failed = true;
            }
        }
        if(!exceptionQueue.isEmpty()) {
            throw new FileDownloadException("Unable to download " + exceptionQueue.size() + " fabric libraries", exceptionQueue.get(0));
        }
        return result;
    }

    /**
     * Downloads a fabric library to a specified directory and adds it to a list of library paths.
     * @param baseDir The directory to download the library to
     * @param library The library to download
     * @param result The list of library paths to add the library to
     * @throws FileDownloadException If there is an error downloading or writing the library
     */
    public static void addFabricLibrary(File baseDir, FabricLibrary library, ArrayList<String> result) throws FileDownloadException {
        if(library == null || library.getUrl() == null || library.getUrl().isBlank() || library.getName() == null || library.getName().isBlank() || baseDir == null || !baseDir.isDirectory()) {
            throw new FileDownloadException("Unmet requirements for fabric library download: library=" + library);
        }

        MavenPom mavenPom;
        try {
            mavenPom = FileUtil.parseMavenUrl(library.getUrl(), library.getName());
        } catch (MalformedURLException | FileDownloadException e) {
            throw new FileDownloadException("Unable to parse fabric library maven file Url: library=" + library.getName(), e);
        }

        String mavenUrl = mavenPom.getMavenUrl();
        if(mavenUrl == null || mavenUrl.isBlank()) {
            throw new FileDownloadException("Unable to parse fabric library maven file: library=" + library.getName());
        }

        URL downloadUrl;
        try {
            downloadUrl = new URL(library.getUrl() + mavenUrl);
        } catch (MalformedURLException e) {
            throw new FileDownloadException("Unable to parse fabric library download Url: library=" + library.getName(), e);
        }

        File libraryDir = new File(baseDir, mavenPom.getMavenDir());
        if(!libraryDir.isDirectory() && !libraryDir.mkdirs()) {
            throw new FileDownloadException("Unable to create library dir: library=" + library.getName());
        }

        File libraryFile = new File(libraryDir, mavenPom.getMavenFileName());

        library.setLocalPath(mavenPom.getMavenDir());
        library.setLocalFileName(mavenPom.getMavenFileName());

        result.add(library.getLocalPath() + library.getLocalFileName());

        FileUtil.downloadFile(downloadUrl, libraryFile);
    }

    /**
     * Gets the fabric profile for a specified minecraft version and fabric version.
     * @param mcVersion The minecraft version to get the fabric profile for
     * @param fabricVersion The fabric version to get the profile for
     * @return The fabric profile
     * @throws FileDownloadException If there is an error loading or parsing the profile
     */
    public static FabricProfile getFabricProfile(String mcVersion, String fabricVersion) throws FileDownloadException {
        try {
            return FabricProfile.fromJson(FileUtil.getStringFromUrl(Sources.getFabricProfileUrl(mcVersion, fabricVersion)));
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse fabric profile", e);
        }
    }

    /**
     * Gets the fabric version details for a specified minecraft version and fabric version.
     * @param mcVersion The minecraft version to get fabric version details for
     * @param fabricVersion The fabric version to get details for
     * @return The fabric version details
     * @throws FileDownloadException If there is an error loading or parsing the version details
     */
    public static FabricVersionDetails getFabricVersionDetails(String mcVersion, String fabricVersion) throws FileDownloadException {
        try {
            return FabricVersionDetails.fromJson(FileUtil.getStringFromUrl(Sources.getFabricVersionUrl(mcVersion, fabricVersion)));
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse fabric version details", e);
        }
    }

    /**
     * Gets all fabric loader versions for a specified minecraft version.
     * @param mcVersion The minecraft version to get fabric loader versions for
     * @return A list of fabric loader versions
     * @throws FileDownloadException If there is an error loading or parsing the versions
     */
    public static List<FabricVersionDetails> getFabricVersions(String mcVersion) throws FileDownloadException {
        if(cacheVersions && versionCache.containsKey(mcVersion)) {
            return versionCache.get(mcVersion);
        }
        try {
            List<FabricVersionDetails> v = FabricVersionDetails.fromJsonArray(FileUtil.getStringFromUrl(Sources.getFabricIndexUrl(mcVersion)));
            if(cacheVersions) {
                versionCache.put(mcVersion, v);
            }
            return v;
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse fabric version details", e);
        }
    }

    /**
     * If set to true a list of fabric versions will be cached when @code{getFabricVersions} is first called and reused on subsequent calls with the same version. Else the versions will be fetched every time. Default is false.
     * @param useCache if true the versions will be cached
     */
    public static void useVersionCache(boolean useCache) {
        cacheVersions = useCache;
    }
}
