package net.treset.mc_version_loader.mods.modrinth;

import net.treset.mc_version_loader.mods.GenericModVersion;
import net.treset.mc_version_loader.mods.ModData;

import java.time.LocalDateTime;
import java.util.List;

public class ModrinthVersion {
    private String authorId;
    private String changelog;
    private String changelogUrl;
    private String datePublished;
    private List<ModrinthVersionDependency> dependencies;
    private int downloads;
    private boolean featured;
    private List<ModrinthVersionFile> files;
    private List<String> gameVersions;
    private String id;
    private List<String> loaders;
    private String name;
    private String projectId;
    private String requestedStatus;
    private String status;
    private String versionNumber;
    private String versionType;

    public ModrinthVersion(String authorId, String changelog, String changelogUrl, String datePublished, List<ModrinthVersionDependency> dependencies, int downloads, boolean featured, List<ModrinthVersionFile> files, List<String> gameVersions, String id, List<String> loaders, String name, String projectId, String requestedStatus, String status, String versionNumber, String versionType) {
        this.authorId = authorId;
        this.changelog = changelog;
        this.changelogUrl = changelogUrl;
        this.datePublished = datePublished;
        this.dependencies = dependencies;
        this.downloads = downloads;
        this.featured = featured;
        this.files = files;
        this.gameVersions = gameVersions;
        this.id = id;
        this.loaders = loaders;
        this.name = name;
        this.projectId = projectId;
        this.requestedStatus = requestedStatus;
        this.status = status;
        this.versionNumber = versionNumber;
        this.versionType = versionType;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getChangelog() {
        return changelog;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }

    public String getChangelogUrl() {
        return changelogUrl;
    }

    public void setChangelogUrl(String changelogUrl) {
        this.changelogUrl = changelogUrl;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    public List<ModrinthVersionDependency> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<ModrinthVersionDependency> dependencies) {
        this.dependencies = dependencies;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public List<ModrinthVersionFile> getFiles() {
        return files;
    }

    public void setFiles(List<ModrinthVersionFile> files) {
        this.files = files;
    }

    public List<String> getGameVersions() {
        return gameVersions;
    }

    public void setGameVersions(List<String> gameVersions) {
        this.gameVersions = gameVersions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getLoaders() {
        return loaders;
    }

    public void setLoaders(List<String> loaders) {
        this.loaders = loaders;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getRequestedStatus() {
        return requestedStatus;
    }

    public void setRequestedStatus(String requestedStatus) {
        this.requestedStatus = requestedStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getVersionType() {
        return versionType;
    }

    public void setVersionType(String versionType) {
        this.versionType = versionType;
    }
}
