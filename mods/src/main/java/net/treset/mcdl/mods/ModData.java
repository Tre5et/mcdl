package net.treset.mcdl.mods;

import net.treset.mcdl.exception.FileDownloadException;

import java.time.LocalDateTime;
import java.util.List;

public interface ModData extends Comparable<ModData> {
    List<String> getAuthors();
    List<String> getCategories();
    LocalDateTime getDateCreated();
    LocalDateTime getDateModified();
    String getDescription();
    int getDownloadsCount();
    String getIconUrl();
    List<String> getGameVersions();
    boolean supportsGameVersion(String version);
    List<String> getModLoaders(); // all lowercase
    boolean supportsModLoader(String modLoader);
    String getSlug();
    String getName();
    String getUrl();
    List<ModVersionData> getVersions() throws FileDownloadException;
    List<ModVersionData> updateVersions() throws FileDownloadException;
    List<String> getVersionGameVersions();
    void setVersionGameVersions(List<String> gameVersions);
    List<String> getVersionModLoaders();
    void setVersionModLoaders(List<String> modLoaders);
    List<ModProvider> getVersionProviders();
    void setVersionProviders(List<ModProvider> providers);
    void setVersionConstraints(List<String> gameVersions, List<String> modLoaders, List<ModProvider> providers);
    List<ModProvider> getModProviders();
    List<String> getProjectIds();

    boolean isSame(ModData otherMod);
}
