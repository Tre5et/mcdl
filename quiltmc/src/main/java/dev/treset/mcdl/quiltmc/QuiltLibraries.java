package dev.treset.mcdl.quiltmc;

import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.util.DownloadStatus;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

public class QuiltLibraries {
    private List<QuiltLibrary> client;
    private List<QuiltLibrary> common;
    private List<QuiltLibrary> server;
    private List<QuiltLibrary> development;

    public QuiltLibraries(List<QuiltLibrary> client, List<QuiltLibrary> common, List<QuiltLibrary> server, List<QuiltLibrary> development) {
        this.client = client;
        this.common = common;
        this.server = server;
        this.development = development;
    }

    /**
     * Downloads all the client libraries to the specified directory and returns a list of library paths.
     * @param baseDir The directory to download the libraries to
     * @param onStatus A callback to report download status
     * @return A list of library paths
     * @throws FileDownloadException If there is an error downloading or writing a library
     */
    public List<String> downloadClient(File baseDir, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        return QuiltLibrary.downloadAll(getClient(), baseDir, onStatus);
    }

    /**
     * Downloads all the common libraries to the specified directory and returns a list of library paths.
     * @param baseDir The directory to download the libraries to
     * @param onStatus A callback to report download status
     * @return A list of library paths
     * @throws FileDownloadException If there is an error downloading or writing a library
     */
    public List<String> downloadCommon(File baseDir, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        return QuiltLibrary.downloadAll(getCommon(), baseDir, onStatus);
    }

    /**
     * Downloads all the server libraries to the specified directory and returns a list of library paths.
     * @param baseDir The directory to download the libraries to
     * @param onStatus A callback to report download status
     * @return A list of library paths
     * @throws FileDownloadException If there is an error downloading or writing a library
     */
    public List<String> downloadServer(File baseDir, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        return QuiltLibrary.downloadAll(getServer(), baseDir, onStatus);
    }

    public List<QuiltLibrary> getClient() {
        return client;
    }

    public void setClient(List<QuiltLibrary> client) {
        this.client = client;
    }

    public List<QuiltLibrary> getCommon() {
        return common;
    }

    public void setCommon(List<QuiltLibrary> common) {
        this.common = common;
    }

    public List<QuiltLibrary> getServer() {
        return server;
    }

    public void setServer(List<QuiltLibrary> server) {
        this.server = server;
    }

    public List<QuiltLibrary> getDevelopment() {
        return development;
    }

    public void setDevelopment(List<QuiltLibrary> development) {
        this.development = development;
    }
}
