package net.treset.version;

public class VersionAssetIndex {
    private String id;
    private String cha1;
    private String size;
    private String totalSize;
    private String url;

    public VersionAssetIndex(String id, String cha1, String size, String totalSize, String url) {
        this.id = id;
        this.cha1 = cha1;
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

    public String getCha1() {
        return cha1;
    }

    public void setCha1(String cha1) {
        this.cha1 = cha1;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(String totalSize) {
        this.totalSize = totalSize;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
