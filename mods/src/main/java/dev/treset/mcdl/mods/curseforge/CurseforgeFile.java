package dev.treset.mcdl.mods.curseforge;

import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.format.FormatUtils;
import dev.treset.mcdl.json.GenericJsonParsable;
import dev.treset.mcdl.json.JsonParsable;
import dev.treset.mcdl.json.JsonUtils;
import dev.treset.mcdl.json.SerializationException;
import dev.treset.mcdl.mods.*;
import dev.treset.mcdl.util.HttpUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurseforgeFile extends GenericModVersion implements JsonParsable {
    private int alternateFileId;
    private List<CurseforgeDependency> dependencies;
    private String displayName;
    private int downloadCount;
    private String downloadUrl;
    private String fileDate;
    private long fileFingerprint;
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

    public CurseforgeFile(int alternateFileId, List<CurseforgeDependency> dependencies, String displayName, int downloadCount, String downloadUrl, String fileDate, long fileFingerprint, int fileLength, String fileName, int fileStatus, int gameId, List<String> gameVersions, List<CurseforgeHash> hashes, int id, boolean isAvailable, boolean isServerPack, int modId, List<CurseforgeModule> modules, int releaseType, List<CurseforgeSortableGameVersion> sortableGameVersions, ModData parentMod) {
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

    public static CurseforgeFile fromJson(String json) throws SerializationException {
        return GenericJsonParsable.fromJson(json, CurseforgeFile.class);
    }

    /**
     * Gets all versions of a curseforge project
     * @param modId The curseforge project id
     * @param parent The parent mod
     * @param gameVersions The game versions to filter by
     * @param modLoaders The mod loaders to filter by
     * @param index The index to start at
     * @return The versions
     * @throws FileDownloadException If an error occurs while downloading the versions
     */
    public static List<CurseforgeFile> getAll(int modId, ModData parent, List<String> gameVersions, List<String> modLoaders, int index) throws FileDownloadException {
        if(ModsDL.getCurseforgeApiKey() == null) {
            throw new FileDownloadException("Curseforge api key not set");
        }
        Map<String, String> params = new HashMap<>();
        params.put(ModsDL.getCurseforgeSearchIndexParam(), String.valueOf(index));

        try {
            String content = HttpUtil.getString(ModsDL.getCurseforgeProjectVersionsUrl(modId), ModsDL.getCurseforgeHeaders(ModsDL.getCurseforgeApiKey()), params, ModsDL.getCaching());
            CurseforgeFiles files = CurseforgeFiles.fromJson(content, parent);
            ArrayList<CurseforgeFile> versions = new ArrayList<>();
            if(files.getData() != null) {
                versions.addAll(files.getData());
            }
            if(files.getPagination().getTotalCount() > files.getPagination().getIndex() + files.getPagination().getPageSize()) {
                versions.addAll(getAll(modId, parent, gameVersions, modLoaders, files.getPagination().getIndex() + files.getPagination().getPageSize()));
            }
            return versions.stream().filter(v ->
                    (gameVersions == null || gameVersions.isEmpty() || v.getGameVersions().stream().anyMatch(gameVersions::contains))
                            && (modLoaders == null || modLoaders.isEmpty() || v.getModLoaders().stream().anyMatch(modLoaders::contains))
            ).toList();
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse curseforge project versions", e);
        } catch (IOException e) {
            throw new FileDownloadException("Unable to download curseforge project versions", e);
        }
    }

    /**
     * Gets a curseforge version
     * @param modId The curseforge project id
     * @param versionId The curseforge version id
     * @return The version
     * @throws FileDownloadException If an error occurs while downloading the version
     */
    public static CurseforgeFile get(int modId, int versionId) throws FileDownloadException {
        if(ModsDL.getCurseforgeApiKey() == null) {
            throw new FileDownloadException("Curseforge api key not set");
        }
        try {
            String content = HttpUtil.getString(ModsDL.getCurseforgeVersionUrl(modId, versionId), ModsDL.getCurseforgeHeaders(ModsDL.getCurseforgeApiKey()), Map.of());
            return CurseforgeFile.fromJson(content);
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse curseforge version", e);
        } catch (IOException e) {
            throw new FileDownloadException("Unable to download curseforge version", e);
        }
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
        return getName().replaceAll(".jar", "").replaceAll("[a-zA-Z]", "").replaceAll("^-*", "").replaceAll("-*$", "").trim().replaceAll("\\s+", "-");
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
    public List<ModVersionData> updateRequiredDependencies() throws FileDownloadException {
        if(ModsDL.getCurseforgeApiKey() == null) {
            throw new FileDownloadException("Curseforge api key not set");
        }
        List<Exception> exceptionQueue = new ArrayList<>();
        if(dependencies != null) {
            List<ModVersionData> requiredDependencies = dependencies.stream()
                    .filter(d -> d != null && d.getRelationType() == 3)
                    .map(d -> {
                        try {
                            return ModsDL.getCurseforgeMod(d.getModId());
                        } catch (FileDownloadException e) {
                            exceptionQueue.add(e);
                        }
                        return null;
                    })
                    .map(p -> {
                        if(p == null) {
                            exceptionQueue.add(new FileDownloadException("Error getting required dependencies: mod=" + getName()));
                            return null;
                        }
                        try {
                            p.setVersionConstraints(dependencyGameVersions, dependencyModLoaders, downloadProviders);
                            return p.getVersions();
                        } catch (FileDownloadException e) {
                            exceptionQueue.add(e);
                        }
                        return null;
                    })
                    .filter(f -> f != null && !f.isEmpty())
                    .map(f -> f.get(0))
                    .toList();
            if(!exceptionQueue.isEmpty()) {
                throw new FileDownloadException("Error getting required dependencies: mod=" + getName(), exceptionQueue.get(0));
            }
            currentDependencies = requiredDependencies;
        } else {
            currentDependencies = List.of();
        }
        return currentDependencies;
    }

    @Override
    public ModData getParentMod() {
        return parentMod;
    }

    @Override
    public void setParentMod(ModData parentMod) {
        this.parentMod = parentMod;
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

    public long getFileFingerprint() {
        return fileFingerprint;
    }

    public void setFileFingerprint(long fileFingerprint) {
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

    @Override
    public List<ModProvider> getModProviders() {
        return List.of(ModProvider.CURSEFORGE);
    }

    @Override
    public ModVersionType getModVersionType() {
        return Map.of(
                1, ModVersionType.RELEASE,
                2, ModVersionType.BETA,
                3, ModVersionType.ALPHA
        ).getOrDefault(releaseType, ModVersionType.NONE);
    }
}
