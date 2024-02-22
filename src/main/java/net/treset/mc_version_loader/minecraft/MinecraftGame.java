package net.treset.mc_version_loader.minecraft;

import net.treset.mc_version_loader.exception.FileDownloadException;
import net.treset.mc_version_loader.json.SerializationException;
import net.treset.mc_version_loader.util.DownloadStatus;
import net.treset.mc_version_loader.util.FileUtil;
import net.treset.mc_version_loader.util.OsUtil;
import net.treset.mc_version_loader.util.Sources;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MinecraftGame {

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

    public static List<String> downloadVersionLibraries(List<MinecraftLibrary> libraries, File baseDir, List<String> features, Consumer<DownloadStatus> statusCallback) throws FileDownloadException {
                ArrayList<String> result = new ArrayList<>();
        List<Exception> exceptionQueue = new ArrayList<>();
        int size = libraries.size();
        int current = 0;
        boolean failed = false;
        for(MinecraftLibrary l : libraries) {
            statusCallback.accept(new DownloadStatus(++current, size, l.getName(), failed));
            try {
                addVersionLibrary(l, baseDir, result, features);
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

    public static void addVersionLibrary(MinecraftLibrary library, File baseDir, ArrayList<String> result, List<String> features) throws FileDownloadException {
        if(library == null || library.getDownloads().getArtifacts().getUrl() == null || library.getDownloads().getArtifacts().getUrl().isBlank() || library.getRules() != null && !library.getRules().stream().allMatch(r -> r.isApplicable(features))) {
            return;
        }

        if(library.getDownloads().getArtifacts().getPath() == null || library.getDownloads().getArtifacts().getPath().isBlank() || baseDir == null || !baseDir.isDirectory()) {
            throw new FileDownloadException("Unmet requirements for library download: library=" + library);
        }

        URL downloadUrl;
        try {
            downloadUrl = new URL(library.getDownloads().getArtifacts().getUrl());
        } catch (MalformedURLException e) {
            throw new FileDownloadException("Unable to convert download url: library=" + library.getName(), e);
        }

        File outDir = new File(baseDir,library.getDownloads().getArtifacts().getPath().substring(0, library.getDownloads().getArtifacts().getPath().lastIndexOf('/')));
        if(!outDir.isDirectory() && !outDir.mkdirs()) {
            throw new FileDownloadException("Unable to make required dirs: library=" + library.getName());
        }

        if(library.getNatives() != null) {
            List<String> applicableNatives = new ArrayList<>();
            if(OsUtil.isOsName("windows") && library.getNatives().getWindows() != null) {
                applicableNatives.add(library.getNatives().getWindows());
            } else if(OsUtil.isOsName("linux") && library.getNatives().getLinux() != null) {
                applicableNatives.add(library.getNatives().getLinux());
            } else if(OsUtil.isOsName("osx") && library.getNatives().getOsx() != null) {
                applicableNatives.add(library.getNatives().getOsx());
            }

            for(String n : applicableNatives) {
                for(MinecraftLibrary.Downloads.Classifiers.Native na : library.getDownloads().getClassifiers().getNatives()) {
                    if(n.equals(na.getName())) {
                        URL nativeUrl;
                        try {
                            nativeUrl = new URL(na.getArtifact().getUrl());
                        } catch (MalformedURLException e) {
                            throw new FileDownloadException("Unable to convert native download url: library=" + library.getName() + ", native=" + na.getName(), e);
                        }

                        File nativeOutDir = new File(baseDir, na.getArtifact().getPath().substring(0, na.getArtifact().getPath().lastIndexOf('/')));
                        if(!nativeOutDir.isDirectory() && !nativeOutDir.mkdirs()) {
                            throw new FileDownloadException("Unable to make required native dirs: library=" + library.getName()  + ", native=" + na.getName());
                        }
                        File outFile = new File(outDir, na.getArtifact().getPath().substring(na.getArtifact().getPath().lastIndexOf('/')));
                        FileUtil.downloadFile(nativeUrl, outFile);
                        result.add(na.getArtifact().getPath());
                    }
                }
            }
        }

        File outFile = new File(outDir, library.getDownloads().getArtifacts().getPath().substring(library.getDownloads().getArtifacts().getPath().lastIndexOf('/')));
        result.add(library.getDownloads().getArtifact().getPath());
        FileUtil.downloadFile(downloadUrl, outFile);
    }

    /**
     * Gets a list of all minecraft versions
     * @return a list of all minecraft versions
     * @throws FileDownloadException if there is an error downloading the version manifest
     */
    public static List<MinecraftVersion> getVersions() throws FileDownloadException {
        try {
            return MinecraftVersion.fromVersionManifest(FileUtil.getStringFromUrl(Sources.getVersionManifestUrl()));
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

    public static MinecraftVersion getVersion(String url) throws FileDownloadException {
        try {
            return MinecraftVersion.fromJson(FileUtil.getStringFromUrl(url));
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse version manifest", e);
        }
    }

    public static MinecraftVersionDetails getVersionDetails(String url) throws FileDownloadException {
        try {
            return MinecraftVersionDetails.fromJson(FileUtil.getStringFromUrl(url));
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse version manifest", e);
        }
    }
 }
