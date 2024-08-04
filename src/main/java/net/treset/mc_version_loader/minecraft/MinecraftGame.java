package net.treset.mc_version_loader.minecraft;

import net.treset.mc_version_loader.exception.FileDownloadException;
import net.treset.mc_version_loader.json.SerializationException;
import net.treset.mc_version_loader.util.DownloadStatus;
import net.treset.mc_version_loader.util.FileUtil;
import net.treset.mc_version_loader.util.OsUtil;
import net.treset.mc_version_loader.util.Sources;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MinecraftGame {

    private static boolean cacheVersions = false;
    private static List<MinecraftVersion> versions = null;


    /**
     * Downloads a minecraft download..
     * @param download the download to download
     * @param targetFile the file to download to
     * @throws FileDownloadException if there is an error downloading the download
     */
    public static void downloadVersionDownload(MinecraftFileDownloads.Downloads download, File targetFile) throws FileDownloadException {
        if(download == null || download.getUrl() == null || download.getUrl().isBlank() || targetFile == null || !targetFile.getParentFile().isDirectory()) {
            throw new FileDownloadException("Unmet requirements for version download: download=" + download);
        }

        URL downloadUrl;
        try {
            downloadUrl = new URL(download.getUrl());
        } catch (MalformedURLException e) {
            throw new FileDownloadException("Unable to convert version download url: download=" + download.getUrl(), e);
        }

        FileUtil.downloadFile(downloadUrl, targetFile);
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
     * @param statusCallback the callback to report download status
     * @return a list of the downloaded libraries
     * @throws FileDownloadException if there is an error downloading the libraries
     */
    public static List<String> downloadVersionLibraries(List<MinecraftLibrary> libraries, File librariesDir, File localLibraryDir, List<String> features, File nativesDir, Consumer<DownloadStatus> statusCallback) throws FileDownloadException {
        ArrayList<String> result = new ArrayList<>();
        List<Exception> exceptionQueue = new ArrayList<>();
        int size = libraries.size();
        int current = 0;
        boolean failed = false;
        for(MinecraftLibrary l : libraries) {
            statusCallback.accept(new DownloadStatus(++current, size, l.getName(), failed));
            try {
                addVersionLibrary(l, librariesDir, localLibraryDir, nativesDir, result, features);
            } catch (FileDownloadException e) {
                exceptionQueue.add(e);
                failed = true;
            }
        }
        if(!exceptionQueue.isEmpty()) {
            throw new FileDownloadException("Unable to download " + exceptionQueue.size() + " libraries", exceptionQueue.get(0));
        }
        return result;
    }

    public static void addVersionLibrary(MinecraftLibrary library, File librariesDir, File localLibraryDir, File nativesDir, ArrayList<String> result, List<String> features) throws FileDownloadException {
        if(library == null || library.getRules() != null && !library.getRules().stream().allMatch(r -> r.isApplicable(features))) {
            return;
        }

        if(library.getDownloads() == null || librariesDir == null || !librariesDir.isDirectory()) {
            throw new FileDownloadException("Unmet requirements for library download: library=" + library.getName());
        }

        if (library.getNatives() != null && library.getDownloads().getClassifiers() != null && library.getDownloads().getClassifiers().getNatives() != null) {
            String applicableNative = null;
            if (OsUtil.isOsName("windows") && library.getNatives().getWindows() != null) {
                applicableNative = library.getNatives().getWindows();
            } else if (OsUtil.isOsName("linux") && library.getNatives().getLinux() != null) {
                applicableNative = library.getNatives().getLinux();
            } else if (OsUtil.isOsName("osx") && library.getNatives().getOsx() != null) {
                applicableNative = library.getNatives().getOsx();
            }

            for (MinecraftLibrary.Downloads.Classifiers.Native na : library.getDownloads().getClassifiers().getNatives()) {
                if (na.getName().equals(applicableNative)) {
                    String libPath = downloadArtifact(na.getArtifact(), librariesDir, localLibraryDir);
                    applyNative(new File(librariesDir, libPath), nativesDir, library.getExtract());
                }
            }
        }

        if(library.getDownloads().getArtifact() != null && !library.getDownloads().getArtifact().getUrl().isBlank()) {
            result.add(downloadArtifact(library.getDownloads().getArtifact(), librariesDir, localLibraryDir));
        }
    }

    public static String downloadArtifact(MinecraftLibrary.Downloads.Artifact artifact, File baseDir) throws FileDownloadException {
        return downloadArtifact(artifact, baseDir, null);
    }

    public static String downloadArtifact(MinecraftLibrary.Downloads.Artifact artifact, File baseDir, File localDir) throws FileDownloadException {
        if(artifact == null || baseDir == null || !baseDir.isDirectory() || artifact.getPath() == null || artifact.getPath().isBlank()) {
            throw new FileDownloadException("Unmet requirements for artifact download: artifact=" + artifact);
        }

        if(artifact.getUrl() == null) {
            return copyLocalArtifact(artifact.getPath(), baseDir, localDir);
        } else {
            URL downloadUrl;
            try {
                downloadUrl = new URL(artifact.getUrl());
            } catch (MalformedURLException e) {
                throw new FileDownloadException("Unable to convert artifact download url: artifact=" + artifact.getUrl(), e);
            }

            File outDir = new File(baseDir, artifact.getPath().substring(0, artifact.getPath().lastIndexOf('/')));
            if (!outDir.isDirectory() && !outDir.mkdirs()) {
                throw new FileDownloadException("Unable to make required dirs: artifact=" + artifact.getPath());
            }
            File outFile = new File(outDir, artifact.getPath().substring(artifact.getPath().lastIndexOf('/')));

            FileUtil.downloadFile(downloadUrl, outFile);

            return artifact.getPath();
        }
    }

    public static String copyLocalArtifact(String path, File baseDir, File localDir) throws FileDownloadException {
        if(path == null || path.isBlank() || baseDir == null || !baseDir.isDirectory() || localDir == null || !localDir.isDirectory()) {
            return null;
        }

        File outDir = new File(baseDir, path.substring(0, path.lastIndexOf('/')));
        if (!outDir.isDirectory() && !outDir.mkdirs()) {
            return null;
        }
        File outFile = new File(outDir, path.substring(path.lastIndexOf('/')));

        File localFile = new File(localDir, path);
        if (localFile.isFile()) {
            try {
                Files.copy(localFile.toPath(), outFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                return path;
            } catch (IOException e) {
                throw new FileDownloadException("Unable to copy local artifact: artifact=" + path, e);
            }
        }
        throw new FileDownloadException("Local artifact not found: artifact=" + path);
    }

    public static boolean applyNative(File nativeFile, File nativeDir, MinecraftLibrary.Extract extract) throws FileDownloadException {
        if(nativeFile == null || !nativeFile.isFile() || nativeDir == null) {
            throw new FileDownloadException("Unmet requirements for native application: file=" + nativeFile);
        }

        try {
            Files.createDirectories(nativeDir.toPath());
        } catch (IOException e) {
            throw new FileDownloadException("Unable to create native directory", e);
        }

        try {
            applyExtract(nativeFile, nativeDir, extract);
            return true;
        } catch(IOException e) {
            File target = new File(nativeDir, nativeFile.getName());
            try {
                Files.copy(nativeFile.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e1) {
                throw new FileDownloadException("Unable to extract or copy native file: file=" + nativeFile, e1);
            }
            return false;
        }
    }

    public static void applyExtract(File file, File nativesDir, MinecraftLibrary.Extract extract) throws IOException {
        if(file == null || !file.isFile() || nativesDir == null || !nativesDir.isDirectory()) {
            throw new IOException("Unmet requirements for extract: file=" + file);
        }

        File tempDir = new File(FileUtil.getTempDir(), file.getName());
        Files.createDirectories(tempDir.toPath());

        FileUtil.exctractFile(file, tempDir);

        if(extract != null && extract.getExclude() != null) {
            for(String exclude : extract.getExclude()) {
                File toRemove = new File(tempDir, exclude);

                if(toRemove.exists()) {
                    try {
                        FileUtil.delete(toRemove);
                    } catch (IOException e) {
                        throw new IOException("Unable to delete extracted file: file=" + toRemove, e);
                    }
                }
            }
        }

        FileUtil.copyDirectory(tempDir, nativesDir, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Gets a list of all minecraft versions
     * @return a list of all minecraft versions
     * @throws FileDownloadException if there is an error downloading the version manifest
     */
    public static List<MinecraftVersion> getVersions() throws FileDownloadException {
        if(cacheVersions && versions != null) {
            return versions;
        }
        try {
            List<MinecraftVersion> v = MinecraftVersion.fromVersionManifest(FileUtil.getStringFromUrl(Sources.getVersionManifestUrl()));
            if(cacheVersions) {
                versions = v;
            }
            return v;
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse version manifest", e);
        }
    }

    /**
     * Gets a list of all minecraft release versions
     * @return a list of all minecraft release version
     * @throws FileDownloadException if there is an error downloading the version manifest
     */
    public static List<MinecraftVersion> getReleases() throws FileDownloadException {
        return getVersions().stream().filter(MinecraftVersion::isRelease).toList();
    }

    /**
     * Gets a specific minecraft version.
     * @param url the url of the version manifest
     * @return the minecraft version
     * @throws FileDownloadException if there is an error downloading the version manifest
     */
    public static MinecraftVersion getVersion(String url) throws FileDownloadException {
        try {
            return MinecraftVersion.fromJson(FileUtil.getStringFromUrl(url));
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse version manifest", e);
        }
    }

    /**
     * Gets details for a specific minecraft version.
     * @param url the url of the details manifest
     * @return the minecraft version
     * @throws FileDownloadException if there is an error downloading the version manifest
     */
    public static MinecraftVersionDetails getVersionDetails(String url) throws FileDownloadException {
        try {
            return MinecraftVersionDetails.fromJson(FileUtil.getStringFromUrl(url));
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse version manifest", e);
        }
    }

    /**
     * If set to true a list of minecraft versions will be cached when @code{getVersions} or @code{getReleases} is first called and reused on subsequent calls. Else the versions will be fetched every time. Default is false.
     * @param useCache if true the versions will be cached
     */
    public static void useVersionCache(boolean useCache) {
        cacheVersions = useCache;
    }
 }
