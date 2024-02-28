package net.treset.mc_version_loader.mods;

import net.treset.mc_version_loader.exception.FileDownloadException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CombinedModVersion extends GenericModVersion {
    private LocalDateTime datePublished;
    private int downloads;
    private String name;
    private String versionNumber;
    private String downloadUrl1;
    private String downloadUrl2;
    private Set<String> modLoaders;
    private Set<String> gameVersions;
    private List<ModVersionData> requiredDependencies;
    private ModData parentMod;
    private final ModVersionData parent1;
    private final ModVersionData parent2;
    private final ModVersionType type;

    public CombinedModVersion(ModVersionData v1, ModVersionData v2, ModData parent) {
        LocalDateTime p1 = v1.getDatePublished();
        LocalDateTime p2 = v2.getDatePublished();
        datePublished = p1.isAfter(p2) ? p2 : p1;
        downloads = v1.getDownloads() + v2.getDownloads();
        name = v1.getName() == null ? v2.getName() : v1.getName();
        versionNumber = v1.getVersionNumber() == null ? v2.getVersionNumber() : v1.getVersionNumber();
        downloadUrl1 = v1.getDownloadUrl();
        downloadUrl2 = v2.getDownloadUrl();
        modLoaders = new HashSet<>(v1.getModLoaders());
        modLoaders.addAll(v2.getModLoaders());
        gameVersions = new HashSet<>(v1.getGameVersions());
        gameVersions.addAll(v2.getGameVersions());
        parentMod = parent;
        type = v1.getModVersionType();
        parent1 = v1;
        parent2 = v2;
    }

    @Override
    public LocalDateTime getDatePublished() {
        return datePublished;
    }

    @Override
    public int getDownloads() {
        return downloads;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVersionNumber() {
        return versionNumber;
    }

    @Override
    public String getDownloadUrl() {
        return downloadUrl1 == null ? downloadUrl2 : downloadUrl1;
    }

    @Override
    public List<String> getModLoaders() {
        return modLoaders.stream().toList();
    }

    @Override
    public List<String> getGameVersions() {
        return gameVersions.stream().toList();
    }

    @Override
    public List<ModVersionData> getRequiredDependencies(List<String> gameVersions, List<String> modLoaders) throws FileDownloadException {
        if(requiredDependencies == null) {
            requiredDependencies = new ArrayList<>();
            List<ModVersionData> rdo1 = parent1.getRequiredDependencies(gameVersions, modLoaders);
            List<ModVersionData> rdo2 = parent2.getRequiredDependencies(gameVersions, modLoaders);
            List<ModVersionData> rd1 = rdo1 == null ? new ArrayList<>() : new ArrayList<>(rdo1);
            List<ModVersionData> rd2 = rdo2 == null ? new ArrayList<>() : new ArrayList<>(rdo2);
            Set<ModVersionData> toRemove = new HashSet<>();
            for (ModVersionData m1 : rd1) {
                for (ModVersionData m2 : rd2) {
                    if (m1 != null && m1.isSame(m2)) {
                        requiredDependencies.add(new CombinedModVersion(m1, m2, new CombinedModData(m1.getParentMod(), m2.getParentMod())));
                        toRemove.add(m1);
                        break;
                    }
                }
            }
            rd1.removeAll(toRemove);
            requiredDependencies.addAll(rd1);
        }
        return requiredDependencies;
    }

    @Override
    public ModData getParentMod() {
        return parentMod;
    }

    @Override
    public void setParentMod(ModData parentMod) {
        this.parentMod = parentMod;
    }

    public void setDatePublished(LocalDateTime datePublished) {
        this.datePublished = datePublished;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getDownloadUrl1() {
        return downloadUrl1;
    }

    public void setDownloadUrl1(String downloadUrl1) {
        this.downloadUrl1 = downloadUrl1;
    }

    public String getDownloadUrl2() {
        return downloadUrl2;
    }

    public void setDownloadUrl2(String downloadUrl2) {
        this.downloadUrl2 = downloadUrl2;
    }

    public void setModLoaders(Set<String> modLoaders) {
        this.modLoaders = modLoaders;
    }

    public void setGameVersions(Set<String> gameVersions) {
        this.gameVersions = gameVersions;
    }

    public void setRequiredDependencies(List<ModVersionData> requiredDependencies) {
        this.requiredDependencies = requiredDependencies;
    }

    public ModVersionType getType() {
        return type;
    }

    @Override
    public ModVersionType getModVersionType() {
        return null;
    }

    @Override
    public List<ModProvider> getModProviders() {
        ArrayList<ModProvider> providers = new ArrayList<>(parent1.getModProviders());
        providers.addAll(parent2.getModProviders());
        return providers;
    }
}
