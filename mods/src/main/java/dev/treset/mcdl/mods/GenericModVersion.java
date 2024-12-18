package dev.treset.mcdl.mods;

import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.format.FormatUtils;
import dev.treset.mcdl.util.FileUtil;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericModVersion implements ModVersionData {
    protected List<String> dependencyGameVersions = null;
    protected List<String> dependencyModLoaders = null;
    protected List<ModProvider> downloadProviders = null;

    protected List<ModVersionData> currentDependencies = null;

    private List<String> currentDependencyGameVersions = null;
    private List<String> currentDependencyModLoaders = null;
    private List<ModProvider> currentDownloadProviders = null;

    public LocalModVersion download(File parentDir) throws FileDownloadException {
        if(getParentMod() == null) {
            throw new FileDownloadException("Unable to download mod: unmet requirements: mod=" + getName());
        }

        List<ModProvider> providers = getParentMod().getModProviders();
        List<String> projectIds = getParentMod().getProjectIds();
        if(providers.size() != projectIds.size()) {
            throw new FileDownloadException("Unable to download mod, provider count does not match project id count: mod=" + getName());
        }

        String[] urlParts = getDownloadUrl().split("/");
        String fileName = urlParts[urlParts.length - 1];
        File modFile = new File(parentDir, fileName);
        URL downloadUrl;
        try {
            downloadUrl = new URL(getDownloadUrl());
        } catch (MalformedURLException e) {
            throw new FileDownloadException("Unable to download mod, malformed url: mod=" + getName(), e);
        }
        FileUtil.downloadFile(downloadUrl, modFile, ModsDL.getCaching());

        ArrayList<LocalModVersion.LocalModDownload> downloads = new ArrayList<>();
        for(int i = 0; i < providers.size(); i++) {
            downloads.add(new LocalModVersion.LocalModDownload(providers.get(i), projectIds.get(i)));
        }

        return new LocalModVersion(
                this,
                downloadUrl.toString().contains("modrinth") ? ModProvider.MODRINTH : ModProvider.CURSEFORGE,
                fileName,
                downloads
        );
    }

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
