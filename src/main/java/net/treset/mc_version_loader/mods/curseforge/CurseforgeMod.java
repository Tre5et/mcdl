package net.treset.mc_version_loader.mods.curseforge;

import com.google.gson.JsonObject;
import net.treset.mc_version_loader.VersionLoader;
import net.treset.mc_version_loader.format.FormatUtils;
import net.treset.mc_version_loader.json.JsonUtils;
import net.treset.mc_version_loader.mods.GenericModData;
import net.treset.mc_version_loader.mods.ModData;
import net.treset.mc_version_loader.mods.ModVersionData;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

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
    private String linksIssuesUrl;
    private String linksSourceUrl;
    private String linksWebsiteUrl;
    private String wikiUrl;
    private CurseforgeImage logo;
    private int mainFileId;
    private String name;
    private List<CurseforgeImage> screenshots;
    private String slug;
    private int status;
    private String summary;
    private int thumbsUpCount;

    public CurseforgeMod(boolean allowModDistribution, List<CurseforgeAuthor> authors, List<CurseforgeCategory> categories, int classId, String dateCreated, String dateModified, String dateReleased, int downloadCount, int gameId, int gamePopularityRank, int id, boolean isAvailable, boolean isFeatured, List<CurseforgeFileIndex> latestEarlyAccessFilesIndex, List<CurseforgeFile> latestFiles, List<CurseforgeFileIndex> latestFilesIndex, String linksIssuesUrl, String linksSourceUrl, String linksWebsiteUrl, String wikiUrl, CurseforgeImage logo, int mainFileId, String name, List<CurseforgeImage> screenshots, String slug, int status, String summary, int thumbsUpCount) {
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
        this.linksIssuesUrl = linksIssuesUrl;
        this.linksSourceUrl = linksSourceUrl;
        this.linksWebsiteUrl = linksWebsiteUrl;
        this.wikiUrl = wikiUrl;
        this.logo = logo;
        this.mainFileId = mainFileId;
        this.name = name;
        this.screenshots = screenshots;
        this.slug = slug;
        this.status = status;
        this.summary = summary;
        this.thumbsUpCount = thumbsUpCount;
    }

    public static CurseforgeMod fromJson(JsonObject modObj) {
        JsonObject linksObj = JsonUtils.getAsJsonObject(modObj, "links");
        CurseforgeMod mod = new CurseforgeMod(
                JsonUtils.getAsBoolean(modObj, "allowModDistribution"),
                CurseforgeAuthor.parseCurseforgeAuthors(JsonUtils.getAsJsonArray(modObj, "authors")),
                CurseforgeCategory.parseCurseforgeCategories(JsonUtils.getAsJsonArray(modObj, "categories")),
                JsonUtils.getAsInt(modObj, "classId"),
                JsonUtils.getAsString(modObj, "dateCreated"),
                JsonUtils.getAsString(modObj, "dateModified"),
                JsonUtils.getAsString(modObj, "dateReleased"),
                JsonUtils.getAsInt(modObj, "downloadCount"),
                JsonUtils.getAsInt(modObj, "gameId"),
                JsonUtils.getAsInt(modObj, "gamePopularityRank"),
                JsonUtils.getAsInt(modObj, "id"),
                JsonUtils.getAsBoolean(modObj, "isAvailable"),
                JsonUtils.getAsBoolean(modObj, "isFeatured"),
                CurseforgeFileIndex.parseCurseforgeFileIndexes(JsonUtils.getAsJsonArray(modObj, "latestEarlyAccessFilesIndexes")),
                CurseforgeFile.parseCurseforgeFiles(JsonUtils.getAsJsonArray(modObj, "latestFiles"), null),
                CurseforgeFileIndex.parseCurseforgeFileIndexes(JsonUtils.getAsJsonArray(modObj, "latestFilesIndexes")),
                JsonUtils.getAsString(linksObj, "issuesUrl"),
                JsonUtils.getAsString(linksObj, "sourceUrl"),
                JsonUtils.getAsString(linksObj, "websiteUrl"),
                JsonUtils.getAsString(linksObj, "wikiUrl"),
                CurseforgeImage.fromJson(JsonUtils.getAsJsonObject(modObj, "logo")),
                JsonUtils.getAsInt(modObj, "mainFieldId"),
                JsonUtils.getAsString(modObj, "name"),
                CurseforgeImage.parseCurseforgeImages(JsonUtils.getAsJsonArray(modObj, "screenshots")),
                JsonUtils.getAsString(modObj, "slug"),
                JsonUtils.getAsInt(modObj, "status"),
                JsonUtils.getAsString(modObj, "summary"),
                JsonUtils.getAsInt(modObj, "thumbsUpCount")
        );
        if(mod.getLatestFiles() != null) {
            for(CurseforgeFile i : mod.getLatestFiles()) {
                i.setParentMod(mod);
            }
        }
        return mod;
    }


    @Override
    public List<String> getAuthors() {
        if(authors == null) {
            return null;
        }
        List<String> out = new ArrayList<>();
        for(CurseforgeAuthor a : authors) {
            if(a != null && a.getName() != null && !a.getName().isBlank()) {
                out.add(a.getName());
            }
        }
        return out;
    }

    @Override
    public List<String> getCategories() {
        if(categories == null) {
            return null;
        }
        List<String> out = new ArrayList<>();
        for(CurseforgeCategory c : categories) {
            if(c != null && c.getName() != null && !c.getName().isBlank()) {
                out.add(c.getName());
            }
        }
        return out;
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
        List<String> out = new ArrayList<>();
        for(CurseforgeFileIndex f : latestFilesIndex) {
            if(f != null && f.getGameVersion() != null && !f.getGameVersion().isBlank()) {
                out.add(f.getGameVersion());
            }
        }
        return out;
    }

    @Override
    public List<String> getModLoaders() {
        if(latestFilesIndex == null) {
            return null;
        }
        List<String> out = new ArrayList<>();
        for(CurseforgeFileIndex f : latestFilesIndex) {
            if(f != null && f.getModLoader() >= 0) {
                if(out.size() >= 2) {
                    break;
                }
                switch (f.getModLoader()) {
                    case 0 -> {
                        if (!out.contains("fabric")) {
                            out.add("fabric");
                        }
                        if (!out.contains("forge")) {
                            out.add("forge");
                        }
                    }
                    case 1 -> {
                        if (!out.contains("forge")) {
                            out.add("forge");
                        }
                    }
                    case 4 -> {
                        if (!out.contains("fabric")) {
                            out.add("fabric");
                        }
                    }
                }
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
        return name;
    }

    @Override
    public List<ModVersionData> getVersions() {
        List<CurseforgeFile> files = VersionLoader.getCurseforgeVersions(id, this, null, -1);
        return List.copyOf(files);
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

    public String getLinksIssuesUrl() {
        return linksIssuesUrl;
    }

    public void setLinksIssuesUrl(String linksIssuesUrl) {
        this.linksIssuesUrl = linksIssuesUrl;
    }

    public String getLinksSourceUrl() {
        return linksSourceUrl;
    }

    public void setLinksSourceUrl(String linksSourceUrl) {
        this.linksSourceUrl = linksSourceUrl;
    }

    public String getLinksWebsiteUrl() {
        return linksWebsiteUrl;
    }

    public void setLinksWebsiteUrl(String linksWebsiteUrl) {
        this.linksWebsiteUrl = linksWebsiteUrl;
    }

    public String getWikiUrl() {
        return wikiUrl;
    }

    public void setWikiUrl(String wikiUrl) {
        this.wikiUrl = wikiUrl;
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
}
