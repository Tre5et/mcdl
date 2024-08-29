package net.treset.mcdl.mods.modrinth;

import com.google.gson.reflect.TypeToken;

import net.treset.mcdl.exception.FileDownloadException;
import net.treset.mcdl.format.FormatUtils;
import net.treset.mcdl.json.GenericJsonParsable;
import net.treset.mcdl.json.JsonParsable;
import net.treset.mcdl.json.JsonUtils;
import net.treset.mcdl.json.SerializationException;
import net.treset.mcdl.mods.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModrinthVersion extends GenericModVersion implements JsonParsable {
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
    private transient ModData parent;

    public ModrinthVersion(String authorId, String changelog, String changelogUrl, String datePublished, List<ModrinthVersionDependency> dependencies, int downloads, boolean featured, List<ModrinthVersionFile> files, List<String> gameVersions, String id, List<String> loaders, String name, String projectId, String requestedStatus, String status, String versionNumber, String versionType, ModData parent) {
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
        this.parent = parent;
    }

    public static ModrinthVersion fromJson(String json, ModData parent) throws SerializationException {
        ModrinthVersion v = GenericJsonParsable.fromJson(json, ModrinthVersion.class);
        if(v != null) {
            v.setParentMod(parent);
        }
        return v;
    }

    public static List<ModrinthVersion> fromJsonArray(String json, ModData parent) throws SerializationException {
        List<ModrinthVersion> out = GenericJsonParsable.fromJsonArray(json, new TypeToken<>(){});
        out.forEach(v -> v.setParentMod(parent));
        return out;
    }

    @Override
    public String toJson() {
        return JsonUtils.getGson().toJson(this);
    }

    @Override
    public void writeToFile(String filePath) throws IOException {
        JsonUtils.writeJsonToFile(this, filePath);
    }

    @Override
    public LocalDateTime getDatePublished() {
        return FormatUtils.parseLocalDateTime(datePublished);
    }

    @Override
    public int getDownloads() {
        return downloads;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVersionNumber() {
        return versionNumber;
    }

    @Override
    public String getDownloadUrl() {
        if(files == null || files.isEmpty()) {
            return null;
        }
        return files.get(0).getUrl();
    }

    @Override
    public List<String> getModLoaders() {
        return loaders;
    }

    @Override
    public List<String> getGameVersions() {
        return gameVersions;
    }

    @Override
    public List<ModVersionData> updateRequiredDependencies() throws FileDownloadException {
        if(MinecraftMods.getModrinthUserAgent() == null) {
            throw new FileDownloadException("Modrinth user agent is null");
        }
        ArrayList<ModVersionData> requiredDependencies = new ArrayList<>();
        if(dependencies != null) {
            for(ModrinthVersionDependency d : dependencies) {
                ModrinthVersion version;
                ModrinthMod parent;
                if (d.isRequired()) {
                    if (d.getProjectId() != null) {
                        try {
                            parent =  MinecraftMods.getModrinthMod(d.getProjectId());
                        } catch (FileDownloadException e) {
                            throw new FileDownloadException("Failed to download parent mod", e);
                        }
                        if(d.getVersionId() != null) {
                            version = MinecraftMods.getModrinthVersion(d.getVersionId(), parent);
                        } else {
                            if(parent != null) {
                                parent.setVersionConstraints(dependencyGameVersions, dependencyModLoaders, downloadProviders);
                                List<ModVersionData> versions = parent.getVersions();
                                if (versions != null && !versions.isEmpty()) {
                                    version = (ModrinthVersion)versions.get(0);
                                } else {
                                    continue;
                                }
                            } else {
                                continue;
                            }
                        }
                    } else {
                        if(d.getVersionId() != null) {
                            try {
                                version = MinecraftMods.getModrinthVersion(d.getVersionId(), null);
                                parent = MinecraftMods.getModrinthMod(version.getProjectId());
                            } catch (FileDownloadException e) {
                                throw new FileDownloadException("Failed to parse modrinth version json", e);
                            }
                            version.setParentMod(parent);
                        } else {
                            continue;
                        }
                    }
                    if (version == null || version.getGameVersions().stream().noneMatch(gameVersions::contains)) {
                        parent.setVersionConstraints(dependencyGameVersions, dependencyModLoaders, downloadProviders);
                        List<ModVersionData> versions = parent.getVersions();
                        if (versions != null && !versions.isEmpty()) {
                            requiredDependencies.add(versions.get(0));
                        }
                    } else {
                        requiredDependencies.add(version);
                    }
                }
            }
        }
        currentDependencies = requiredDependencies;
        return currentDependencies;
    }

    @Override
    public ModData getParentMod() {
        return parent;
    }

    @Override
    public void setParentMod(ModData parent) {
        this.parent = parent;
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

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    public List<ModrinthVersionDependency> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<ModrinthVersionDependency> dependencies) {
        this.dependencies = dependencies;
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

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getVersionType() {
        return versionType;
    }

    public void setVersionType(String versionType) {
        this.versionType = versionType;
    }

    public ModData getParent() {
        return parent;
    }

    public void setParent(ModData parent) {
        this.parent = parent;
    }

    @Override
    public ModVersionType getModVersionType() {
        return Map.of(
                "release", ModVersionType.RELEASE,
                "beta", ModVersionType.BETA,
                "alpha", ModVersionType.ALPHA
        ).getOrDefault(versionType, ModVersionType.NONE);
    }

    @Override
    public List<ModProvider> getModProviders() {
        return List.of(ModProvider.MODRINTH);
    }
}