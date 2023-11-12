package net.treset.mc_version_loader.mods;

import net.treset.mc_version_loader.exception.FileDownloadException;
import net.treset.mc_version_loader.format.FormatUtils;
import net.treset.mc_version_loader.json.SerializationException;
import net.treset.mc_version_loader.mods.curseforge.CurseforgeFile;
import net.treset.mc_version_loader.mods.curseforge.CurseforgeFiles;
import net.treset.mc_version_loader.mods.curseforge.CurseforgeMod;
import net.treset.mc_version_loader.mods.curseforge.CurseforgeSearch;
import net.treset.mc_version_loader.mods.modrinth.ModrinthMod;
import net.treset.mc_version_loader.mods.modrinth.ModrinthSearch;
import net.treset.mc_version_loader.mods.modrinth.ModrinthSearchHit;
import net.treset.mc_version_loader.mods.modrinth.ModrinthVersion;
import net.treset.mc_version_loader.util.FileUtil;
import net.treset.mc_version_loader.launcher.LauncherMod;
import net.treset.mc_version_loader.launcher.LauncherModDownload;
import net.treset.mc_version_loader.util.Sources;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class ModUtil {
    private static String modrinthUserAgent;
    private static String curseforgeApiKey;

    public static LauncherMod downloadModFile(ModVersionData data, File parentDir, boolean enabled) throws FileDownloadException {
        if(data == null || data.getParentMod() == null) {
            throw new FileDownloadException("Unable to download mod: unmet requirements: mod=" + data);
        }

        List<ModProvider> providers = data.getParentMod().getModProviders();
        List<String> projectIds = data.getParentMod().getProjectIds();
        if(providers.size() != projectIds.size()) {
            throw new FileDownloadException("Unable to download mod, provider count does not match project id count: mod=" + data.getName());
        }

        String[] urlParts = data.getDownloadUrl().split("/");
        String fileName = urlParts[urlParts.length - 1];
        File modFile = new File(parentDir, fileName);
        URL downloadUrl;
        try {
            downloadUrl = new URL(data.getDownloadUrl());
        } catch (MalformedURLException e) {
            throw new FileDownloadException("Unable to download mod, malformed url: mod=" + data.getName(), e);
        }
        FileUtil.downloadFile(downloadUrl, modFile);

        ArrayList<LauncherModDownload> downloads = new ArrayList<>();
        for(int i = 0; i < providers.size(); i++) {
            downloads.add(new LauncherModDownload(providers.get(i).toString().toLowerCase(), projectIds.get(i)));
        }

        return new LauncherMod(
                data.getModProviders().get(0).toString().toLowerCase(),
                data.getParentMod().getDescription(),
                enabled,
                data.getParentMod().getUrl(),
                data.getParentMod().getIconUrl(),
                data.getParentMod().getName(),
                fileName,
                data.getVersionNumber(),
                downloads
        );
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
        if(modrinthUserAgent == null) {
            throw new FileDownloadException("Modrinth user agent not set");
        }
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
                versions.forEach(v -> facets.append(Sources.getModrinthVersionsFacet(v)).append(","));
            }
            if(loaders != null && !loaders.isEmpty()) {
                loaders.forEach(l -> facets.append(Sources.getModrinthCategoryFacet(l)).append(","));
            }
            params.add(Map.entry(Sources.getModrinthSearchFacetsParam(), facets.substring(0, facets.length() - 1) + "]"));
        }
        try {
            return ModrinthSearch.fromJson(FileUtil.getStringFromHttpGet(Sources.getModrinthSearchUrl(), Sources.getModrinthHeaders(modrinthUserAgent), params));
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse modrinth search results", e);
        }
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
        if(modrinthUserAgent == null) {
            throw new FileDownloadException("Modrinth user agent not set");
        }
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
        try {
            return ModrinthVersion.fromJsonArray(FileUtil.getStringFromHttpGet(Sources.getModrinthProjectVersionsUrl(modId), Sources.getModrinthHeaders(modrinthUserAgent), params), parent);
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse modrinth project versions", e);
        }
    }

    /**
     * Gets a specific Modrinth version
     * @param versionId the version id
     * @param parent parent mod data for the version to reference
     * @return the version data
     * @throws FileDownloadException if there is an error downloading the version
     */
    public static ModrinthVersion getModrinthVersion(String versionId, ModData parent) throws FileDownloadException {
        if(modrinthUserAgent == null) {
            throw new FileDownloadException("Modrinth user agent not set");
        }
        try {
            return ModrinthVersion.fromJson(FileUtil.getStringFromHttpGet(Sources.getModrinthVersionUrl(versionId), Sources.getModrinthHeaders(modrinthUserAgent), List.of()), parent);
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse modrinth version", e);
        }
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
        if(curseforgeApiKey == null) {
            throw new FileDownloadException("Curseforge api key not set");
        }
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
        try {
            return CurseforgeSearch.fromJson(FileUtil.getStringFromHttpGet(Sources.getCurseforgeSearchUrl(), Sources.getCurseforgeHeaders(curseforgeApiKey), params));
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse curseforge search results", e);
        }
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
        if(curseforgeApiKey == null) {
            throw new FileDownloadException("Curseforge api key not set");
        }
        List<Map.Entry<String, String>> params = new ArrayList<>();
        if(gameVersion != null && !gameVersion.isBlank()) {
            params.add(Map.entry(Sources.getCurseforgeSearchGameversionParam(), gameVersion));
        }
        if(modLoader >= 0) {
            params.add(Map.entry(Sources.getCurseforgeSearchLoaderParam(), String.valueOf(modLoader)));
        }
        try {
            return CurseforgeFiles.fromJson(FileUtil.getStringFromHttpGet(Sources.getCurseforgeProjectVersionsUrl(modId), Sources.getCurseforgeHeaders(curseforgeApiKey), params), parent);
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse curseforge project versions", e);
        }
    }

    /**
     * Gets a specific Curseforge version
     * @param modId the parent project id
     * @param versionId the version id
     * @return the version data
     * @throws FileDownloadException if there is an error downloading the version
     */
    public static CurseforgeFile getCurseforgeVersion(int modId, int versionId) throws FileDownloadException {
        if(curseforgeApiKey == null) {
            throw new FileDownloadException("Curseforge api key not set");
        }
        try {
            return CurseforgeFile.fromJson(FileUtil.getStringFromHttpGet(Sources.getCurseforgeVersionUrl(modId, versionId), Sources.getCurseforgeHeaders(curseforgeApiKey), List.of()));
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse curseforge version", e);
        }
    }

    public static ModrinthMod getModrinthMod(String modId) throws FileDownloadException {
        if(modrinthUserAgent == null) {
            throw new FileDownloadException("Modrinth user agent not set");
        }
        try {
            return ModrinthMod.fromJson(FileUtil.getStringFromHttpGet(Sources.getModrinthProjectUrl(modId), Sources.getModrinthHeaders(modrinthUserAgent), List.of()));
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse modrinth mod", e);
        }
    }

    public static CurseforgeMod getCurseforgeMod(long projectId) throws FileDownloadException {
        if(curseforgeApiKey == null) {
            throw new FileDownloadException("Curseforge api key not set");
        }
        try {
            return CurseforgeMod.fromJson(FileUtil.getStringFromHttpGet(Sources.getCurseforgeProjectUrl(projectId), Sources.getCurseforgeHeaders(curseforgeApiKey), List.of()));
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse curseforge mod", e);
        }
    }

    public static boolean checkModrinthValid(String modId) throws FileDownloadException {
        if(modrinthUserAgent == null) {
            throw new FileDownloadException("Modrinth user agent not set");
        }
        String result = FileUtil.getStringFromHttpGet(Sources.getModrinthProjectUrl(modId), Sources.getModrinthHeaders(modrinthUserAgent), List.of());
        return result != null && !result.isBlank();
    }

    public static boolean checkCurseforgeValid(long projectId) throws FileDownloadException {
        if(curseforgeApiKey == null) {
            throw new FileDownloadException("Curseforge api key not set");
        }
        String result = FileUtil.getStringFromHttpGet(Sources.getCurseforgeProjectUrl(projectId), Sources.getCurseforgeHeaders(curseforgeApiKey), List.of());
        return result != null && !result.isBlank();
    }

    public static String getModrinthUserAgent() {
        return modrinthUserAgent;
    }

    public static void setModrinthUserAgent(String modrinthUserAgent) {
        ModUtil.modrinthUserAgent = modrinthUserAgent;
    }

    public static String getCurseforgeApiKey() {
        return curseforgeApiKey;
    }

    public static void setCurseforgeApiKey(String curseforgeApiKey) {
        ModUtil.curseforgeApiKey = curseforgeApiKey;
    }
}
