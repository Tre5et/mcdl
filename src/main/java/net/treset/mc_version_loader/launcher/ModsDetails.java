package net.treset.mc_version_loader.launcher;

import java.util.List;

public class ModsDetails {
    private String modsType;
    private String modsVersion;
    private List<LauncherMod> mods;

    public ModsDetails(String modsType, String modsVersion, List<LauncherMod> mods) {
        this.modsType = modsType;
        this.modsVersion = modsVersion;
        this.mods = mods;
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
