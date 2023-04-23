package net.treset.mc_version_loader.mods;

import java.time.LocalDateTime;
import java.util.List;

public interface ModVersionData {
    LocalDateTime getDatePublished();
    int getDownloads();
    String getName();
    String getVersionNumber();
    String getDownloadUrl();
    List<String> getModLoaders(); // all lowercase
    boolean supportsModLoader(String modLoader);
    List<String> getGameVersions();
    boolean supportsGameVersion(String version);
    List<ModData> getRequiredDependencies();
    ModData getParentMod();
    boolean setParentMod(ModData parent);

    boolean isSame(ModVersionData otherVersion);
}
