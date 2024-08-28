package net.treset.mcdl.mods.curseforge;

public class CurseforgeFileIndex {
    private int fileId;
    private String filename;
    private String gameVersion;
    private int gameVersionTypeId;
    private int modLoader;
    private int releaseType;

    public CurseforgeFileIndex(int fileId, String filename, String gameVersion, int gameVersionTypeId, int modLoader, int releaseType) {
        this.fileId = fileId;
        this.filename = filename;
        this.gameVersion = gameVersion;
        this.gameVersionTypeId = gameVersionTypeId;
        this.modLoader = modLoader;
        this.releaseType = releaseType;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getGameVersion() {
        return gameVersion;
    }

    public void setGameVersion(String gameVersion) {
        this.gameVersion = gameVersion;
    }

    public int getGameVersionTypeId() {
        return gameVersionTypeId;
    }

    public void setGameVersionTypeId(int gameVersionTypeId) {
        this.gameVersionTypeId = gameVersionTypeId;
    }

    public int getModLoader() {
        return modLoader;
    }

    public void setModLoader(int modLoader) {
        this.modLoader = modLoader;
    }

    public int getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(int releaseType) {
        this.releaseType = releaseType;
    }
}
