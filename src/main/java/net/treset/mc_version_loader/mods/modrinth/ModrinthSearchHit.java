package net.treset.mc_version_loader.mods.modrinth;

import net.treset.mc_version_loader.mods.GenericModData;
import net.treset.mc_version_loader.mods.ModVersionData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
    private String name;
    private List<String> versions;

    public ModrinthSearchHit(String author, List<String> categories, String clientSide, int color, String dateCreated, String dateModified, String description, List<String> displayCategories, int downloads, String featuredGallery, int follows, List<String> gallery, String iconUrl, String latestVersion, String license, String projectId, String projectType, String serverSide, String slug, String name, List<String> versions) {
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
        this.name = name;
        this.versions = versions;
    }

    // TODO
    @Override
    public List<String> getAuthors() {
        return null;
    }

    @Override
    public List<String> getCategories() {
        return null;
    }

    @Override
    public LocalDateTime getDateCreated() {
        return null;
    }

    @Override
    public LocalDateTime getDateModified() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public int getDownloadsCount() {
        return 0;
    }

    @Override
    public String getIconUrl() {
        return null;
    }

    @Override
    public List<String> getGameVersions() {
        return null;
    }

    @Override
    public List<String> getModLoaders() {
        return null;
    }

    @Override
    public String getSlug() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public List<ModVersionData> getVersions() {
        return null;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setVersions(List<String> versions) {
        this.versions = versions;
    }
}
