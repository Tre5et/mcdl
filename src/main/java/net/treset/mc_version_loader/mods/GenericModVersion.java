package net.treset.mc_version_loader.mods;

import net.treset.mc_version_loader.format.FormatUtils;

import java.util.List;

public abstract class GenericModVersion implements ModVersionData {
    @Override
    public boolean isSame(ModVersionData otherVersion) {
        return this.getParentMod() != null &&  otherVersion.getParentMod() != null && this.getModVersionType() == otherVersion.getModVersionType() && this.getParentMod().isSame(otherVersion.getParentMod()) && (FormatUtils.formatVersionComparison(this.getVersionNumber()).equals(FormatUtils.formatVersionComparison(otherVersion.getVersionNumber())) || FormatUtils.formatVersionComparison(this.getName()).equals(FormatUtils.formatVersionComparison(otherVersion.getName())));
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

    @Override
    public int compareTo(ModVersionData versionData) {
        return getDatePublished().compareTo(versionData.getDatePublished());
    }
}
