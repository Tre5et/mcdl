package dev.treset.mcdl.mods.curseforge;

public class CurseforgeCategory {
    private int classId;
    private String dateModified;
    private int gameId;
    private String iconUrl;
    private int id;
    private boolean isClass;
    private String name;
    private int parentCategoryId;
    private String slug;
    private String url;

    public CurseforgeCategory(int classId, String dateModified, int gameId, String iconUrl, int id, boolean isClass, String name, int parentCategoryId, String slug, String url) {
        this.classId = classId;
        this.dateModified = dateModified;
        this.gameId = gameId;
        this.iconUrl = iconUrl;
        this.id = id;
        this.isClass = isClass;
        this.name = name;
        this.parentCategoryId = parentCategoryId;
        this.slug = slug;
        this.url = url;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isClass() {
        return isClass;
    }

    public void setClass(boolean aClass) {
        isClass = aClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(int parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
