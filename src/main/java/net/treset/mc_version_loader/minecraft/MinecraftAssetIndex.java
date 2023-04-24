package net.treset.mc_version_loader.minecraft;

public class MinecraftAssetIndex {
    private String id;
    private String sha1;
    private int size;
    private int totalSize;
    private String url;

    public MinecraftAssetIndex(String id, String sha1, int size, int totalSize, String url) {
        this.id = id;
        this.sha1 = sha1;
        this.size = size;
        this.totalSize = totalSize;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
