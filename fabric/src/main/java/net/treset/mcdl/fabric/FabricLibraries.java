package net.treset.mcdl.fabric;

import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.util.DownloadStatus;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

public class FabricLibraries {
    private List<FabricLibrary> client;
    private List<FabricLibrary> common;
    private List<FabricLibrary> server;

    public FabricLibraries(List<FabricLibrary> client, List<FabricLibrary> common, List<FabricLibrary> server) {
        this.client = client;
        this.common = common;
        this.server = server;
    }

    /**
     * Downloads the client libraries to a specified directory and returns a list of library paths.
     * @param baseDir The directory to download the libraries to
     * @param onStatus A callback to be called when a library is downloaded
     * @return A list of library paths
     * @throws FileDownloadException If there is an error downloading or writing a library
     */
    public List<String> downloadClient(File baseDir, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        return FabricLibrary.downloadAll(getClient(), baseDir, onStatus);
    }

    /**
     * Downloads the common libraries to a specified directory and returns a list of library paths.
     * @param baseDir The directory to download the libraries to
     * @param onStatus A callback to be called when a library is downloaded
     * @return A list of library paths
     * @throws FileDownloadException If there is an error downloading or writing a library
     */
    public List<String> downloadCommon(File baseDir, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        return FabricLibrary.downloadAll(getCommon(), baseDir, onStatus);
    }

    /**
     * Downloads the server libraries to a specified directory and returns a list of library paths.
     * @param baseDir The directory to download the libraries to
     * @param onStatus A callback to be called when a library is downloaded
     * @return A list of library paths
     * @throws FileDownloadException If there is an error downloading or writing a library
     */
    public List<String> downloadServer(File baseDir, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        return FabricLibrary.downloadAll(getServer(), baseDir, onStatus);
    }

    public List<FabricLibrary> getClient() {
        return client;
    }

    public void setClient(List<FabricLibrary> client) {
        this.client = client;
    }

    public List<FabricLibrary> getCommon() {
        return common;
    }

    public void setCommon(List<FabricLibrary> common) {
        this.common = common;
    }

    public List<FabricLibrary> getServer() {
        return server;
    }

    public void setServer(List<FabricLibrary> server) {
        this.server = server;
    }
}
