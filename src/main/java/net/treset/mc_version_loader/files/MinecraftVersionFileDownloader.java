package net.treset.mc_version_loader.files;

import net.treset.mc_version_loader.exception.FileDownloadException;
import net.treset.mc_version_loader.minecraft.MinecraftFileDownloads;
import net.treset.mc_version_loader.minecraft.MinecraftLibrary;
import net.treset.mc_version_loader.os.OsDetails;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MinecraftVersionFileDownloader {

    public static void downloadVersionDownload(MinecraftFileDownloads.Downloads download, File baseDir) throws FileDownloadException {
        if(download == null || download.getUrl() == null || download.getUrl().isBlank() || baseDir == null || !baseDir.isDirectory()) {
            throw new FileDownloadException("Unmet requirements for version download: download=" + download);
        }

        URL downloadUrl;
        try {
            downloadUrl = new URL(download.getUrl());
        } catch (MalformedURLException e) {
            throw new FileDownloadException("Unable to convert version download url: download=" + download.getUrl(), e);
        }

        File outFile = new File(baseDir, download.getUrl().substring(download.getUrl().lastIndexOf('/')));
        FileUtils.downloadFile(downloadUrl, outFile);
    }

    public static List<String> downloadVersionLibraries(List<MinecraftLibrary> libraries, File baseDir, List<String> features) throws FileDownloadException {
        ArrayList<String> result = new ArrayList<>();
        List<Exception> exceptionQueue = new ArrayList<>();
        libraries.forEach(l -> {
                    try {
                        addVersionLibrary(l, baseDir, result, features);
                    } catch (FileDownloadException e) {
                        exceptionQueue.add(e);
                    }
        });
        if(!exceptionQueue.isEmpty()) {
            throw new FileDownloadException("Unable to download " + exceptionQueue.size() + " libraries", exceptionQueue);
        }
        return result;
    }

    public static void addVersionLibrary(MinecraftLibrary library, File baseDir, ArrayList<String> result, List<String> features) throws FileDownloadException {
        if(library == null || library.getRules() != null && !library.getRules().stream().allMatch(r -> r.isApplicable(features))) {
            return;
        }

        if(library.getDownloads().getArtifacts().getUrl() == null || library.getDownloads().getArtifacts().getUrl().isBlank() || library.getDownloads().getArtifacts().getPath() == null || library.getDownloads().getArtifacts().getPath().isBlank() || baseDir == null || !baseDir.isDirectory()) {
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
            if(OsDetails.isOsName("windows") && library.getNatives().getWindows() != null) {
                applicableNatives.add(library.getNatives().getWindows());
            } else if(OsDetails.isOsName("linux") && library.getNatives().getLinux() != null) {
                applicableNatives.add(library.getNatives().getLinux());
            } else if(OsDetails.isOsName("osx") && library.getNatives().getOsx() != null) {
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
                        FileUtils.downloadFile(nativeUrl, outFile);
                        result.add(na.getArtifact().getPath());
                    }
                }
            }
        }

        File outFile = new File(outDir, library.getDownloads().getArtifacts().getPath().substring(library.getDownloads().getArtifacts().getPath().lastIndexOf('/')));
        result.add(library.getDownloads().getArtifact().getPath());
        FileUtils.downloadFile(downloadUrl, outFile);
    }
}
