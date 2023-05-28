package net.treset.mc_version_loader.files;

import net.treset.mc_version_loader.fabric.FabricLibrary;
import net.treset.mc_version_loader.fabric.FabricLoaderData;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FabricFileDownloader {
    private static final Logger LOGGER = Logger.getLogger(FileUtils.class.toString());

    public static boolean downloadFabricLoader(File baseDir, FabricLoaderData loader) {
        if(loader == null || loader.getMaven() == null || loader.getMaven().isBlank() || baseDir == null || !baseDir.isDirectory()) {
            LOGGER.log(Level.WARNING, "Unmet requirements for fabric download");
            return false;
        }

        MavenPom mavenPom = FileUtils.parseMavenUrl(Sources.getFabricMavenUrl(), loader.getMaven());
        if(mavenPom == null) {
            LOGGER.log(Level.WARNING, "Unable to parse fabric version maven file");
            return false;
        }
        String mavenUrl = mavenPom.getMavenUrl();
        if(mavenUrl == null || mavenUrl.isBlank()) {
            LOGGER.log(Level.WARNING, "Unable to parse fabric version maven file");
            return false;
        }

        URL downloadUrl;
        try {
            downloadUrl = new URL(Sources.getFabricMavenUrl() + mavenUrl);
        } catch (MalformedURLException e) {
            LOGGER.log(Level.WARNING, "Unable to parse fabric Url", e);
            return false;
        }

        File outFile = new File(baseDir, "fabric-client.jar");

        return FileUtils.downloadFile(downloadUrl, outFile);
    }

    public static List<String> downloadFabricLibraries(File baseDir, List<FabricLibrary> libraries) {
        ArrayList<String> result = new ArrayList<>();
        if(!libraries.parallelStream()
                .allMatch(library -> addFabricLibrary(baseDir, library, result))) {
            LOGGER.log(Level.WARNING, "Unable to download all libraries");
            return null;
        }
        return result;
    }

    public static boolean addFabricLibrary(File baseDir, FabricLibrary library, ArrayList<String> result) {
        if(library == null || library.getUrl() == null || library.getUrl().isBlank() || library.getName() == null || library.getName().isBlank() || baseDir == null || !baseDir.isDirectory()) {
            LOGGER.log(Level.WARNING, "Unmet requirements for fabric download");
            return false;
        }

        MavenPom mavenPom = FileUtils.parseMavenUrl(library.getUrl(), library.getName());
        if(mavenPom == null) {
            LOGGER.log(Level.WARNING, "Unable to parse fabric version maven file");
            return false;
        }
        String mavenUrl = mavenPom.getMavenUrl();
        if(mavenUrl == null || mavenUrl.isBlank()) {
            LOGGER.log(Level.WARNING, "Unable to parse fabric version maven file");
            return false;
        }

        URL downloadUrl;
        try {
            downloadUrl = new URL(library.getUrl() + mavenUrl);
        } catch (MalformedURLException e) {
            LOGGER.log(Level.WARNING, "Unable to parse fabric library url", e);
            return false;
        }

        File libraryDir = new File(baseDir, mavenPom.getMavenDir());
        if(!libraryDir.isDirectory() && !libraryDir.mkdirs()) {
            LOGGER.log(Level.WARNING, "Unable to create library dir");
            return false;
        }

        File libraryFile = new File(libraryDir, mavenPom.getMavenFileName());

        library.setLocalPath(mavenPom.getMavenDir());
        library.setLocalFileName(mavenPom.getMavenFileName());

        result.add(library.getLocalPath() + library.getLocalFileName());

        return FileUtils.downloadFile(downloadUrl, libraryFile);
    }
}
