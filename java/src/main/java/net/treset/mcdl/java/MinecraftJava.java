package net.treset.mcdl.java;

import net.treset.mcdl.exception.FileDownloadException;
import net.treset.mcdl.json.SerializationException;
import net.treset.mcdl.util.DownloadStatus;
import net.treset.mcdl.util.FileUtil;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MinecraftJava {
    private static final String JAVA_RUNTIME_URL = "https://launchermeta.mojang.com/v1/products/java-runtime/2ec0cc96c44e5a76b9c8b7c39df7210883d12871/all.json";

    /**
     * Downloads all java files in a list to the specified directory while creating any necessary subdirectories.
     * @param baseDir The directory to download the files to
     * @param files The list of files to download
     * @throws FileDownloadException If there is an error downloading or writing a file
     */
    public static void downloadJavaFiles(File baseDir, List<JavaFile> files) throws FileDownloadException {
        downloadJavaFiles(baseDir, files, status -> {});
    }

    /**
     * Downloads all java files in a list to the specified directory while creating any necessary subdirectories.
     * @param baseDir The directory to download the files to
     * @param files The list of files to download
     * @param statusCallback A callback to be called when a file is downloaded
     * @throws FileDownloadException If there is an error downloading or writing a file
     */
    public static void downloadJavaFiles(File baseDir, List<JavaFile> files, Consumer<DownloadStatus> statusCallback) throws FileDownloadException {
        if(!baseDir.isDirectory() || files == null || files.isEmpty()) {
            throw new FileDownloadException("Unmet requirements for java download");
        }

        List<Exception> exceptionQueue = new ArrayList<>();
        int size = files.size();
        int current = 0;
        boolean failed = false;
        for(JavaFile file : files) {
            statusCallback.accept(new DownloadStatus(++current, size, file.getName(), failed));
            try {
                downloadJavaFile(baseDir, file);
            } catch (FileDownloadException e) {
                exceptionQueue.add(e);
                failed = true;
            }
        }
        if(!exceptionQueue.isEmpty()) {
            throw new FileDownloadException("Failed to download " + exceptionQueue.size() + " java files", exceptionQueue.get(0));
        }

    }

    /**
     * Downloads a single java file to the specified directory while creating any necessary subdirectories.
     * @param baseDir The directory to download the file to
     * @param file The file to download
     * @throws FileDownloadException If there is an error downloading or writing the file
     */
    public static void downloadJavaFile(File baseDir, JavaFile file) throws FileDownloadException {
        if(file == null || file.getType() == null || file.getType().isBlank() || file.getName() == null || file.getName().isBlank() || (file.isFile() && (file.getRaw() == null || file.getRaw().getUrl() == null || file.getRaw().getUrl().isBlank())) || baseDir == null || !baseDir.isDirectory()) {
            throw new FileDownloadException("Unmet requirements for java file download: file=" + file);
        }

        File outDir = null;
        if(file.isDir()) {
            outDir = new File(baseDir, file.getName());
        } else if(file.isFile()) {
            outDir = new File(baseDir, file.getName().substring(0, file.getName().lastIndexOf('/') == -1 ? file.getName().length() : file.getName().lastIndexOf('/')));
        }

        if (outDir == null || (!outDir.isDirectory() && !outDir.mkdirs())) {
            throw new FileDownloadException("Unable to make required dirs for file download: file=" + file.getName());
        }

        if(file.isDir()) {
            return;
        }
        if(file.isFile()) {
            URL downloadUrl;
            try {
                downloadUrl = new URL(file.getRaw().getUrl());
            } catch (MalformedURLException e) {
                throw new FileDownloadException("Unable to convert download url: file=" + file.getName(), e);
            }

            File outFile = new File(outDir, file.getName().substring(file.getName().lastIndexOf('/') == -1 ? 0 : file.getName().lastIndexOf('/')));
            FileUtil.downloadFile(downloadUrl, outFile);
            return;
        }
        throw new FileDownloadException("Unable to determine file type: file=" + file.getName());
    }

    /**
     * Gets a list of all available java runtimes.
     * @return A list of java runtimes
     * @throws FileDownloadException If there is an error downloading or parsing the runtimes
     */
    public static JavaRuntimes getJavaRuntimes() throws FileDownloadException {
        try {
            return JavaRuntimes.fromJson(FileUtil.getStringFromUrl(getJavaRuntimeUrl()));
        } catch (SerializationException e) {
            throw new FileDownloadException("Failed to parse runtimes file", e);
        }
    }

    /**
     * Gets the java files from the specified url.
     * @param url The url to get the java files from. Typically found in {@link JavaRuntimeRelease#getManifest()}
     * @return A list of java file objects
     * @throws FileDownloadException If there is an error downloading or parsing the file
     */
    public static List<JavaFile> getJavaFiles(String url) throws FileDownloadException {
        try {
            return JavaFile.fromJson(FileUtil.getStringFromUrl(url));
        } catch (SerializationException e) {
            throw new FileDownloadException("Failed to parse java file", e);
        }
    }

    public static String getJavaRuntimeUrl() {
        return JAVA_RUNTIME_URL;
    }
}
