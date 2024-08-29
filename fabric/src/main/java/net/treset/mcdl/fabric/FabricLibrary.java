package net.treset.mcdl.fabric;

import net.treset.mcdl.exception.FileDownloadException;
import net.treset.mcdl.util.FileUtil;
import net.treset.mcdl.util.MavenPom;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class FabricLibrary {
    private String name;
    private String url;
    private String localPath;
    private String localFileName;

    public FabricLibrary(String name, String url) {
        this.name = name;
        this.url = url;
    }

    /**
     * Downloads the library to a specified directory and adds it to a list of library paths.
     * @param baseDir The directory to download the library to
     * @param libraryPaths The list of library paths to add the library to
     * @throws FileDownloadException If there is an error downloading or writing the library
     */
    public void download(File baseDir, ArrayList<String> libraryPaths) throws FileDownloadException {
        if(getUrl() == null || getUrl().isBlank() || getName() == null || getName().isBlank() || baseDir == null || !baseDir.isDirectory()) {
            throw new FileDownloadException("Unmet requirements for fabric library download: library=" + getName());
        }

        MavenPom mavenPom;
        try {
            mavenPom = FileUtil.parseMavenUrl(getUrl(), getName());
        } catch (MalformedURLException | FileDownloadException e) {
            throw new FileDownloadException("Unable to parse fabric library maven file Url: library=" + getName(), e);
        }

        String mavenUrl = mavenPom.getMavenUrl();
        if(mavenUrl == null || mavenUrl.isBlank()) {
            throw new FileDownloadException("Unable to parse fabric library maven file: library=" + getName());
        }

        URL downloadUrl;
        try {
            downloadUrl = new URL(getUrl() + mavenUrl);
        } catch (MalformedURLException e) {
            throw new FileDownloadException("Unable to parse fabric library download Url: library=" + getName(), e);
        }

        File libraryDir = new File(baseDir, mavenPom.getMavenDir());
        if(!libraryDir.isDirectory() && !libraryDir.mkdirs()) {
            throw new FileDownloadException("Unable to create library dir: library=" + getName());
        }

        File libraryFile = new File(libraryDir, mavenPom.getMavenFileName());

        setLocalPath(mavenPom.getMavenDir());
        setLocalFileName(mavenPom.getMavenFileName());

        libraryPaths.add(getLocalPath() + getLocalFileName());

        FileUtil.downloadFile(downloadUrl, libraryFile);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getLocalFileName() {
        return localFileName;
    }

    public void setLocalFileName(String localFileName) {
        this.localFileName = localFileName;
    }

    @Override
    public String toString() {
        return getName();
    }
}
