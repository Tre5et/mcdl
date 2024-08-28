package net.treset.mcdl.mods;

import net.treset.mcdl.exception.FileDownloadException;

import java.time.LocalDateTime;
import java.util.*;

public class CombinedModVersion extends GenericModVersion {
    private LocalDateTime datePublished;
    private int downloads;
    private String name;
    private String versionNumber;
    private String downloadUrl1;
    private String downloadUrl2;
    private Set<String> modLoaders;
    private Set<String> gameVersions;
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
        List<String> urls;
        if(downloadProviders == null) {
            urls = List.of(downloadUrl1, downloadUrl2);
        } else {
            int index1 = downloadProviders.indexOf(parent1.getModProviders().get(0));
            int index2 = downloadProviders.indexOf(parent2.getModProviders().get(0));
            if(index1 < 0 && index2 >= 0 || index2 >= 0 && index2 < index1) {
                urls = List.of(downloadUrl2, downloadUrl1);
            } else {
                urls = List.of(downloadUrl1, downloadUrl2);
            }
        }

        return urls.stream().filter(Objects::nonNull).findFirst().orElse(null);
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
    public List<ModVersionData> updateRequiredDependencies() throws FileDownloadException {
        ArrayList<ModVersionData> requiredDependencies = new ArrayList<>();
        parent1.setDependencyConstraints(dependencyGameVersions, dependencyModLoaders, downloadProviders);
        parent2.setDependencyConstraints(dependencyGameVersions, dependencyModLoaders, downloadProviders);
        List<ModVersionData> rdo1 = parent1.getRequiredDependencies();
        List<ModVersionData> rdo2 = parent2.getRequiredDependencies();
        List<ModVersionData> rd1 = rdo1 == null ? new ArrayList<>() : new ArrayList<>(rdo1);
        List<ModVersionData> rd2 = rdo2 == null ? new ArrayList<>() : new ArrayList<>(rdo2);
        Set<ModVersionData> toRemove1 = new HashSet<>();
        Set<ModVersionData> toRemove2 = new HashSet<>();
        for (ModVersionData m1 : rd1) {
            for (ModVersionData m2 : rd2) {
                if (m1 != null && m1.isSame(m2)) {
                    requiredDependencies.add(new CombinedModVersion(m1, m2, new CombinedModData(m1.getParentMod(), m2.getParentMod())));
                    toRemove1.add(m1);
                    toRemove2.add(m2);
                    break;
                }
            }
        }
        rd1.removeAll(toRemove1);
        rd2.removeAll(toRemove2);
        requiredDependencies.addAll(rd1);
        requiredDependencies.addAll(rd2);

        currentDependencies = requiredDependencies;
        return currentDependencies;
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
