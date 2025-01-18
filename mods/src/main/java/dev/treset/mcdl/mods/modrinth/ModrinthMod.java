package dev.treset.mcdl.mods.modrinth;

import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.format.FormatUtils;
import dev.treset.mcdl.json.GenericJsonParsable;
import dev.treset.mcdl.json.SerializationException;
import dev.treset.mcdl.mods.GenericModData;
import dev.treset.mcdl.mods.ModProvider;
import dev.treset.mcdl.mods.ModVersionData;
import dev.treset.mcdl.mods.ModsDL;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ModrinthMod extends GenericModData {
    private List<String> additionalCategories;
    private String approved;
    private String body;
    private String bodyUrl;
    private List<String> categories;
    private String clientSide;
    private int color;
    private String description;
    private String discordUrl;
    private List<ModrinthDonationUrl> donationUrls;
    private int downloads;
    private String flameAnvilProject;
    private String flameAnvilUser;
    private int followers;
    private List<ModrinthGalleryImage> gallery;
    private List<String> gameVersions;
    private String iconUrl;
    private String id;
    private String issuesUrl;
    private ModrinthLicense license;
    private List<String> loaders;
    private ModrinthModeratorMessage moderatorMessage;
    private String projectType;
    private String datePublished;
    private String queued;
    private String requestedStatus;
    private String serverSide;
    private String slug;
    private String sourceUrl;
    private String status;
    private String team;
    private String title;
    private String dateUpdated;
    private List<String> versions;
    private String wikiUrl;

    public ModrinthMod(List<String> additionalCategories, String approved, String body, String bodyUrl, List<String> categories, String clientSide, int color, String description, String discordUrl, List<ModrinthDonationUrl> donationUrls, int downloads, String flameAnvilProject, String flameAnvilUser, int followers, List<ModrinthGalleryImage> gallery, List<String> gameVersion, String iconUrl, String id, String issuesUrl, ModrinthLicense license, List<String> loaders, ModrinthModeratorMessage moderatorMessage, String projectType, String datePublished, String queued, String requestedStatus, String serverSide, String slug, String sourceUrl, String status, String team, String title, String updated, List<String> versions, String wikiUrl) {
        this.additionalCategories = additionalCategories;
        this.approved = approved;
        this.body = body;
        this.bodyUrl = bodyUrl;
        this.categories = categories;
        this.clientSide = clientSide;
        this.color = color;
        this.description = description;
        this.discordUrl = discordUrl;
        this.donationUrls = donationUrls;
        this.downloads = downloads;
        this.flameAnvilProject = flameAnvilProject;
        this.flameAnvilUser = flameAnvilUser;
        this.followers = followers;
        this.gallery = gallery;
        this.gameVersions = gameVersion;
        this.iconUrl = iconUrl;
        this.id = id;
        this.issuesUrl = issuesUrl;
        this.license = license;
        this.loaders = loaders;
        this.moderatorMessage = moderatorMessage;
        this.projectType = projectType;
        this.datePublished = datePublished;
        this.queued = queued;
        this.requestedStatus = requestedStatus;
        this.serverSide = serverSide;
        this.slug = slug;
        this.sourceUrl = sourceUrl;
        this.status = status;
        this.team = team;
        this.title = title;
        this.dateUpdated = updated;
        this.versions = versions;
        this.wikiUrl = wikiUrl;
    }

    public static ModrinthMod fromJson(String json) throws SerializationException {
        return GenericJsonParsable.fromJson(json, ModrinthMod.class);
    }

    /**
     * Gets a mod from Modrinth
     * @param id The mod ID
     * @return The mod
     * @throws FileDownloadException If an error occurs while downloading the mod
     */
    public static ModrinthMod get(String id) throws FileDownloadException {
        if(ModsDL.getModrinthUserAgent() == null) {
            throw new FileDownloadException("Modrinth user agent not set");
        }
        try {
            String content = ModsDL.httpGetModrinthString(ModsDL.getModrinthProjectUrl(id), Map.of());
            return ModrinthMod.fromJson(content);
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse modrinth mod", e);
        } catch (IOException e) {
            throw new FileDownloadException("Unable to download modrinth mod", e);
        }
    }

    @Override
    public List<String> getAuthors() {
        // TODO (but stays like that for now)
        return List.of();
    }

    @Override
    public List<String> getCategories() {
        return categories;
    }

    @Override
    public LocalDateTime getDateCreated() {
        return FormatUtils.parseLocalDateTime(datePublished);
    }

    @Override
    public LocalDateTime getDateModified() {
        return FormatUtils.parseLocalDateTime(dateUpdated);
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
        return gameVersions;
    }

    @Override
    public List<String> getModLoaders() {
        return loaders;
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
        return "https://modrinth.com/project/" + id;
    }


    @Override
    public List<ModVersionData> updateVersions() throws FileDownloadException {
        if(versionProviders == null || !versionProviders.contains(ModProvider.MODRINTH)) {
            return List.of();
        }
        currentVersions = List.copyOf(ModsDL.getModrinthVersions(id, this, versionGameVersions, versionModLoaders));
        return currentVersions;
    }

    public List<String> getAdditionalCategories() {
        return additionalCategories;
    }

    public void setAdditionalCategories(List<String> additionalCategories) {
        this.additionalCategories = additionalCategories;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBodyUrl() {
        return bodyUrl;
    }

    public void setBodyUrl(String bodyUrl) {
        this.bodyUrl = bodyUrl;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiscordUrl() {
        return discordUrl;
    }

    public void setDiscordUrl(String discordUrl) {
        this.discordUrl = discordUrl;
    }

    public List<ModrinthDonationUrl> getDonationUrls() {
        return donationUrls;
    }

    public void setDonationUrls(List<ModrinthDonationUrl> donationUrls) {
        this.donationUrls = donationUrls;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public String getFlameAnvilProject() {
        return flameAnvilProject;
    }

    public void setFlameAnvilProject(String flameAnvilProject) {
        this.flameAnvilProject = flameAnvilProject;
    }

    public String getFlameAnvilUser() {
        return flameAnvilUser;
    }

    public void setFlameAnvilUser(String flameAnvilUser) {
        this.flameAnvilUser = flameAnvilUser;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public List<ModrinthGalleryImage> getGallery() {
        return gallery;
    }

    public void setGallery(List<ModrinthGalleryImage> gallery) {
        this.gallery = gallery;
    }

    public void setGameVersions(List<String> gameVersions) {
        this.gameVersions = gameVersions;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIssuesUrl() {
        return issuesUrl;
    }

    public void setIssuesUrl(String issuesUrl) {
        this.issuesUrl = issuesUrl;
    }

    public ModrinthLicense getLicense() {
        return license;
    }

    public void setLicense(ModrinthLicense license) {
        this.license = license;
    }

    public List<String> getLoaders() {
        return loaders;
    }

    public void setLoaders(List<String> loaders) {
        this.loaders = loaders;
    }

    public ModrinthModeratorMessage getModeratorMessage() {
        return moderatorMessage;
    }

    public void setModeratorMessage(ModrinthModeratorMessage moderatorMessage) {
        this.moderatorMessage = moderatorMessage;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    public String getQueued() {
        return queued;
    }

    public void setQueued(String queued) {
        this.queued = queued;
    }

    public String getRequestedStatus() {
        return requestedStatus;
    }

    public void setRequestedStatus(String requestedStatus) {
        this.requestedStatus = requestedStatus;
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

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public void setVersions(List<String> versions) {
        this.versions = versions;
    }

    public String getWikiUrl() {
        return wikiUrl;
    }

    public void setWikiUrl(String wikiUrl) {
        this.wikiUrl = wikiUrl;
    }

    @Override
    public List<ModProvider> getModProviders() {
        return List.of(ModProvider.MODRINTH);
    }

    @Override
    public List<String> getProjectIds() {
        return List.of(id);
    }
}
