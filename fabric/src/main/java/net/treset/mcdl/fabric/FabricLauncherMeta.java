package net.treset.mcdl.fabric;

import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.util.DownloadStatus;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

public class FabricLauncherMeta {
    private FabricLibraries libraries;
    private int version;

    public FabricLauncherMeta(FabricLibraries libraries, int version) {
        this.libraries = libraries;
        this.version = version;
    }

    /**
     * Downloads the client libraries to a specified directory and returns a list of library paths.
     * @param baseDir The directory to download the libraries to
     * @param onStatus A callback to be called when a library is downloaded
     * @return A list of library paths
     * @throws FileDownloadException If there is an error downloading or writing a library
     */
    public List<String> downloadClientLibraries(File baseDir, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        return libraries.downloadClient(baseDir, onStatus);
    }

    /**
     * Downloads the common libraries to a specified directory and returns a list of library paths.
     * @param baseDir The directory to download the libraries to
     * @param onStatus A callback to be called when a library is downloaded
     * @return A list of library paths
     * @throws FileDownloadException If there is an error downloading or writing a library
     */
    public List<String> downloadCommonLibraries(File baseDir, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        return libraries.downloadCommon(baseDir, onStatus);
    }

    /**
     * Downloads the server libraries to a specified directory and returns a list of library paths.
     * @param baseDir The directory to download the libraries to
     * @param onStatus A callback to be called when a library is downloaded
     * @return A list of library paths
     * @throws FileDownloadException If there is an error downloading or writing a library
     */
    public List<String> downloadServerLibraries(File baseDir, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        return libraries.downloadServer(baseDir, onStatus);
    }

    public FabricLibraries getLibraries() {
        return libraries;
    }

    public void setLibraries(FabricLibraries libraries) {
        this.libraries = libraries;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
