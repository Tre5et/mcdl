package net.treset.mc_version_loader.mods;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CombinedModVersion extends GenericModVersion {
    LocalDateTime datePublished;
    int downloads;
    String name;
    String versionNumber;
    String downloadUrl1;
    String downloadUrl2;
    Set<String> modLoaders;
    Set<String> gameVersions;
    List<ModData> requiredDependencies;
    ModData parentMod;

    public CombinedModVersion(ModVersionData v1, ModVersionData v2, ModData parent, String gameVersion, String modLoader) {
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
        requiredDependencies = new ArrayList<>();
        List<ModData> rdo1 = v1.getRequiredDependencies();
        List<ModData> rdo2 = v2.getRequiredDependencies();
        List<ModData> rd1 = rdo1 == null ? new ArrayList<>() : new ArrayList<>(rdo1);
        List<ModData> rd2 = rdo2 == null ? new ArrayList<>() : new ArrayList<>(rdo2);
        Set<ModData> toRemove = new HashSet<>();
        for (ModData m1 : rd1) {
            for (ModData m2 : rd2) {
                if (m1 != null && m1.isSame(m2)) {
                    requiredDependencies.add(new CombinedModData(m1, m2, gameVersion, modLoader));
                    toRemove.add(m1);
                    toRemove.add(m2);
                    break;
                }
            }
        }
        rd1.removeAll(toRemove);
        rd2.removeAll(toRemove);
        requiredDependencies.addAll(rd1);
        requiredDependencies.addAll(rd2);
        parentMod = parent;
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
    public List<ModData> getRequiredDependencies() {
        return requiredDependencies;
    }

    @Override
    public ModData getParentMod() {
        return parentMod;
    }

    @Override
    public boolean setParentMod(ModData parentMod) {
        this.parentMod = parentMod;
        return true;
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

    public void setRequiredDependencies(List<ModData> requiredDependencies) {
        this.requiredDependencies = requiredDependencies;
    }
}
