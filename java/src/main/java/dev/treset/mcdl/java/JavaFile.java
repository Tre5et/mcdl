package dev.treset.mcdl.java;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.json.GenericJsonParsable;
import dev.treset.mcdl.json.JsonUtils;
import dev.treset.mcdl.json.SerializationException;
import dev.treset.mcdl.util.DownloadStatus;
import dev.treset.mcdl.util.FileUtil;
import dev.treset.mcdl.util.HttpUtil;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

public class JavaFile extends GenericJsonParsable {
    private transient String name;
    private boolean executable;
    private String type;
    private JavaDownload lzma;
    private JavaDownload raw;

    public JavaFile(String name, boolean executable, String type, JavaDownload lzma, JavaDownload raw) {
        this.name = name;
        this.executable = executable;
        this.type = type;
        this.lzma = lzma;
        this.raw = raw;
    }

    public static List<JavaFile> fromJson(String jsonManifest) throws SerializationException {
        JsonObject manifestObj = JsonUtils.getAsJsonObject(JsonUtils.parseJson(jsonManifest));
        JsonObject filesObj = JsonUtils.getAsJsonObject(manifestObj, "files");
        Set<Map.Entry<String, JsonElement>> files = JsonUtils.getMembers(filesObj);
        if(files != null) {
            return files.stream().map(f -> {
                try {
                    return JavaFile.fromJsonObject(f.getKey(), JsonUtils.getAsJsonObject(f.getValue()));
                } catch (SerializationException e) {
                    return null;
                }
            }).toList();
        }
        return List.of();
    }

    public static JavaFile fromJsonObject(String name, JsonObject jsonObject) throws SerializationException {
        JavaFile file = JsonUtils.getGson().fromJson(jsonObject, JavaFile.class);
        JsonObject downloadsObj = JsonUtils.getAsJsonObject(jsonObject, "downloads");
        file.setName(name);
        file.setLzma(JavaDownload.fromJsonObject(JsonUtils.getAsJsonObject(downloadsObj, "lzma")));
        file.setRaw(JavaDownload.fromJsonObject(JsonUtils.getAsJsonObject(downloadsObj, "raw")));
        return file;
    }

    /**
     * Gets all java files from the specified URL
     * @param url The URL to get the files from
     * @return A list of java files
     * @throws FileDownloadException If there is an error downloading or parsing the files
     */
    public static List<JavaFile> getAll(String url) throws FileDownloadException {
        try {
            return JavaFile.fromJson(HttpUtil.getString(url));
        } catch (SerializationException e) {
            throw new FileDownloadException("Failed to parse java file", e);
        } catch (IOException e) {
            throw new FileDownloadException("Failed to download java file", e);
        }
    }

    /**
     * Downloads all the files to the specified directory
     * @param files The files to download
     * @param baseDir The directory to download the files to
     * @param onStatus The status callback
     * @throws FileDownloadException If there is an error downloading or writing a file
     */
    public static void downloadAll(List<JavaFile> files, File baseDir, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        if(!baseDir.isDirectory() || files == null || files.isEmpty()) {
            throw new FileDownloadException("Unmet requirements for java download");
        }

        int size = files.size();
        int current = 0;
        for(JavaFile file : files) {
            if (onStatus != null) {
                onStatus.accept(new DownloadStatus(++current, size, file.getName()));
            }
            try {
                file.download(baseDir);
            } catch (FileDownloadException e) {
                throw new FileDownloadException("Failed to download java files", e);
            }
        }
    }

    /**
     * Downloads the file to the specified directory
     * @param baseDir The directory to download the file to
     * @throws FileDownloadException If there is an error downloading or writing the file
     */
    public void download(File baseDir) throws FileDownloadException {
        if(getType() == null || getType().isBlank() || getName() == null || getName().isBlank() || (isFile() && (getRaw() == null || getRaw().getUrl() == null || getRaw().getUrl().isBlank())) || baseDir == null || !baseDir.isDirectory()) {
            throw new FileDownloadException("Unmet requirements for java file download: file=" + getName());
        }

        File outDir = null;
        if(isDir()) {
            outDir = new File(baseDir, getName());
        } else if(isFile()) {
            outDir = new File(baseDir, getName().substring(0, getName().lastIndexOf('/') == -1 ? getName().length() : getName().lastIndexOf('/')));
        }

        if (outDir == null || (!outDir.isDirectory() && !outDir.mkdirs())) {
            throw new FileDownloadException("Unable to make required dirs for file download: file=" + getName());
        }

        if(isDir()) {
            return;
        }
        if(isFile()) {
            URL downloadUrl;
            try {
                downloadUrl = new URL(getRaw().getUrl());
            } catch (MalformedURLException e) {
                throw new FileDownloadException("Unable to convert download url: file=" + getName(), e);
            }

            File outFile = new File(outDir, getName().substring(getName().lastIndexOf('/') == -1 ? 0 : getName().lastIndexOf('/')));
            FileUtil.downloadFile(downloadUrl, outFile);
            return;
        }
        throw new FileDownloadException("Unable to determine file type: file=" + getName());
    }

    public boolean isFile() {
        return Objects.equals(getType(), "file");
    }

    public boolean isDir() {
        return Objects.equals(getType(), "directory");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isExecutable() {
        return executable;
    }

    public void setExecutable(boolean executable) {
        this.executable = executable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JavaDownload getLzma() {
        return lzma;
    }

    public void setLzma(JavaDownload lzma) {
        this.lzma = lzma;
    }

    public JavaDownload getRaw() {
        return raw;
    }

    public void setRaw(JavaDownload raw) {
        this.raw = raw;
    }
}
