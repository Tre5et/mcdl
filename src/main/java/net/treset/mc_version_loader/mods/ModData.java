package net.treset.mc_version_loader.mods;

import net.treset.mc_version_loader.exception.FileDownloadException;

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
    List<ModVersionData> getVersions(String gameVersion, String modLoader) throws FileDownloadException;
    List<ModVersionData> updateVersions() throws FileDownloadException;
    List<ModProvider> getModProviders();
    List<String> getProjectIds();

    boolean isSame(ModData otherMod);
}
