package net.treset.mc_version_loader;

import net.treset.mc_version_loader.exception.FileDownloadException;
import net.treset.mc_version_loader.files.Sources;
import net.treset.mc_version_loader.format.FormatUtils;
import net.treset.mc_version_loader.minecraft.MinecraftVersion;
import net.treset.mc_version_loader.mods.CombinedModData;
import net.treset.mc_version_loader.mods.ModData;
import net.treset.mc_version_loader.mods.curseforge.CurseforgeFile;
import net.treset.mc_version_loader.mods.curseforge.CurseforgeFiles;
import net.treset.mc_version_loader.mods.curseforge.CurseforgeMod;
import net.treset.mc_version_loader.mods.curseforge.CurseforgeSearch;
import net.treset.mc_version_loader.mods.modrinth.ModrinthSearch;
import net.treset.mc_version_loader.mods.modrinth.ModrinthSearchHit;
import net.treset.mc_version_loader.mods.modrinth.ModrinthVersion;
import net.treset.mc_version_loader.mojang.MinecraftProfile;

import java.util.*;

public class VersionLoader {
    /**
     * Gets a list of all minecraft versions
     * @return a list of all minecraft versions
     * @throws FileDownloadException if there is an error downloading the version manifest
     */
    public static List<MinecraftVersion> getVersions() throws FileDownloadException {
        return MinecraftVersion.fromVersionManifest(Sources.getVersionManifestJson());
    }

    /**
     * Gets a list of all minecraft release versions
     * @return a list of all minecraft release version
     * @throws FileDownloadException if there is an error downloading the version manifest
     */
    public static List<MinecraftVersion> getReleases() throws FileDownloadException {
        return getVersions().stream().filter(MinecraftVersion::isRelease).toList();
    }

    /**
     * Search for a mod on modrinth and curseforge
     * @param query the search query
     * @param gameVersion the game version to search for
     * @param modLoader the mod loader to search for
     * @param limit the maximum number of results to return
     * @param offset the offset to start the search at
     * @return a list of mods that match the search query sorted by download count
     * @throws FileDownloadException if there is an error during the search
     */
    public static List<ModData> searchCombinedMods(String query, String gameVersion, String modLoader, int limit, int offset) throws FileDownloadException {
        ModrinthSearch mrSearch = searchModrinth(query, gameVersion == null ? null : List.of(gameVersion), modLoader == null ? null : List.of(modLoader), limit, offset);
        CurseforgeSearch cfSearch = searchCurseforge(query, gameVersion, FormatUtils.modLoaderToCurseforgeModLoader(modLoader), limit, offset);
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

    /**
     * Searches Modrinth for a search query
     * @param query the search query
     * @param versions the game versions to search for
     * @param loaders the mod loaders to search for
     * @param limit the maximum number of results to return
     * @param offset the offset to start the search at
     * @return a list of mods that match the search query sorted by download count
     * @throws FileDownloadException if there is an error during the search
     */
    public static ModrinthSearch searchModrinth(String query, List<String> versions, List<String> loaders, int limit, int offset) throws FileDownloadException {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        if(query != null) {
            params.add(Map.entry(Sources.getModrinthSearchQueryParam(), query));
        }
        if(limit > 0) {
            params.add(Map.entry(Sources.getModrinthSearchLimitParam(), String.valueOf(limit)));
        }
        if(offset >= 0) {
            params.add(Map.entry(Sources.getModrinthSearchOffsetParam(), String.valueOf(offset)));
        }
        if(versions != null && !versions.isEmpty() || loaders != null && !loaders.isEmpty()) {
            StringBuilder facets = new StringBuilder().append("[");
            if(versions != null && !versions.isEmpty()) {
                versions.forEach(v -> facets.append(String.format(Sources.getModrinthVersionsFacet(), v)).append(","));
            }
            if(loaders != null && !loaders.isEmpty()) {
                loaders.forEach(l -> facets.append(String.format(Sources.getModrinthCategoryFacet(), l)).append(","));
            }
            params.add(Map.entry(Sources.getModrinthSearchFacetsParam(), facets.substring(0, facets.length() - 1) + "]"));
        }
        return ModrinthSearch.fromJson(Sources.getFileFromHttpGet(Sources.getModrinthSearchUrl(), Sources.getModrinthHeaders(), params));
    }

    /**
     * Gets the versions for a specific Modrinth project
     * @param modId the project id
     * @param parent parent mod data for the versions to reference
     * @param versions the game versions to get project versions for
     * @param modLoaders the mod loaders to get project versions for
     * @return a list of versions for the project sorted descending by date
     * @throws FileDownloadException if there is an error downloading the project versions
     */
    public static List<ModrinthVersion> getModrinthVersion(String modId, ModData parent, List<String> versions, List<String> modLoaders) throws FileDownloadException {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        if(versions != null && !versions.isEmpty()) {
            StringBuilder ver = new StringBuilder("[");
            versions.forEach(v -> ver.append("\"").append(v).append("\","));
            params.add(Map.entry(Sources.getModrinthVersionsGameversionsParam(), ver.substring(0, ver.length() - 1) + "]"));
        }
        if(modLoaders != null && !modLoaders.isEmpty()) {
            StringBuilder loaders = new StringBuilder("[");
            modLoaders.forEach(l -> loaders.append("\"").append(l).append("\","));
            params.add(Map.entry(Sources.getModrinthVersionsLoadersParam(), loaders.substring(0, loaders.length() - 1) + "]"));
        }
        return ModrinthVersion.fromJsonArray(Sources.getFileFromHttpGet(String.format(Sources.getModrinthVersionsUrl(), modId), Sources.getModrinthHeaders(), params), parent);
    }

    /**
     * Gets a specific Modrinth version
     * @param versionId the version id
     * @param parent parent mod data for the version to reference
     * @return the version data
     * @throws FileDownloadException if there is an error downloading the version
     */
    public static ModrinthVersion getModrinthVersion(String versionId, ModData parent) throws FileDownloadException {
        return ModrinthVersion.fromJson(Sources.getFileFromHttpGet(String.format(Sources.getModrinthVersionUrl(), versionId), Sources.getModrinthHeaders(), List.of()), parent);
    }

    /**
     * Searches Curseforge for a search query
     * @param query the search query
     * @param gameVersion the game version to search for
     * @param modLoader the mod loader id to search for
     * @param limit the maximum number of results to return
     * @param offset the offset to start the search at
     * @return a list of mods that match the search query sorted by download count
     * @throws FileDownloadException if there is an error during the search
     */
    public static CurseforgeSearch searchCurseforge(String query, String gameVersion, int modLoader, int limit, int offset) throws FileDownloadException {
        List<Map.Entry<String, String>> params = new ArrayList<>(Sources.getCurseforgeSearchDefaultParams());
        if(query != null && !query.isBlank()) {
            params.add(Map.entry(Sources.getCurseforgeSearchQueryParam(), query));
        }
        if(gameVersion != null && !gameVersion.isBlank()) {
            params.add(Map.entry(Sources.getCurseforgeSearchGameversionParam(), gameVersion));
        }
        if(modLoader >= 0) {
            params.add(Map.entry(Sources.getCurseforgeSearchLoaderParam(), String.valueOf(modLoader)));
        }
        if(limit > 0) {
            params.add(Map.entry(Sources.getCurseforgeSearchLimitParam(), String.valueOf(limit)));
        }
        if(offset > 0) {
            params.add(Map.entry(Sources.getCurseforgeSearchOffsetParam(), String.valueOf(offset)));
        }
        return CurseforgeSearch.fromJson(Sources.getFileFromHttpGet(Sources.getCurseforgeSearchUrl(), Sources.getCurseforgeHeaders(), params));
    }

    /**
     * Gets the versions for a specific Curseforge project
     * @param modId the project id
     * @param parent parent mod data for the versions to reference
     * @param gameVersion the game version to get project versions for
     * @param modLoader the mod loader id to get project versions for
     * @return a list of versions for the project sorted descending by date
     * @throws FileDownloadException if there is an error downloading the project versions
     */
    public static CurseforgeFiles getCurseforgeVersions(int modId, ModData parent, String gameVersion, int modLoader) throws FileDownloadException {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        if(gameVersion != null && !gameVersion.isBlank()) {
            params.add(Map.entry(Sources.getCurseforgeSearchGameversionParam(), gameVersion));
        }
        if(modLoader >= 0) {
            params.add(Map.entry(Sources.getCurseforgeSearchLoaderParam(), String.valueOf(modLoader)));
        }
        return CurseforgeFiles.fromJson(Sources.getFileFromHttpGet(String.format(Sources.getCurseforgeVersionsUrl(), modId), Sources.getCurseforgeHeaders(), params), parent);
    }

    /**
     * Gets a specific Curseforge version
     * @param modId the parent project id
     * @param versionId the version id
     * @return the version data
     * @throws FileDownloadException if there is an error downloading the version
     */
    public static CurseforgeFile getCurseforgeVersion(int modId, int versionId) throws FileDownloadException {
        return CurseforgeFile.fromJson(Sources.getFileFromHttpGet(String.format(Sources.getCurseforgeVersionUrl(), modId, versionId), Sources.getCurseforgeHeaders(), List.of()));
    }

    public static MinecraftProfile getMinecraftProfile(String uuid) throws FileDownloadException {
        return MinecraftProfile.fromJson(Sources.getFileFromUrl(String.format(Sources.getMojangSessionProfileUrl(), uuid)));
    }
 }