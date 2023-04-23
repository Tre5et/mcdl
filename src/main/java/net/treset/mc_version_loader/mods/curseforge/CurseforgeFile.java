package net.treset.mc_version_loader.mods.curseforge;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.treset.mc_version_loader.format.FormatUtils;
import net.treset.mc_version_loader.json.JsonUtils;
import net.treset.mc_version_loader.mods.GenericModVersion;
import net.treset.mc_version_loader.mods.ModData;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class CurseforgeFile extends GenericModVersion {
    private int alternateFileId;
    private List<CurseforgeDependency> dependencies;
    private String displayName;
    private int downloadCount;
    private String downloadUrl;
    private String fileDate;
    private int fileFingerprint;
    private int fileLength;
    private String fileName;
    private int fileStatus;
    private int gameId;
    private List<String> gameVersions;
    private List<CurseforgeHash> hashes;
    private int id;
    private boolean isAvailable;
    private boolean isServerPack;
    private int modId;
    private List<CurseforgeModule> modules;
    private int releaseType;
    private List<CurseforgeSortableGameVersion> sortableGameVersions;
    private ModData parentMod;

    public CurseforgeFile(int alternateFileId, List<CurseforgeDependency> dependencies, String displayName, int downloadCount, String downloadUrl, String fileDate, int fileFingerprint, int fileLength, String fileName, int fileStatus, int gameId, List<String> gameVersions, List<CurseforgeHash> hashes, int id, boolean isAvailable, boolean isServerPack, int modId, List<CurseforgeModule> modules, int releaseType, List<CurseforgeSortableGameVersion> sortableGameVersions, ModData parentMod) {
        this.alternateFileId = alternateFileId;
        this.dependencies = dependencies;
        this.displayName = displayName;
        this.downloadCount = downloadCount;
        this.downloadUrl = downloadUrl;
        this.fileDate = fileDate;
        this.fileFingerprint = fileFingerprint;
        this.fileLength = fileLength;
        this.fileName = fileName;
        this.fileStatus = fileStatus;
        this.gameId = gameId;
        this.gameVersions = gameVersions;
        this.hashes = hashes;
        this.id = id;
        this.isAvailable = isAvailable;
        this.isServerPack = isServerPack;
        this.modId = modId;
        this.modules = modules;
        this.releaseType = releaseType;
        this.sortableGameVersions = sortableGameVersions;
        this.parentMod = parentMod;
    }

    public static CurseforgeFile fromJson(JsonObject fileObj, ModData parentMod) {
        return new CurseforgeFile(
                JsonUtils.getAsInt(fileObj, "alternateFileId"),
                CurseforgeDependency.parseCurseforgeDependencies(JsonUtils.getAsJsonArray(fileObj, "dependencies")),
                JsonUtils.getAsString(fileObj, "displayName"),
                JsonUtils.getAsInt(fileObj, "downloadCount"),
                JsonUtils.getAsString(fileObj, "downloadUrl"),
                JsonUtils.getAsString(fileObj, "fileDate"),
                JsonUtils.getAsInt(fileObj, "fileFingerprint"),
                JsonUtils.getAsInt(fileObj, "fileLength"),
                JsonUtils.getAsString(fileObj, "fileName"),
                JsonUtils.getAsInt(fileObj, "fileStatus"),
                JsonUtils.getAsInt(fileObj, "gameId"),
                JsonUtils.parseJsonStringArray(JsonUtils.getAsJsonArray(fileObj, "gameVersions")),
                CurseforgeHash.parseCursefprgeHashes(JsonUtils.getAsJsonArray(fileObj, "hashes")),
                JsonUtils.getAsInt(fileObj, "id"),
                JsonUtils.getAsBoolean(fileObj, "isAvailable"),
                JsonUtils.getAsBoolean(fileObj, "isServerPack"),
                JsonUtils.getAsInt(fileObj, "modId"),
                CurseforgeModule.parseCurseforgeModules(JsonUtils.getAsJsonArray(fileObj, "modules")),
                JsonUtils.getAsInt(fileObj, "releaseType"),
                CurseforgeSortableGameVersion.parseCurseforgeSortableGameVersion(JsonUtils.getAsJsonArray(fileObj, "sortableGameVersions")),
                parentMod
        );
    }

    public static List<CurseforgeFile> parseCurseforgeFiles(JsonArray filesArray, ModData parentMod) {
        List<CurseforgeFile> files = new ArrayList<>();
        if(filesArray != null) {
            for(JsonElement e : filesArray) {
                files.add(fromJson(JsonUtils.getAsJsonObject(e), parentMod));
            }
        }
        return files;
    }

    public static List<CurseforgeFile> parseCurseforgeFiles(String json, ModData parentMod) {
        JsonObject filesObj = JsonUtils.getAsJsonObject(JsonUtils.parseJson(json));
        return parseCurseforgeFiles(JsonUtils.getAsJsonArray(filesObj, "data"), parentMod);
    }


    @Override
    public LocalDateTime getDatePublished() {
        return FormatUtils.parseLocalDateTime(fileDate);
    }

    @Override
    public int getDownloads() {
        return downloadCount;
    }

    @Override
    public String getName() {
        return displayName;
    }

    @Override
    public String getVersionNumber() {
        return getName().replaceAll("[a-zA-Z]", "").trim().replaceAll("\\s*", "-");
    }

    @Override
    public String getDownloadUrl() {
        return downloadUrl;
    }

    @Override
    public List<String> getModLoaders() {
        if(gameVersions == null) {
            return null;
        }
        List<String> out = new ArrayList<>();
        for(String v : gameVersions) {
            char[] chars = v.toCharArray();
            boolean loader = true;
            for(char c : chars) {
                if(Character.isDigit(c)) {
                    loader = false;
                    break;
                }
            }
            if(loader) {
                out.add(v.toLowerCase());
            }
        }
        return out;
    }

    @Override
    public List<String> getGameVersions() {
        if(gameVersions == null) {
            return null;
        }
        List<String> out = new ArrayList<>();
        for(String v : gameVersions) {
            char[] chars = v.toCharArray();
            for(char c : chars) {
                if(Character.isDigit(c)) {
                    out.add(v.toLowerCase());
                    break;
                }
            }
        }
        return out;
    }

    @Override
    public List<ModData> getRequiredDependencies() {
        if(dependencies == null) {
            return null;
        }
        List<ModData> out = new ArrayList<>();
        for(CurseforgeDependency d : dependencies) {
            if(d != null && d.getRelationType() == 3) {
                // TODO
            }
        }
        return out;
    }

    @Override
    public ModData getParentMod() {
        return parentMod;
    }

    @Override
    public boolean setParentMod(ModData parentMod) {
        this.parentMod = parentMod;
        return true;
    }

    public int getAlternateFileId() {
        return alternateFileId;
    }

    public void setAlternateFileId(int alternateFileId) {
        this.alternateFileId = alternateFileId;
    }

    public List<CurseforgeDependency> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<CurseforgeDependency> dependencies) {
        this.dependencies = dependencies;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getFileDate() {
        return fileDate;
    }

    public void setFileDate(String fileDate) {
        this.fileDate = fileDate;
    }

    public int getFileFingerprint() {
        return fileFingerprint;
    }

    public void setFileFingerprint(int fileFingerprint) {
        this.fileFingerprint = fileFingerprint;
    }

    public int getFileLength() {
        return fileLength;
    }

    public void setFileLength(int fileLength) {
        this.fileLength = fileLength;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(int fileStatus) {
        this.fileStatus = fileStatus;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public void setGameVersions(List<String> gameVersions) {
        this.gameVersions = gameVersions;
    }

    public List<CurseforgeHash> getHashes() {
        return hashes;
    }

    public void setHashes(List<CurseforgeHash> hashes) {
        this.hashes = hashes;
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

    public boolean isServerPack() {
        return isServerPack;
    }

    public void setServerPack(boolean serverPack) {
        isServerPack = serverPack;
    }

    public int getModId() {
        return modId;
    }

    public void setModId(int modId) {
        this.modId = modId;
    }

    public List<CurseforgeModule> getModules() {
        return modules;
    }

    public void setModules(List<CurseforgeModule> modules) {
        this.modules = modules;
    }

    public int getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(int releaseType) {
        this.releaseType = releaseType;
    }

    public List<CurseforgeSortableGameVersion> getSortableGameVersions() {
        return sortableGameVersions;
    }

    public void setSortableGameVersions(List<CurseforgeSortableGameVersion> sortableGameVersions) {
        this.sortableGameVersions = sortableGameVersions;
    }
}
