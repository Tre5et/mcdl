package net.treset.mc_version_loader.mods.modrinth;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.treset.mc_version_loader.format.FormatUtils;
import net.treset.mc_version_loader.json.JsonUtils;
import net.treset.mc_version_loader.mods.GenericModVersion;
import net.treset.mc_version_loader.mods.ModData;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ModrinthVersion extends GenericModVersion {
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
    private ModData parent;

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

    public static ModrinthVersion formJson(JsonObject versionObj, ModData parent) {
        return new ModrinthVersion(
                JsonUtils.getAsString(versionObj, "author_id"),
                JsonUtils.getAsString(versionObj, "changelog"),
                JsonUtils.getAsString(versionObj, "changelog_url"),
                JsonUtils.getAsString(versionObj, "date_published"),
                parseModrinthVersionDependencies(JsonUtils.getAsJsonArray(versionObj, "dependencies")),
                JsonUtils.getAsInt(versionObj, "downloads"),
                JsonUtils.getAsBoolean(versionObj, "featured"),
                parseModrinthVersionFiles(JsonUtils.getAsJsonArray(versionObj, "files")),
                JsonUtils.parseJsonStringArray(JsonUtils.getAsJsonArray(versionObj, "game_versions")),
                JsonUtils.getAsString(versionObj, "id"),
                JsonUtils.parseJsonStringArray(JsonUtils.getAsJsonArray(versionObj, "loaders")),
                JsonUtils.getAsString(versionObj, "name"),
                JsonUtils.getAsString(versionObj, "project_id"),
                JsonUtils.getAsString(versionObj, "requested_status"),
                JsonUtils.getAsString(versionObj, "status"),
                JsonUtils.getAsString(versionObj, "version_number"),
                JsonUtils.getAsString(versionObj, "version_type"),
                parent
        );
    }

    private static List<ModrinthVersionDependency> parseModrinthVersionDependencies(JsonArray dependencyArray) {
        List<ModrinthVersionDependency> dependencies = new ArrayList<>();
        if(dependencyArray != null) {
            for(JsonElement d : dependencyArray) {
                dependencies.add(ModrinthVersionDependency.fromJson(JsonUtils.getAsJsonObject(d)));
            }
        }
        return dependencies;
    }

    private static List<ModrinthVersionFile> parseModrinthVersionFiles(JsonArray filesArray) {
        List<ModrinthVersionFile> files = new ArrayList<>();
        if(filesArray != null) {
            for(JsonElement f : filesArray) {
                files.add(ModrinthVersionFile.fromJson(JsonUtils.getAsJsonObject(f)));
            }
        }
        return files;
    }

    public static List<ModrinthVersion> parseModrinthVersions(String json, ModData parent) {
        JsonArray versionsArray = JsonUtils.getAsJsonArray(JsonUtils.parseJson(json));
        List<ModrinthVersion> out = new ArrayList<>();
        if(versionsArray != null) {
            for(JsonElement v : versionsArray) {
                out.add(formJson(JsonUtils.getAsJsonObject(v), parent));
            }
        }
        return out;
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
        if(files == null || files.size() < 1) {
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
    public List<ModData> getRequiredDependencies() {
        // TODO
        return null;
    }

    @Override
    public ModData getParentMod() {
        return parent;
    }
}
