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

public class MinecraftMods {
    private static String modrinthUserAgent;
    private static String curseforgeApiKey;

    /**
     * Downloads a mod version to the specified directory.
     * @param data The version to download
     * @param parentDir The directory to download the mod to
     * @return The downloaded launcher mod data
     * @throws FileDownloadException If there is an error downloading or writing the mod
     */
    public static LauncherMod downloadModFile(ModVersionData data, File parentDir) throws FileDownloadException {
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
                (downloadUrl.toString().contains("modrinth") ? ModProvider.MODRINTH : ModProvider.CURSEFORGE).toString().toLowerCase(),
                data.getParentMod().getDescription(),
                true,
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
     * @param gameVersions the game versions to search for
     * @param modLoaders the mod loaders to search for
     * @param limit the maximum number of results to return
     * @param offset the offset to start the search at
     * @return a list of mods that match the search query sorted by download count
     * @throws FileDownloadException if there is an error during the search
     */
    public static List<ModData> searchCombinedMods(String query, List<String> gameVersions, List<String> modLoaders, int limit, int offset) throws FileDownloadException {
        ModrinthSearch mrSearch = searchModrinth(query, gameVersions, modLoaders, limit, offset);
        CurseforgeSearch cfSearch = searchCurseforge(query, gameVersions, FormatUtils.modLoadersToCurseforgeModLoaders(modLoaders), limit, offset);
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
            StringBuilder facets = new StringBuilder().append("[[");
            if(versions != null && !versions.isEmpty()) {
                for(int i = 0; i < versions.size(); i++) {
                    facets.append(Sources.getModrinthVersionsFacet(versions.get(i)));
                    if(i < versions.size() - 1) {
                        facets.append(",");
                    }
                }
            }
            facets.append("],[");
            if(loaders != null && !loaders.isEmpty()) {
                for(int i = 0; i < loaders.size(); i++) {
                    facets.append(Sources.getModrinthCategoryFacet(loaders.get(i)));
                    if(i < loaders.size() - 1) {
                        facets.append(",");
                    }
                }
            }
            facets.append("]]");
            params.add(Map.entry(Sources.getModrinthSearchFacetsParam(), facets.toString()));
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
    public static List<ModrinthVersion> getModrinthVersions(String modId, ModData parent, List<String> versions, List<String> modLoaders) throws FileDownloadException {
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
     * @param gameVersions the game version to search for
     * @param modLoaders the mod loader id to search for
     * @param limit the maximum number of results to return
     * @param offset the offset to start the search at
     * @return a list of mods that match the search query sorted by download count
     * @throws FileDownloadException if there is an error during the search
     */
    public static CurseforgeSearch searchCurseforge(String query, List<String> gameVersions, Set<Integer> modLoaders, int limit, int offset) throws FileDownloadException {
        if(curseforgeApiKey == null) {
            throw new FileDownloadException("Curseforge api key not set");
        }
        List<Map.Entry<String, String>> params = new ArrayList<>(Sources.getCurseforgeSearchDefaultParams());
        if(query != null && !query.isBlank()) {
            params.add(Map.entry(Sources.getCurseforgeSearchQueryParam(), query));
        }
        if(gameVersions != null && !gameVersions.isEmpty()) {
            params.add(Map.entry(
                    Sources.getCurseforgeSearchGameversionsParam(),
                    FormatUtils.formatAsArrayParam(gameVersions)
            ));
        }
        if(modLoaders != null && !modLoaders.isEmpty()) {
            params.add(Map.entry(
                    Sources.getCurseforgeSearchLoadersParam(),
                    FormatUtils.formatAsArrayParam(modLoaders)
            ));
        }
        if(limit > 0) {
            params.add(Map.entry(Sources.getCurseforgeSearchLimitParam(), String.valueOf(limit)));
        }
        if(offset > 0) {
            params.add(Map.entry(Sources.getCurseforgeSearchIndexParam(), String.valueOf(offset)));
        }
        try {
            return CurseforgeSearch.fromJson(FileUtil.getStringFromHttpGet(Sources.getCurseforgeSearchUrl(), Sources.getCurseforgeHeaders(curseforgeApiKey), params));
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse curseforge search results", e);
        }
    }

    /**
     * Gets all versions for a specific Curseforge project
     * @param modId the project id
     * @param parent parent mod data for the versions to reference
     * @return a list of versions for the project sorted descending by date
     * @throws FileDownloadException if there is an error downloading the project versions
     */
    public static List<CurseforgeFile> getCurseforgeVersions(int modId, ModData parent, List<String> gameVersions, List<String> modLoaders, int index) throws FileDownloadException {
        if(curseforgeApiKey == null) {
            throw new FileDownloadException("Curseforge api key not set");
        }
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(Map.entry(Sources.getCurseforgeSearchIndexParam(), String.valueOf(index)));

        try {
            CurseforgeFiles files = CurseforgeFiles.fromJson(FileUtil.getStringFromHttpGet(Sources.getCurseforgeProjectVersionsUrl(modId), Sources.getCurseforgeHeaders(curseforgeApiKey), params), parent);
            ArrayList<CurseforgeFile> versions = new ArrayList<>();
            if(files.getData() != null) {
                versions.addAll(files.getData());
            }
            if(files.getPagination().getTotalCount() > files.getPagination().getIndex() + files.getPagination().getPageSize()) {
                versions.addAll(getCurseforgeVersions(modId, parent, gameVersions, modLoaders, files.getPagination().getIndex() + files.getPagination().getPageSize()));
            }
            return versions.stream().filter(v ->
                    (gameVersions == null || gameVersions.isEmpty() || v.getGameVersions().stream().anyMatch(gameVersions::contains))
                    && (modLoaders == null || modLoaders.isEmpty() || v.getModLoaders().stream().anyMatch(modLoaders::contains))
            ).toList();
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse curseforge project versions", e);
        }
    }

    public static List<CurseforgeFile> getCurseforgeVersions(int modId, ModData parent, List<String> gameVersions, List<String> modLoaders) throws FileDownloadException {
        return getCurseforgeVersions(modId, parent, gameVersions, modLoaders, 0);
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

    /**
     * Gets the Modrinth mod with the specified id.
     * @param modId The mod id
     * @return The modrinth mod
     * @throws FileDownloadException If there is an error loading or parsing the mod
     */
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

    /**
     * Gets the Curseforge mod with the specified id.
     * @param projectId The project id
     * @return The curseforge mod
     * @throws FileDownloadException If there is an error loading or parsing the mod
     */
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

    /**
     * Checks if a Modrinth mod with the specified id exists.
     * @param modId The mod id
     * @return Whether the mod exists
     * @throws FileDownloadException If there is an error making the mod request
     */
    public static boolean checkModrinthValid(String modId) throws FileDownloadException {
        if(modrinthUserAgent == null) {
            throw new FileDownloadException("Modrinth user agent not set");
        }
        String result = FileUtil.getStringFromHttpGet(Sources.getModrinthProjectUrl(modId), Sources.getModrinthHeaders(modrinthUserAgent), List.of());
        return !result.isBlank();
    }

    /**
     * Checks if a Curseforge mod with the specified id exists.
     * @param projectId The project id
     * @return Whether the mod exists
     * @throws FileDownloadException If there is an error making the mod request
     */
    public static boolean checkCurseforgeValid(long projectId) throws FileDownloadException {
        if(curseforgeApiKey == null) {
            throw new FileDownloadException("Curseforge api key not set");
        }
        String result = FileUtil.getStringFromHttpGet(Sources.getCurseforgeProjectUrl(projectId), Sources.getCurseforgeHeaders(curseforgeApiKey), List.of());
        return !result.isBlank();
    }

    public static String getModrinthUserAgent() {
        return modrinthUserAgent;
    }

    /**
     * Sets the modrinth user agent. This must be done before any requests involving modrinth are made.
     * @param modrinthUserAgent The modrinth user agent
     */
    public static void setModrinthUserAgent(String modrinthUserAgent) {
        MinecraftMods.modrinthUserAgent = modrinthUserAgent;
    }

    public static String getCurseforgeApiKey() {
        return curseforgeApiKey;
    }

    /**
     * Sets the curseforge api key. This must be done before any requests involving curseforge are made.
     * @param curseforgeApiKey The curseforge api key
     */
    public static void setCurseforgeApiKey(String curseforgeApiKey) {
        MinecraftMods.curseforgeApiKey = curseforgeApiKey;
    }
}
