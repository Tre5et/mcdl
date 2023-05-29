package net.treset.mc_version_loader.files;

import net.treset.mc_version_loader.minecraft.MinecraftFileDownloads;
import net.treset.mc_version_loader.minecraft.MinecraftLibrary;
import net.treset.mc_version_loader.os.OsDetails;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MinecraftVersionFileDownloader {
    private static final Logger LOGGER = Logger.getLogger(MinecraftVersionFileDownloader.class.toString());

    public static boolean downloadVersionDownload(MinecraftFileDownloads.Downloads download, File baseDir) {
        if(download == null || download.getUrl() == null || download.getUrl().isBlank() || baseDir == null || !baseDir.isDirectory()) {
            LOGGER.log(Level.WARNING, "Unable to start version download; unmet requirements");
            return false;
        }

        URL downloadUrl;
        try {
            downloadUrl = new URL(download.getUrl());
        } catch (MalformedURLException e) {
            LOGGER.log(Level.WARNING, "Unable to convert download url", e);
            return false;
        }

        File outFile = new File(baseDir, download.getUrl().substring(download.getUrl().lastIndexOf('/')));
        return FileUtils.downloadFile(downloadUrl, outFile);
    }

    public static List<String> downloadVersionLibraries(List<MinecraftLibrary> libraries, File baseDir, List<String> features) {
        ArrayList<String> result = new ArrayList<>();
        boolean success = libraries.stream()
                .allMatch(library -> addVersionLibrary(library, baseDir, result, features));
        if(!success) {
            LOGGER.log(Level.WARNING, "Unable to download all libraries");
            return null;
        }
        return result;
    }

    public static boolean addVersionLibrary(MinecraftLibrary library, File baseDir, ArrayList<String> result, List<String> features) {
        if(library == null || library.getRules() != null && !library.getRules().stream().allMatch(r -> r.isApplicable(features))) {
            LOGGER.log(Level.INFO, "Skipping library " + library.getName() + " due to rules");
            return true;
        }

        if(library.getDownloads().getArtifacts().getUrl() == null || library.getDownloads().getArtifacts().getUrl().isBlank() || library.getDownloads().getArtifacts().getPath() == null || library.getDownloads().getArtifacts().getPath().isBlank() || baseDir == null || !baseDir.isDirectory()) {
            LOGGER.log(Level.WARNING, "Unable to start library download; unmet requirements");
            return false;
        }

        URL downloadUrl;
        try {
            downloadUrl = new URL(library.getDownloads().getArtifacts().getUrl());
        } catch (MalformedURLException e) {
            LOGGER.log(Level.WARNING, "Unable to convert download url", e);
            return false;
        }

        File outDir = new File(baseDir,library.getDownloads().getArtifacts().getPath().substring(0, library.getDownloads().getArtifacts().getPath().lastIndexOf('/')));
        if(!outDir.isDirectory() && !outDir.mkdirs()) {
            LOGGER.log(Level.WARNING, "Unable to make required dirs");
            return false;
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
                            LOGGER.log(Level.WARNING, "Unable to convert native download url", e);
                            return false;
                        }

                        File nativeOutDir = new File(baseDir, na.getArtifact().getPath().substring(0, na.getArtifact().getPath().lastIndexOf('/')));
                        if(!nativeOutDir.isDirectory() && !nativeOutDir.mkdirs()) {
                            LOGGER.log(Level.WARNING, "Unable to make required dirs");
                            return false;
                        }
                        File outFile = new File(outDir, na.getArtifact().getPath().substring(na.getArtifact().getPath().lastIndexOf('/')));
                        if(!FileUtils.downloadFile(nativeUrl, outFile)) {
                            LOGGER.log(Level.WARNING, "Unable to download native file");
                            return false;
                        }
                        result.add(na.getArtifact().getPath());
                    }
                }
            }
        }

        File outFile = new File(outDir, library.getDownloads().getArtifacts().getPath().substring(library.getDownloads().getArtifacts().getPath().lastIndexOf('/')));
        result.add(library.getDownloads().getArtifact().getPath());
        return FileUtils.downloadFile(downloadUrl, outFile);
    }
}
