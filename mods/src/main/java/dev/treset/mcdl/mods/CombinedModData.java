package dev.treset.mcdl.mods;

import dev.treset.mcdl.exception.FileDownloadException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CombinedModData extends GenericModData {
    private Set<String> authors;
    private Set<String> categories;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;
    private String description;
    private int downloadCount;
    private String iconUrl;
    private Set<String> gameVersions;
    private Set<String> modLoaders;
    private String slug;
    private String name;
    private ModData parent1;
    private ModData parent2;

    public CombinedModData(ModData m1, ModData m2) {
        authors = new HashSet<>(m1.getAuthors());
        authors.addAll(m2.getAuthors());
        categories = new HashSet<>(m1.getCategories());
        categories.addAll(m2.getCategories());
        LocalDateTime c1 = m1.getDateCreated();
        LocalDateTime c2 = m2.getDateCreated();
        dateCreated = c1 == null ? c2 : c2 == null ? c1 : c1.isAfter(c2) ? c2 : c1;
        LocalDateTime dm1 = m1.getDateModified();
        LocalDateTime dm2 = m2.getDateModified();
        dateModified = dm1 == null ? dm2 : dm2 == null ? dm1 : dm1.isAfter(dm2) ? dm2 : dm1;
        description = m1.getDescription() == null ? m2.getDescription() : m1.getDescription();
        downloadCount = m1.getDownloadsCount() + m2.getDownloadsCount();
        iconUrl = m1.getIconUrl() == null ? m2.getIconUrl() : m1.getIconUrl();
        gameVersions = m1.getGameVersions() != null ? new HashSet<>(m1.getGameVersions()) : new HashSet<>(m2.getGameVersions());
        if(m2.getGameVersions() != null) gameVersions.addAll(m2.getGameVersions());
        modLoaders =  m1.getModLoaders() != null ? new HashSet<>(m1.getModLoaders()) : new HashSet<>(m2.getModLoaders());
        if(m2.getModLoaders() != null) modLoaders.addAll(m2.getModLoaders());
        slug = m1.getSlug() == null ? m2.getSlug() : m1.getSlug();
        name = m1.getName() == null ? m2.getName() : m1.getName();
        parent1 = m1;
        parent2 = m2;
    }

    @Override
    public List<String> getAuthors() {
        return authors.stream().toList();
    }

    @Override
    public List<String> getCategories() {
        return categories.stream().toList();
    }

    @Override
    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    @Override
    public LocalDateTime getDateModified() {
        return dateModified;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getDownloadsCount() {
        return downloadCount;
    }

    @Override
    public String getIconUrl() {
        return iconUrl;
    }

    @Override
    public List<String> getGameVersions() {
        return gameVersions.stream().toList();
    }

    @Override
    public List<String> getModLoaders() {
        return modLoaders.stream().toList();
    }

    @Override
    public String getSlug() {
        return slug;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUrl() {
        if(versionProviders == null) {
            return parent1.getUrl();
        }
        int index1 = versionProviders.indexOf(parent1.getModProviders().get(0));
        int index2 = versionProviders.indexOf(parent2.getModProviders().get(0));
        if(parent2.getUrl() != null && index2 >= 0 && (index1 < 0 || index2 < index1)) {
            return parent2.getUrl();
        }
        return parent1.getUrl();
    }

    @Override
    public List<ModVersionData> updateVersions() throws FileDownloadException {
        ArrayList<ModVersionData> versions = new ArrayList<>();
        parent1.setVersionConstraints(versionGameVersions, versionModLoaders, versionProviders);
        parent2.setVersionConstraints(versionGameVersions, versionModLoaders, versionProviders);
        List<ModVersionData> vo1 = parent1.getVersions();
        List<ModVersionData> vo2 = parent2.getVersions();
        ArrayList<ModVersionData> v1 = vo1 == null ? new ArrayList<>() : new ArrayList<>(vo1);
        ArrayList<ModVersionData> v2 = vo2 == null ? new ArrayList<>() : new ArrayList<>(vo2);
        Set<ModVersionData> toRemove = new HashSet<>();
        for(ModVersionData vd1 : v1) {
            for(ModVersionData vd2 : v2) {
                if(vd1 != null && vd1.isSame(vd2)) {
                    versions.add(new CombinedModVersion(vd1, vd2, this));
                    toRemove.add(vd1);
                    toRemove.add(vd2);
                    break;
                }
            }
        }
        v1.removeAll(toRemove);
        v2.removeAll(toRemove);
        for (ModVersionData v : v1) {
            v.setParentMod(this);
            versions.add(v);
        }
        for (ModVersionData v : v2) {
            v.setParentMod(this);
            versions.add(v);
        }
        currentVersions = versions;
        return currentVersions;
    }

    public void setAuthors(Set<String> authors) {
        this.authors = authors;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateModified(LocalDateTime dateModified) {
        this.dateModified = dateModified;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public void setGameVersions(Set<String> gameVersions) {
        this.gameVersions = gameVersions;
    }

    public void setModLoaders(Set<String> modLoaders) {
        this.modLoaders = modLoaders;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ModData getParent1() {
        return parent1;
    }

    public void setParent1(ModData parent1) {
        this.parent1 = parent1;
    }

    public ModData getParent2() {
        return parent2;
    }

    public void setParent2(ModData parent2) {
        this.parent2 = parent2;
    }

    @Override
    public List<ModProvider> getModProviders() {
        ArrayList<ModProvider> providers = new ArrayList<>(parent1.getModProviders());
        providers.addAll(parent2.getModProviders());
        return providers;
    }

    @Override
    public List<String> getProjectIds() {
        ArrayList<String> ids = new ArrayList<>(parent1.getProjectIds());
        ids.addAll(parent2.getProjectIds());
        return ids;
    }
}
