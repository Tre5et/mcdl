package net.treset.mcdl.mods.modrinth;

public class ModrinthGalleryImage {
    private String url;
    private boolean featured;
    private String title;
    private String description;
    private String created;
    private int ordering;

    public ModrinthGalleryImage(String url, boolean featured, String title, String description, String created, int ordering) {
        this.url = url;
        this.featured = featured;
        this.title = title;
        this.description = description;
        this.created = created;
        this.ordering = ordering;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getOrdering() {
        return ordering;
    }

    public void setOrdering(int ordering) {
        this.ordering = ordering;
    }
}
