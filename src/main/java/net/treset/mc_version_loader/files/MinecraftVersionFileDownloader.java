package net.treset.mc_version_loader.files;

import net.treset.mc_version_loader.minecraft.MinecraftFileDownloads;
import net.treset.mc_version_loader.minecraft.MinecraftLibrary;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
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

    public static boolean downloadVersionLibraries(List<MinecraftLibrary> libraries, File baseDir) {
        return libraries.parallelStream()
                .map(library -> downloadVersionLibrary(library, baseDir))
                .allMatch(b -> b);
    }

    public static boolean downloadVersionLibrary(MinecraftLibrary library, File baseDir) {
        if(library == null || library.getDownloads().getArtifacts().getUrl() == null || library.getDownloads().getArtifacts().getUrl().isBlank() || library.getDownloads().getArtifacts().getPath() == null || library.getDownloads().getArtifacts().getPath().isBlank() || baseDir == null || !baseDir.isDirectory()) {
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

        File outFile = new File(outDir, library.getDownloads().getArtifacts().getPath().substring(library.getDownloads().getArtifacts().getPath().lastIndexOf('/')));
        return FileUtils.downloadFile(downloadUrl, outFile);
    }
}
