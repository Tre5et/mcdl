package net.treset.mc_version_loader.files;

import net.treset.mc_version_loader.java.JavaFile;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaFileDownloader {
    private static final Logger LOGGER = Logger.getLogger(JavaFileDownloader.class.toString());

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
            return FileUtils.downloadFile(downloadUrl, outFile);
        }
        return file.isDir();
    }
}
