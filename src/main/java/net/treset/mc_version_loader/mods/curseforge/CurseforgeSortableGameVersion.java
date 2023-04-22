package net.treset.mc_version_loader.mods.curseforge;

public class CurseforgeSortableGameVersion {
    private String gameVersion;
    private String gameVersionName;
    private String gameVersionPadded;
    private String gameVersionReleaseDate;
    private int gameVersionTypeId;

    public CurseforgeSortableGameVersion(String gameVersion, String gameVersionName, String gameVersionPadded, String gameVersionReleaseDate, int gameVersionTypeId) {
        this.gameVersion = gameVersion;
        this.gameVersionName = gameVersionName;
        this.gameVersionPadded = gameVersionPadded;
        this.gameVersionReleaseDate = gameVersionReleaseDate;
        this.gameVersionTypeId = gameVersionTypeId;
    }

    public String getGameVersion() {
        return gameVersion;
    }

    public void setGameVersion(String gameVersion) {
        this.gameVersion = gameVersion;
    }

    public String getGameVersionName() {
        return gameVersionName;
    }

    public void setGameVersionName(String gameVersionName) {
        this.gameVersionName = gameVersionName;
    }

    public String getGameVersionPadded() {
        return gameVersionPadded;
    }

    public void setGameVersionPadded(String gameVersionPadded) {
        this.gameVersionPadded = gameVersionPadded;
    }

    public String getGameVersionReleaseDate() {
        return gameVersionReleaseDate;
    }

    public void setGameVersionReleaseDate(String gameVersionReleaseDate) {
        this.gameVersionReleaseDate = gameVersionReleaseDate;
    }

    public int getGameVersionTypeId() {
        return gameVersionTypeId;
    }

    public void setGameVersionTypeId(int gameVersionTypeId) {
        this.gameVersionTypeId = gameVersionTypeId;
    }
}
