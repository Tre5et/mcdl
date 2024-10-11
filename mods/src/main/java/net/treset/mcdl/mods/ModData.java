package net.treset.mcdl.mods;

import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.format.FormatUtils;
import net.treset.mcdl.mods.curseforge.CurseforgeMod;
import net.treset.mcdl.mods.curseforge.CurseforgeSearch;
import net.treset.mcdl.mods.modrinth.ModrinthSearch;
import net.treset.mcdl.mods.modrinth.ModrinthSearchHit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface ModData extends Comparable<ModData> {
    static List<ModData> searchCombined(String query, List<String> gameVersions, List<String> modLoaders, int limit, int offset) throws FileDownloadException {
        ModrinthSearch mrSearch = ModrinthSearch.search(query, gameVersions, modLoaders, limit, offset);
        CurseforgeSearch cfSearch = CurseforgeSearch.search(query, gameVersions, FormatUtils.modLoadersToCurseforgeModLoaders(modLoaders), limit, offset);
        List<ModData> combinedMods = new ArrayList<>();
        Set<ModData> toRemove = new HashSet<>();
        List<ModrinthSearchHit> mh = mrSearch.getHits();
        List<CurseforgeMod> ch = cfSearch.getData();
        ch.removeIf(c -> !c.isAllowModDistribution());
        for(ModrinthSearchHit m : mh) {
            for(CurseforgeMod c : ch) {
                if(m.isSame(c)) {
                    combinedMods.add(new CombinedModData(m, c));
                    toRemove.add(m);
                    toRemove.add(c);
                    break;
                }
            }
        }
        mh.removeAll(toRemove);
        ch.removeAll(toRemove);
        combinedMods.addAll(mh);
        combinedMods.addAll(ch);
        return combinedMods.stream().sorted().toList();
    }

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
