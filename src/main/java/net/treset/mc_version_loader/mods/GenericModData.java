package net.treset.mc_version_loader.mods;

import net.treset.mc_version_loader.exception.FileDownloadException;
import net.treset.mc_version_loader.format.FormatUtils;

import java.util.List;

public abstract class GenericModData implements ModData {
    protected List<String> versionGameVersions = null;
    protected List<String> versionModLoaders = null;
    protected List<ModProvider> versionProviders = null;

    protected List<ModVersionData> currentVersions = null;

    private List<String> currentVersionGameVersions = null;
    private List<String> currentVersionModLoaders = null;
    private List<ModProvider> currentVersionProviders = null;

    @Override
    public boolean isSame(ModData otherMod) {
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

    @Override
    public List<ModVersionData> getVersions() throws FileDownloadException {
        if(
            currentVersions == null ||
            currentVersionGameVersions != versionGameVersions ||
            currentVersionModLoaders != versionModLoaders ||
            currentVersionProviders != versionProviders
        ) {
            currentVersionGameVersions = versionGameVersions;
            currentVersionModLoaders = versionModLoaders;
            currentVersionProviders = versionProviders;
            return updateVersions();
        }
        return currentVersions;
    }

    @Override
    public List<String> getVersionGameVersions() {
        return versionGameVersions;
    }

    @Override
    public void setVersionGameVersions(List<String> updateGameVersions) {
        this.versionGameVersions = updateGameVersions;
    }

    @Override
    public List<String> getVersionModLoaders() {
        return versionModLoaders;
    }

    @Override
    public void setVersionModLoaders(List<String> updateModLoaders) {
        this.versionModLoaders = updateModLoaders;
    }

    @Override
    public List<ModProvider> getVersionProviders() {
        return versionProviders;
    }

    @Override
    public void setVersionProviders(List<ModProvider> updateProviders) {
        this.versionProviders = updateProviders;
    }

    @Override
    public void setVersionConstraints(List<String> gameVersions, List<String> modLoaders, List<ModProvider> providers) {
        setVersionGameVersions(gameVersions);
        setVersionModLoaders(modLoaders);
        setVersionProviders(providers);
    }

    @Override
    public int compareTo(ModData modData) {
        return Integer.compare(modData.getDownloadsCount(), this.getDownloadsCount());
    }
}
