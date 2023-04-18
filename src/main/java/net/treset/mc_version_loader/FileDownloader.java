package net.treset.mc_version_loader;

import net.treset.mc_version_loader.java.JavaFile;
import net.treset.mc_version_loader.version.VersionDownloads;
import net.treset.mc_version_loader.version.VersionLibrary;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileDownloader {
    private static final Logger LOGGER = Logger.getLogger(FileDownloader.class.toString());

    public static boolean downloadVersionDownload(VersionDownloads.Downloads download, File baseDir) {
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
        return downloadFile(downloadUrl, outFile);
    }

    public static boolean downloadVersionLibrary(VersionLibrary library, File baseDir) {
        if(library == null || library.getArtifactUrl() == null || library.getArtifactUrl().isBlank() || library.getArtifactPath() == null || library.getArtifactPath().isBlank() || baseDir == null || !baseDir.isDirectory()) {
            LOGGER.log(Level.WARNING, "Unable to start library download; unmet requirements");
            return false;
        }

        URL downloadUrl;
        try {
            downloadUrl = new URL(library.getArtifactUrl());
        } catch (MalformedURLException e) {
            LOGGER.log(Level.WARNING, "Unable to convert download url", e);
            return false;
        }

        File outDir = new File(baseDir, library.getArtifactPath().substring(0, library.getArtifactPath().lastIndexOf('/')));
        if(!outDir.isDirectory() && !outDir.mkdirs()) {
            LOGGER.log(Level.WARNING, "Unable to make required dirs");
            return false;
        }

        File outFile = new File(outDir, library.getArtifactPath().substring(library.getArtifactPath().lastIndexOf('/')));
        return downloadFile(downloadUrl, outFile);
    }

    public static boolean downloadJavaFile(JavaFile file, File baseDir) {
        if(file == null || file.getType() == null || file.getType().isBlank() || file.getName() == null || file.getName().isBlank() || (file.isFile() && (file.getRaw() == null || file.getRaw().getUrl() == null || file.getRaw().getUrl().isBlank())) || baseDir == null || !baseDir.isDirectory()) {
            LOGGER.log(Level.WARNING, "Unable to start java file download; unmet requirements");
            return false;
        }


        File outDir = null;
        if(file.isDir()) {
            outDir = new File(baseDir, file.getName());
        } else if(file.isFile()) {
            outDir = new File(baseDir, file.getName().substring(0, file.getName().lastIndexOf('/') == -1 ? file.getName().length() : file.getName().lastIndexOf('/')));
        }

        if (outDir == null || (!outDir.isDirectory() && !outDir.mkdirs())) {
            LOGGER.log(Level.WARNING, "Unable to make required dirs");
            return false;
        }

        if(file.isFile()) {
            URL downloadUrl;
            try {
                downloadUrl = new URL(file.getRaw().getUrl());
            } catch (MalformedURLException e) {
                LOGGER.log(Level.WARNING, "Unable to convert download url", e);
                return false;
            }

            File outFile = new File(outDir, file.getName().substring(file.getName().lastIndexOf('/') == -1 ? 0 : file.getName().lastIndexOf('/')));
            return downloadFile(downloadUrl, outFile);
        }
        return file.isDir();
    }

    private static boolean downloadFile(URL downloadUrl, File outFile) {
        try(FileOutputStream fileOutputStream = new FileOutputStream(outFile)) {

            ReadableByteChannel readableByteChannel = Channels.newChannel(downloadUrl.openStream());

            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch(IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to download file", e);
            return false;
        }
        return true;
    }
}
