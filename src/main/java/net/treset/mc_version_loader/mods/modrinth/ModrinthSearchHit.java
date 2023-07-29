package net.treset.mc_version_loader.mods.modrinth;

import net.treset.mc_version_loader.exception.FileDownloadException;
import net.treset.mc_version_loader.format.FormatUtils;
import net.treset.mc_version_loader.mods.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ModrinthSearchHit extends GenericModData {
    private String author;
    private List<String> categories;
    private String clientSide;
    private int color;
    private String dateCreated;
    private String dateModified;
    private String description;
    private List<String> displayCategories;
    private int downloads;
    private String featuredGallery;
    private int follows;
    private List<String> gallery;
    private String iconUrl;
    private String latestVersion;
    private String license;
    private String projectId;
    private String projectType;
    private String serverSide;
    private String slug;
    private String title;
    private List<String> versions;
    private transient List<ModVersionData> versionData;

    public ModrinthSearchHit(String author, List<String> categories, String clientSide, int color, String dateCreated, String dateModified, String description, List<String> displayCategories, int downloads, String featuredGallery, int follows, List<String> gallery, String iconUrl, String latestVersion, String license, String projectId, String projectType, String serverSide, String slug, String title, List<String> versions) {
        this.author = author;
        this.categories = categories;
        this.clientSide = clientSide;
        this.color = color;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.description = description;
        this.displayCategories = displayCategories;
        this.downloads = downloads;
        this.featuredGallery = featuredGallery;
        this.follows = follows;
        this.gallery = gallery;
        this.iconUrl = iconUrl;
        this.latestVersion = latestVersion;
        this.license = license;
        this.projectId = projectId;
        this.projectType = projectType;
        this.serverSide = serverSide;
        this.slug = slug;
        this.title = title;
        this.versions = versions;
    }

    @Override
    public List<String> getAuthors() {
        return List.of(author);
    }

    @Override
    public List<String> getCategories() {
        return categories;
    }

    @Override
    public LocalDateTime getDateCreated() {
        return FormatUtils.parseLocalDateTime(dateCreated);
    }

    @Override
    public LocalDateTime getDateModified() {
        return FormatUtils.parseLocalDateTime(dateModified);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getDownloadsCount() {
        return downloads;
    }

    @Override
    public String getIconUrl() {
        return iconUrl;
    }

    @Override
    public List<String> getGameVersions() {
        return versions;
    }

    @Override
    public List<String> getModLoaders() {
        if(categories == null) {
            return null;
        }
        List<String> out = new ArrayList<>();
        for(String c : categories) {
            if(out.size() >= 2) {
                break;
            }
            if(c != null && (c.equals("fabric") || c.equals("forge"))) {
                out.add(c);
            }
        }
        return out;
    }

    @Override
    public String getSlug() {
        return slug;
    }

    @Override
    public String getName() {
        return title;
    }

    @Override
    public String getUrl() {
        return "https://modrinth.com/project/" + projectId;
    }

    @Override
    public List<ModVersionData> getVersions() throws FileDownloadException {
        return getVersions(null, null);
    }

    @Override
    public List<ModVersionData> getVersions(String gameVersion, String modLoader) throws FileDownloadException {
        if(versionData == null) {
            updateVersions(gameVersion, modLoader);
        }
        return versionData;
    }

    @Override
    public List<ModVersionData> updateVersions() throws FileDownloadException {
        return updateVersions(null, null);
    }

    public List<ModVersionData> updateVersions(String gameVersion, String modLoader) throws FileDownloadException {
        versionData = List.copyOf(ModUtil.getModrinthVersion(projectId, this, gameVersion == null ? null : List.of(gameVersion), modLoader == null ? null : List.of(modLoader)));
        return versionData;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getClientSide() {
        return clientSide;
    }

    public void setClientSide(String clientSide) {
        this.clientSide = clientSide;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getDisplayCategories() {
        return displayCategories;
    }

    public void setDisplayCategories(List<String> displayCategories) {
        this.displayCategories = displayCategories;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public String getFeaturedGallery() {
        return featuredGallery;
    }

    public void setFeaturedGallery(String featuredGallery) {
        this.featuredGallery = featuredGallery;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public List<String> getGallery() {
        return gallery;
    }

    public void setGallery(List<String> gallery) {
        this.gallery = gallery;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getServerSide() {
        return serverSide;
    }

    public void setServerSide(String serverSide) {
        this.serverSide = serverSide;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVersions(List<String> versions) {
        this.versions = versions;
    }

    @Override
    public List<ModProvider> getModProviders() {
        return List.of(ModProvider.MODRINTH);
    }

    @Override
    public List<String> getProjectIds() {
        return List.of(projectId);
    }
}
