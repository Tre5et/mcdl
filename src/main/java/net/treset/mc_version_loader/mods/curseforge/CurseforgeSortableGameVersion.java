package net.treset.mc_version_loader.mods.curseforge;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;

import java.util.ArrayList;
import java.util.List;

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

    public static CurseforgeSortableGameVersion fromJson(JsonObject versionObj) {
        return new CurseforgeSortableGameVersion(
                JsonUtils.getAsString(versionObj, "gameVersion"),
                JsonUtils.getAsString(versionObj, "gameVersionName"),
                JsonUtils.getAsString(versionObj, "gameVersionPadded"),
                JsonUtils.getAsString(versionObj, "gameVersionReleaseDate"),
                JsonUtils.getAsInt(versionObj, "gameVersionTypeId")
        );
    }

    public static List<CurseforgeSortableGameVersion> parseCurseforgeSortableGameVersion(JsonArray versionArray) {
        List<CurseforgeSortableGameVersion> versions = new ArrayList<>();
        if(versionArray != null) {
            for(JsonElement e : versionArray) {
                versions.add(fromJson(JsonUtils.getAsJsonObject(e)));
            }
        }
        return versions;
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
