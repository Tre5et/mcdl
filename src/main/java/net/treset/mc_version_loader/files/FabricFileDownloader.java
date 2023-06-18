package net.treset.mc_version_loader.files;

import net.treset.mc_version_loader.exception.FileDownloadException;
import net.treset.mc_version_loader.fabric.FabricLibrary;
import net.treset.mc_version_loader.fabric.FabricLoaderData;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FabricFileDownloader {
    public static void downloadFabricLoader(File baseDir, FabricLoaderData loader) throws FileDownloadException {
        downloadFabricLoader(baseDir, loader, status -> {});
    }

    public static void downloadFabricLoader(File baseDir, FabricLoaderData loader, Consumer<DownloadStatus> statusCallback) throws FileDownloadException {
        if(loader == null || loader.getMaven() == null || loader.getMaven().isBlank() || baseDir == null || !baseDir.isDirectory()) {
            throw new FileDownloadException("Unmet requirements for fabric download");
        }

        MavenPom mavenPom;
        try {
            mavenPom = FileUtils.parseMavenUrl(Sources.getFabricMavenUrl(), loader.getMaven());
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

        File outFile = new File(baseDir, "fabric-client.jar");

        FileUtils.downloadFile(downloadUrl, outFile);
    }

    public static List<String> downloadFabricLibraries(File baseDir, List<FabricLibrary> libraries) throws FileDownloadException {
        return downloadFabricLibraries(baseDir, libraries, status -> {});
    }

    public static List<String> downloadFabricLibraries(File baseDir, List<FabricLibrary> libraries, Consumer<DownloadStatus> statusCallback) throws FileDownloadException {
        statusCallback.accept(new DownloadStatus(0, libraries.size(), "", false));
        ArrayList<String> result = new ArrayList<>();
        ArrayList<Exception> exceptionQueue = new ArrayList<>();
        int size = libraries.size();
        int current = 0;
        boolean failed = false;
        for(FabricLibrary library : libraries) {
            statusCallback.accept(new DownloadStatus(current++, size, library.getName(), failed));
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

    public static void addFabricLibrary(File baseDir, FabricLibrary library, ArrayList<String> result) throws FileDownloadException {
        if(library == null || library.getUrl() == null || library.getUrl().isBlank() || library.getName() == null || library.getName().isBlank() || baseDir == null || !baseDir.isDirectory()) {
            throw new FileDownloadException("Unmet requirements for fabric library download: library=" + library);
        }

        MavenPom mavenPom;
        try {
            mavenPom = FileUtils.parseMavenUrl(library.getUrl(), library.getName());
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

        FileUtils.downloadFile(downloadUrl, libraryFile);
    }
}
