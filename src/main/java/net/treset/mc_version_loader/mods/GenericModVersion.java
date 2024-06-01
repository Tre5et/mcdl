package net.treset.mc_version_loader.mods;

import net.treset.mc_version_loader.exception.FileDownloadException;
import net.treset.mc_version_loader.format.FormatUtils;

import java.util.List;

public abstract class GenericModVersion implements ModVersionData {
    protected List<String> dependencyGameVersions = null;
    protected List<String> dependencyModLoaders = null;
    protected List<ModProvider> downloadProviders = null;

    protected List<ModVersionData> currentDependencies = null;

    private List<String> currentDependencyGameVersions = null;
    private List<String> currentDependencyModLoaders = null;
    private List<ModProvider> currentDownloadProviders = null;

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
    public List<String> getDependencyGameVersions() {
        return dependencyGameVersions;
    }

    @Override
    public void setDependencyGameVersions(List<String> dependencyGameVersions) {
        this.dependencyGameVersions = dependencyGameVersions;
    }

    @Override
    public List<ModProvider> getDownloadProviders() {
        return downloadProviders;
    }

    @Override
    public void setDownloadProviders(List<ModProvider> downloadProviders) {
        this.downloadProviders = downloadProviders;
    }

    @Override
    public List<String> getDependencyModLoaders() {
        return dependencyModLoaders;
    }

    @Override
    public void setDependencyModLoaders(List<String> dependencyModLoaders) {
        this.dependencyModLoaders = dependencyModLoaders;
    }

    @Override
    public void setDependencyConstraints(List<String> gameVersions, List<String> modLoaders, List<ModProvider> providers) {
        setDependencyGameVersions(gameVersions);
        setDependencyModLoaders(modLoaders);
        setDownloadProviders(providers);
    }

    @Override
    public List<ModVersionData> getRequiredDependencies() throws FileDownloadException {
        if(
            currentDependencies == null ||
            currentDependencyGameVersions != dependencyGameVersions ||
            currentDependencyModLoaders != dependencyModLoaders ||
            currentDownloadProviders != downloadProviders
        ) {
            currentDependencyGameVersions = dependencyGameVersions;
            currentDependencyModLoaders = dependencyModLoaders;
            currentDownloadProviders = downloadProviders;
            return updateRequiredDependencies();
        }
        return currentDependencies;
    }

    @Override
    public int compareTo(ModVersionData versionData) {
        return getDatePublished().compareTo(versionData.getDatePublished());
    }

    @Override
    public String toString() {
        return getVersionNumber();
    }
}
