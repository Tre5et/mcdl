package dev.treset.mcdl.quiltmc;

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

public class QuiltLibrary {
    private String name;
    private String url;

    public QuiltLibrary(String name, String url) {
        this.name = name;
        this.url = url;
    }

    /**
     * Downloads all the libraries to the specified directory and returns a list of library paths.
     * @param libraries The libraries to download
     * @param baseDir The directory to download the libraries to
     * @param onStatus A callback to report download status
     * @return A list of library paths
     * @throws FileDownloadException If there is an error downloading or writing a library
     */
    public static List<String> downloadAll(List<QuiltLibrary> libraries, File baseDir, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        if (onStatus != null) {
            onStatus.accept(new DownloadStatus(0, libraries.size(), ""));
        }
        ArrayList<String> currentLibraries = new ArrayList<>();
        int size = libraries.size();
        int current = 0;
        for(QuiltLibrary lib : libraries) {
            if (onStatus != null) {
                onStatus.accept(new DownloadStatus(++current, size, lib.getName()));
            }
            lib.download(baseDir, currentLibraries);
        }
        return currentLibraries;
    }

    /**
     * Downloads the library to the specified directory and adds the library path to the current libraries list.
     * @param baseDir The directory to download the library to
     * @param currentLibraries The list of current libraries
     * @throws FileDownloadException If there is an error downloading or writing the library
     */
    public void download(File baseDir, ArrayList<String> currentLibraries) throws FileDownloadException {
        if(getUrl() == null || getUrl().isBlank() || getName() == null || getName().isBlank() || baseDir == null || !baseDir.isDirectory()) {
            throw new FileDownloadException("Unmet requirements for quilt library download: library=" + getName());
        }

        MavenPom mavenPom;
        try {
            mavenPom = FileUtil.parseMavenUrl(getUrl(), getName(), QuiltDL.getCaching());
        } catch (MalformedURLException | FileDownloadException e) {
            throw new FileDownloadException("Unable to parse quilt library maven file Url: library=" + getName(), e);
        }

        String mavenUrl = mavenPom.getMavenUrl();
        if(mavenUrl == null || mavenUrl.isBlank()) {
            throw new FileDownloadException("Unable to parse quilt library maven file: library=" + getName());
        }

        URL downloadUrl;
        try {
            downloadUrl = new URL(getUrl() + mavenUrl);
        } catch (MalformedURLException e) {
            throw new FileDownloadException("Unable to parse quilt library download Url: library=" + getName(), e);
        }

        File libraryDir = new File(baseDir, mavenPom.getMavenDir());
        if(!libraryDir.isDirectory() && !libraryDir.mkdirs()) {
            throw new FileDownloadException("Unable to create library dir: library=" + getName());
        }

        File libraryFile = new File(libraryDir, mavenPom.getMavenFileName());

        FileUtil.downloadFile(downloadUrl, libraryFile, QuiltDL.getCaching());

        currentLibraries.add(mavenPom.getMavenDir() + mavenPom.getMavenFileName());
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
}
