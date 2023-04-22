package net.treset.mc_version_loader.mods;

import net.treset.mc_version_loader.format.FormatUtils;

import java.util.List;

public abstract class GenericModData implements ModData {
    @Override
    public boolean isEqual(ModData otherMod) {
        return FormatUtils.formatModComparison(this.getSlug()).equals(FormatUtils.formatModComparison(otherMod.getSlug())) || FormatUtils.formatModComparison(this.getName()).equals(FormatUtils.formatModComparison(otherMod.getName()));
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
