package net.treset.mc_version_loader.mods;

import net.treset.mc_version_loader.exception.FileDownloadException;

import java.time.LocalDateTime;
import java.util.List;

public interface ModVersionData extends Comparable<ModVersionData> {
    LocalDateTime getDatePublished();
    int getDownloads();
    String getName();
    String getVersionNumber();
    String getDownloadUrl();
    List<String> getModLoaders(); // all lowercase
    boolean supportsModLoader(String modLoader);
    List<String> getGameVersions();
    boolean supportsGameVersion(String version);
    List<ModVersionData> getRequiredDependencies(String gameVersion, String modLoader) throws FileDownloadException;
    ModData getParentMod();
    boolean setParentMod(ModData parent);
    List<ModProvider> getModProviders();
    ModVersionType getModVersionType();

    boolean isSame(ModVersionData otherVersion);

}
