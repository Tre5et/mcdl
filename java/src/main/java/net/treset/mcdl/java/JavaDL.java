package net.treset.mcdl.java;

import net.treset.mcdl.exception.FileDownloadException;
import net.treset.mcdl.util.DownloadStatus;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

public class JavaDL {
    private static final String JAVA_RUNTIME_URL = "https://launchermeta.mojang.com/v1/products/java-runtime/2ec0cc96c44e5a76b9c8b7c39df7210883d12871/all.json";

    /**
     * Downloads all java files in a list to the specified directory while creating any necessary subdirectories.
     * @param files The list of files to download
     * @param baseDir The directory to download the files to
     * @throws FileDownloadException If there is an error downloading or writing a file
     */
    public static void downloadJavaFiles(List<JavaFile> files, File baseDir) throws FileDownloadException {
        downloadJavaFiles(files, baseDir, status -> {});
    }

    /**
     * Downloads all java files in a list to the specified directory while creating any necessary subdirectories.
     * @param files The list of files to download
     * @param baseDir The directory to download the files to
     * @param statusCallback A callback to be called when a file is downloaded
     * @throws FileDownloadException If there is an error downloading or writing a file
     */
    public static void downloadJavaFiles(List<JavaFile> files, File baseDir, Consumer<DownloadStatus> statusCallback) throws FileDownloadException {
        JavaFile.downloadAll(files, baseDir, statusCallback);
    }

    /**
     * Downloads a single java file to the specified directory while creating any necessary subdirectories.
     * @param file The file to download
     * @param baseDir The directory to download the file to
     * @throws FileDownloadException If there is an error downloading or writing the file
     */
    public static void downloadJavaFile(JavaFile file, File baseDir) throws FileDownloadException {
        file.download(baseDir);
    }

    /**
     * Gets a list of all available java runtimes.
     * @return A list of java runtimes
     * @throws FileDownloadException If there is an error downloading or parsing the runtimes
     */
    public static JavaRuntimes getJavaRuntimes() throws FileDownloadException {
        return JavaRuntimes.get();
    }

    /**
     * Gets the java files from the specified url.
     * @param url The url to get the java files from. Typically found in {@link JavaRuntimeRelease#getManifest()}
     * @return A list of java file objects
     * @throws FileDownloadException If there is an error downloading or parsing the file
     */
    public static List<JavaFile> getJavaFiles(String url) throws FileDownloadException {
        return JavaFile.getAll(url);
    }

    public static String getJavaRuntimeUrl() {
        return JAVA_RUNTIME_URL;
    }
}
