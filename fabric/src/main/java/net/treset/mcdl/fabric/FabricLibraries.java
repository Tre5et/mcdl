package net.treset.mcdl.fabric;

import net.treset.mcdl.exception.FileDownloadException;
import net.treset.mcdl.util.DownloadStatus;

import java.io.File;
import java.util.ArrayList;
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
     * Downloads a list of fabric libraries to a specified directory and returns a list of library paths.
     * @param baseDir The directory to download the libraries to
     * @param libraries The libraries to download
     * @param onStatus A callback to be called when a library is downloaded
     * @return A list of library paths
     * @throws FileDownloadException If there is an error downloading or writing a library
     */
    public static List<String> download(File baseDir, List<FabricLibrary> libraries, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        onStatus.accept(new DownloadStatus(0, libraries.size(), ""));
        ArrayList<String> libraryPaths = new ArrayList<>();
        int size = libraries.size();
        int current = 0;
        for(FabricLibrary library : libraries) {
            onStatus.accept(new DownloadStatus(++current, size, library.getName()));
            try {
                library.download(baseDir, libraryPaths);
            } catch (FileDownloadException e) {
                throw new FileDownloadException("Unable to download fabric library " + library, e);
            }
        }
        return libraryPaths;
    }

    /**
     * Downloads the client libraries to a specified directory and returns a list of library paths.
     * @param baseDir The directory to download the libraries to
     * @param onStatus A callback to be called when a library is downloaded
     * @return A list of library paths
     * @throws FileDownloadException If there is an error downloading or writing a library
     */
    public List<String> downloadClient(File baseDir, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        return FabricMC.downloadFabricLibraries(baseDir, client, onStatus);
    }

    /**
     * Downloads the common libraries to a specified directory and returns a list of library paths.
     * @param baseDir The directory to download the libraries to
     * @param onStatus A callback to be called when a library is downloaded
     * @return A list of library paths
     * @throws FileDownloadException If there is an error downloading or writing a library
     */
    public List<String> downloadCommon(File baseDir, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        return FabricMC.downloadFabricLibraries(baseDir, common, onStatus);
    }

    /**
     * Downloads the server libraries to a specified directory and returns a list of library paths.
     * @param baseDir The directory to download the libraries to
     * @param onStatus A callback to be called when a library is downloaded
     * @return A list of library paths
     * @throws FileDownloadException If there is an error downloading or writing a library
     */
    public List<String> downloadServer(File baseDir, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        return FabricMC.downloadFabricLibraries(baseDir, server, onStatus);
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
