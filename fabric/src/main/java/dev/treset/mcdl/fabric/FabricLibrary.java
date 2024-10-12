package dev.treset.mcdl.fabric;

import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.util.DownloadStatus;
import dev.treset.mcdl.util.FileUtil;
import dev.treset.mcdl.util.MavenPom;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
     * Downloads a list of fabric libraries to a specified directory and returns a list of library paths.
     * @param libraries The libraries to download
     * @param baseDir The directory to download the libraries to
     * @param onStatus A callback to be called when a library is downloaded
     * @return A list of library paths
     * @throws FileDownloadException If there is an error downloading or writing a library
     */
    public static List<String> downloadAll(List<FabricLibrary> libraries, File baseDir, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        if (onStatus != null) {
            onStatus.accept(new DownloadStatus(0, libraries.size(), ""));
        }
        ArrayList<String> libraryPaths = new ArrayList<>();
        int size = libraries.size();
        int current = 0;
        for(FabricLibrary library : libraries) {
            if (onStatus != null) {
                onStatus.accept(new DownloadStatus(++current, size, library.getName()));
            }
            try {
                library.downloadAll(baseDir, libraryPaths);
            } catch (FileDownloadException e) {
                throw new FileDownloadException("Unable to download fabric library " + library, e);
            }
        }
        return libraryPaths;
    }

    /**
     * Downloads the library to a specified directory and adds it to a list of library paths.
     * @param baseDir The directory to download the library to
     * @param libraryPaths The list of library paths to add the library to
     * @throws FileDownloadException If there is an error downloading or writing the library
     */
    public void downloadAll(File baseDir, ArrayList<String> libraryPaths) throws FileDownloadException {
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
