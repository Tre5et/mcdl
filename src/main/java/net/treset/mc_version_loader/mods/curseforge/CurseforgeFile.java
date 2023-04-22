package net.treset.mc_version_loader.mods.curseforge;

import java.util.List;

public class CurseforgeFile {
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

    public CurseforgeFile(int alternateFileId, List<CurseforgeDependency> dependencies, String displayName, int downloadCount, String downloadUrl, String fileDate, int fileFingerprint, int fileLength, String fileName, int fileStatus, int gameId, List<String> gameVersions, List<CurseforgeHash> hashes, int id, boolean isAvailable, boolean isServerPack, int modId, List<CurseforgeModule> modules, int releaseType, List<CurseforgeSortableGameVersion> sortableGameVersions) {
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

    public String getDownloadUrl() {
        return downloadUrl;
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

    public List<String> getGameVersions() {
        return gameVersions;
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
