package net.treset.mc_version_loader.mods;

import java.time.LocalDateTime;
import java.util.List;

public interface ModData {
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
    List<ModVersionData> getVersions();

    boolean isEqual(ModData otherMod);
}
