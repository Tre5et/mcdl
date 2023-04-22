package net.treset.mc_version_loader.mods;

import net.treset.mc_version_loader.format.FormatUtils;

import java.util.List;

public abstract class GenericModVersion implements ModVersionData {
    @Override
    public boolean isEqual(ModVersionData otherVersion) {
        return this.getParentMod().isEqual(otherVersion.getParentMod()) && (FormatUtils.formatVersionComparison(this.getVersionNumber()).equals(FormatUtils.formatVersionComparison(otherVersion.getVersionNumber())) || FormatUtils.formatVersionComparison(this.getName()).equals(FormatUtils.formatVersionComparison(otherVersion.getName())));
    }

    @Override
    public boolean supportsGameVersion(String version) {
        List<String> gameVersions = getGameVersions();
        return gameVersions != null && gameVersions.contains(version);
    }

    @Override
    public boolean supportsModLoader(String modLoader) {
        List<String> modLoaders = getModLoaders();
        return modLoaders != null && modLoaders.contains(modLoader);
    }
}
