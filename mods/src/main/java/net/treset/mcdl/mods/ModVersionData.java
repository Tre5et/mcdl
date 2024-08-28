package net.treset.mcdl.mods;

import net.treset.mcdl.exception.FileDownloadException;

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
    List<String> getDependencyGameVersions();
    void setDependencyGameVersions(List<String> gameVersions);
    List<String> getDependencyModLoaders();
    void setDependencyModLoaders(List<String> modLoaders);
    List<ModProvider> getDownloadProviders();
    void setDownloadProviders(List<ModProvider> providers);
    void setDependencyConstraints(List<String> gameVersions, List<String> modLoaders, List<ModProvider> providers);
    List<ModVersionData> getRequiredDependencies() throws FileDownloadException;
    List<ModVersionData> updateRequiredDependencies() throws FileDownloadException;
    ModData getParentMod();
    void setParentMod(ModData parent);
    List<ModProvider> getModProviders();
    ModVersionType getModVersionType();

    boolean isSame(ModVersionData otherVersion);

}
