package net.treset.mcdl.mods;

import dev.treset.mcdl.exception.FileDownloadException;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public interface ModVersionData extends Comparable<ModVersionData> {
    LocalModVersion download(File parentDir) throws FileDownloadException;

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
