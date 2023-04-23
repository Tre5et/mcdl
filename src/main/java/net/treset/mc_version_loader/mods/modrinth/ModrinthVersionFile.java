package net.treset.mc_version_loader.mods.modrinth;

import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;

public class ModrinthVersionFile {
    private String fileType;
    private String filename;
    private String hashesSha1;
    private String hashesSha512;
    private boolean primary;
    private int size;
    private String url;

    public ModrinthVersionFile(String fileType, String filename, String hashesSha1, String hashesSha512, boolean primary, int size, String url) {
        this.fileType = fileType;
        this.filename = filename;
        this.hashesSha1 = hashesSha1;
        this.hashesSha512 = hashesSha512;
        this.primary = primary;
        this.size = size;
        this.url = url;
    }

    public static ModrinthVersionFile fromJson(JsonObject fileObj) {
        JsonObject hashesObj = JsonUtils.getAsJsonObject(fileObj, "hashes");
        return new ModrinthVersionFile(
            JsonUtils.getAsString(fileObj, "file_type"),
            JsonUtils.getAsString(fileObj, "filename"),
            JsonUtils.getAsString(hashesObj, "sha1"),
            JsonUtils.getAsString(hashesObj, "sha512"),
            JsonUtils.getAsBoolean(fileObj, "primary"),
            JsonUtils.getAsInt(fileObj, "size"),
            JsonUtils.getAsString(fileObj, "url")
        );
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getHashesSha1() {
        return hashesSha1;
    }

    public void setHashesSha1(String hashesSha1) {
        this.hashesSha1 = hashesSha1;
    }

    public String getHashesSha512() {
        return hashesSha512;
    }

    public void setHashesSha512(String hashesSha512) {
        this.hashesSha512 = hashesSha512;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
