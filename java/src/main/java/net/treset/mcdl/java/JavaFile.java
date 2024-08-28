package net.treset.mcdl.java;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.treset.mcdl.json.GenericJsonParsable;
import net.treset.mcdl.json.JsonUtils;
import net.treset.mcdl.json.SerializationException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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
