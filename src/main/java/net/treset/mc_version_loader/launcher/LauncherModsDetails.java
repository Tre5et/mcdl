package net.treset.mc_version_loader.launcher;

import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;

import java.util.List;

public class LauncherModsDetails {
    private String modsType;
    private String modsVersion;
    private List<LauncherMod> mods;

    public LauncherModsDetails(String modsType, String modsVersion, List<LauncherMod> mods) {
        this.modsType = modsType;
        this.modsVersion = modsVersion;
        this.mods = mods;
    }

    public static LauncherModsDetails fromJson(String json) {
        JsonObject modsObj = JsonUtils.getAsJsonObject(JsonUtils.parseJson(json));
        return new LauncherModsDetails(
                JsonUtils.getAsString(modsObj, "mods_type"),
                JsonUtils.getAsString(modsObj, "mods_version"),
                LauncherMod.parseMods(JsonUtils.getAsJsonArray(modsObj, "mods"))
        );
    }

    public String getModsType() {
        return modsType;
    }

    public void setModsType(String modsType) {
        this.modsType = modsType;
    }

    public String getModsVersion() {
        return modsVersion;
    }

    public void setModsVersion(String modsVersion) {
        this.modsVersion = modsVersion;
    }

    public List<LauncherMod> getMods() {
        return mods;
    }

    public void setMods(List<LauncherMod> mods) {
        this.mods = mods;
    }
}
