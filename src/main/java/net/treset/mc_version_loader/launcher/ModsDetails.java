package net.treset.mc_version_loader.launcher;

import java.util.List;

public class ModsDetails {
    String modsType;
    List<LauncherMod> mods;

    public ModsDetails(String modsType, List<LauncherMod> mods) {
        this.modsType = modsType;
        this.mods = mods;
    }

    public String getModsType() {
        return modsType;
    }

    public void setModsType(String modsType) {
        this.modsType = modsType;
    }

    public List<LauncherMod> getMods() {
        return mods;
    }

    public void setMods(List<LauncherMod> mods) {
        this.mods = mods;
    }
}
