package net.treset.mcdl.mods.curseforge;

public class CurseforgeImage {
    private String description;
    private int id;
    private int modId;
    private String thumbnailUrl;
    private String title;
    private String url;

    public CurseforgeImage(String description, int id, int modId, String thumbnailUrl, String title, String url) {
        this.description = description;
        this.id = id;
        this.modId = modId;
        this.thumbnailUrl = thumbnailUrl;
        this.title = title;
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getModId() {
        return modId;
    }

    public void setModId(int modId) {
        this.modId = modId;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
