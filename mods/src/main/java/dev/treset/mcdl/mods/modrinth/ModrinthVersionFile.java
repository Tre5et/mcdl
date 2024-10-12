package dev.treset.mcdl.mods.modrinth;

public class ModrinthVersionFile {
    private String fileType;
    private String filename;
    private ModrinthHashes hashes;
    private boolean primary;
    private int size;
    private String url;

    public ModrinthVersionFile(String fileType, String filename, ModrinthHashes hashes, boolean primary, int size, String url) {
        this.fileType = fileType;
        this.filename = filename;
        this.hashes = hashes;
        this.primary = primary;
        this.size = size;
        this.url = url;
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

    public ModrinthHashes getHashes() {
        return hashes;
    }

    public void setHashes(ModrinthHashes hashes) {
        this.hashes = hashes;
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
