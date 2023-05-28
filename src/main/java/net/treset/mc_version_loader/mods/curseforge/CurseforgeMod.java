package net.treset.mc_version_loader.mods.curseforge;

import net.treset.mc_version_loader.VersionLoader;
import net.treset.mc_version_loader.format.FormatUtils;
import net.treset.mc_version_loader.json.GenericJsonParsable;
import net.treset.mc_version_loader.json.JsonUtils;
import net.treset.mc_version_loader.mods.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CurseforgeMod extends GenericModData {
    private boolean allowModDistribution;
    private List<CurseforgeAuthor> authors;
    private List<CurseforgeCategory> categories;
    private int classId;
    private String dateCreated;
    private String dateModified;
    private String dateReleased;
    private int downloadCount;
    private int gameId;
    private int gamePopularityRank;
    private int id;
    private boolean isAvailable;
    private boolean isFeatured;
    private List<CurseforgeFileIndex> latestEarlyAccessFilesIndex;
    private List<CurseforgeFile> latestFiles;
    private List<CurseforgeFileIndex> latestFilesIndex;
    private CurseforgeModLinks links;
    private CurseforgeImage logo;
    private int mainFileId;
    private String name;
    private List<CurseforgeImage> screenshots;
    private String slug;
    private int status;
    private String summary;
    private int thumbsUpCount;
    private List<ModVersionData> versions;

    public CurseforgeMod(boolean allowModDistribution, List<CurseforgeAuthor> authors, List<CurseforgeCategory> categories, int classId, String dateCreated, String dateModified, String dateReleased, int downloadCount, int gameId, int gamePopularityRank, int id, boolean isAvailable, boolean isFeatured, List<CurseforgeFileIndex> latestEarlyAccessFilesIndex, List<CurseforgeFile> latestFiles, List<CurseforgeFileIndex> latestFilesIndex, CurseforgeModLinks links, CurseforgeImage logo, int mainFileId, String name, List<CurseforgeImage> screenshots, String slug, int status, String summary, int thumbsUpCount) {
        this.allowModDistribution = allowModDistribution;
        this.authors = authors;
        this.categories = categories;
        this.classId = classId;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.dateReleased = dateReleased;
        this.downloadCount = downloadCount;
        this.gameId = gameId;
        this.gamePopularityRank = gamePopularityRank;
        this.id = id;
        this.isAvailable = isAvailable;
        this.isFeatured = isFeatured;
        this.latestEarlyAccessFilesIndex = latestEarlyAccessFilesIndex;
        this.latestFiles = latestFiles;
        this.latestFilesIndex = latestFilesIndex;
        this.links = links;
        this.logo = logo;
        this.mainFileId = mainFileId;
        this.name = name;
        this.screenshots = screenshots;
        this.slug = slug;
        this.status = status;
        this.summary = summary;
        this.thumbsUpCount = thumbsUpCount;
    }

    public static CurseforgeMod fromJson(String json) {
        if(json != null && json.startsWith("{\"data\":")) {
            json = json.substring(8);
            json = json.substring(0, json.length() - 1);
        }
        return GenericJsonParsable.fromJson(json, CurseforgeMod.class, JsonUtils.getGsonCamelCase());
    }

    @Override
    public List<String> getAuthors() {
        if(authors == null) {
            return null;
        }
        return authors.stream()
                .filter(a -> a != null && a.getName() != null && !a.getName().isBlank())
                .map(CurseforgeAuthor::getName).toList();
    }

    @Override
    public List<String> getCategories() {
        if(categories == null) {
            return null;
        }
        return categories.stream()
                .filter(c -> c != null && c.getName() != null && !c.getName().isBlank())
                .map(CurseforgeCategory::getName).toList();
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
        return summary;
    }

    @Override
    public int getDownloadsCount() {
        return downloadCount;
    }

    @Override
    public String getIconUrl() {
        return logo == null ? null : logo.getUrl();
    }

    @Override
    public List<String> getGameVersions() {
        if(latestFilesIndex == null) {
            return null;
        }
        return latestFilesIndex.stream()
                .filter(f -> f != null && f.getGameVersion() != null && !f.getGameVersion().isBlank())
                .map(CurseforgeFileIndex::getGameVersion).toList();
    }

    @Override
    public List<String> getModLoaders() {
        if(latestFilesIndex == null) {
            return null;
        }
        return latestFilesIndex.stream()
                .filter(f -> f != null && f.getModLoader() >= 0)
                .map(f -> FormatUtils.curseforgeModLoaderToModLoader(f.getModLoader()))
                .distinct().limit(2).toList();
    }

    @Override
    public String getSlug() {
        return slug;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<ModVersionData> getVersions() {
        return getVersions(null, -1);
    }

    @Override
    public List<ModVersionData> getVersions(String gameVersion, String modLoader) {
        return getVersions(gameVersion, FormatUtils.modLoaderToCurseforgeModLoader(modLoader));
    }

    @Override
    public List<ModVersionData> updateVersions() {
        return updateVersions(null, -1);
    }

    public List<ModVersionData> updateVersions(String gameVersion, int modLoader) {
        versions = List.copyOf(VersionLoader.getCurseforgeVersions(id, this, gameVersion, modLoader).getData());
        return versions;
    }

    public List<ModVersionData> getVersions(String gameVersion, int modLoader) {
        if(versions == null) {
            updateVersions(gameVersion, modLoader);
        }
        return versions;
    }

    public boolean isAllowModDistribution() {
        return allowModDistribution;
    }

    public void setAllowModDistribution(boolean allowModDistribution) {
        this.allowModDistribution = allowModDistribution;
    }

    public void setAuthors(List<CurseforgeAuthor> authors) {
        this.authors = authors;
    }

    public void setCategories(List<CurseforgeCategory> categories) {
        this.categories = categories;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getDateReleased() {
        return dateReleased;
    }

    public void setDateReleased(String dateReleased) {
        this.dateReleased = dateReleased;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getGamePopularityRank() {
        return gamePopularityRank;
    }

    public void setGamePopularityRank(int gamePopularityRank) {
        this.gamePopularityRank = gamePopularityRank;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(boolean featured) {
        isFeatured = featured;
    }

    public List<CurseforgeFileIndex> getLatestEarlyAccessFilesIndex() {
        return latestEarlyAccessFilesIndex;
    }

    public void setLatestEarlyAccessFilesIndex(List<CurseforgeFileIndex> latestEarlyAccessFilesIndex) {
        this.latestEarlyAccessFilesIndex = latestEarlyAccessFilesIndex;
    }

    public List<CurseforgeFile> getLatestFiles() {
        return latestFiles;
    }

    public void setLatestFiles(List<CurseforgeFile> latestFiles) {
        this.latestFiles = latestFiles;
    }

    public List<CurseforgeFileIndex> getLatestFilesIndex() {
        return latestFilesIndex;
    }

    public void setLatestFilesIndex(List<CurseforgeFileIndex> latestFilesIndex) {
        this.latestFilesIndex = latestFilesIndex;
    }

    public CurseforgeModLinks getLinks() {
        return links;
    }

    public void setLinks(CurseforgeModLinks links) {
        this.links = links;
    }

    public CurseforgeImage getLogo() {
        return logo;
    }

    public void setLogo(CurseforgeImage logo) {
        this.logo = logo;
    }

    public int getMainFileId() {
        return mainFileId;
    }

    public void setMainFileId(int mainFileId) {
        this.mainFileId = mainFileId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CurseforgeImage> getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(List<CurseforgeImage> screenshots) {
        this.screenshots = screenshots;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getThumbsUpCount() {
        return thumbsUpCount;
    }

    public void setThumbsUpCount(int thumbsUpCount) {
        this.thumbsUpCount = thumbsUpCount;
    }

    @Override
    public List<ModProvider> getModProviders() {
        return List.of(ModProvider.CURSEFORGE);
    }

    @Override
    public List<String> getProjectIds() {
        return List.of(String.valueOf(id));
    }
}
