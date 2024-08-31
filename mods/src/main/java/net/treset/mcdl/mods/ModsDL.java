package net.treset.mcdl.mods;

import net.treset.mcdl.exception.FileDownloadException;
import net.treset.mcdl.mods.curseforge.CurseforgeFile;
import net.treset.mcdl.mods.curseforge.CurseforgeMod;
import net.treset.mcdl.mods.curseforge.CurseforgeSearch;
import net.treset.mcdl.mods.modrinth.ModrinthMod;
import net.treset.mcdl.mods.modrinth.ModrinthSearch;
import net.treset.mcdl.mods.modrinth.ModrinthVersion;
import net.treset.mcdl.util.HttpUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ModsDL {
    private static final String CURSEFORGE_SEARCH_URL = "https://api.curseforge.com/v1/mods/search";
    private static final Map<String, String> CURSEFORGE_SEARCH_DEFAULT_PARAMS = (Map.of("gameId", "432", "sortField", "2", "sortOrder", "desc"));
    private static final String CURSEFORGE_SEARCH_QUERY_PARAM = "searchFilter"; // search query
    private static final String CURSEFORGE_SEARCH_GAMEVERSIONS_PARAM = "gameVersions"; // game version
    private static final String CURSEFORGE_SEARCH_LOADERS_PARAM = "modLoaderTypes"; // mod loader index (1=forge, 4=fabric)
    private static final String CURSEFORGE_SEARCH_LIMIT_PARAM = "pageSize";
    private static final String CURSEFORGE_SEARCH_INDEX_PARAM = "index";
    private static final String CURSEFORGE_PROJECT_URL = "https://api.curseforge.com/v1/mods/%d"; // Mod-ID
    private static final String CURSEFORGE_VERSIONS_URL = "https://api.curseforge.com/v1/mods/%d/files"; // Mod-ID
    private static final String CURSEFORGE_VERSIONS_GAMEVERSIONS_PARAM = CURSEFORGE_SEARCH_GAMEVERSIONS_PARAM;
    private static final String CURSEFORGE_VERSIONS_LOADERS_PARAM = CURSEFORGE_SEARCH_LOADERS_PARAM;
    private static final String CURSEFORGE_VERSION_URL = "https://api.curseforge.com/v1/mods/%d/files/%d";
    private static final String MODRINTH_SEARCH_URL = "https://api.modrinth.com/v2/search";
    private static final String MODRINTH_SEARCH_QUERY_PARAM = "query";
    private static final String MODRINTH_SEARCH_LIMIT_PARAM = "limit";
    private static final String MODRINTH_SEARCH_OFFSET_PARAM = "offset";
    private static final String MODRINTH_SEARCH_FACETS_PARAM = "facets";
    private static final String MODRINTH_CATEGORY_FACET = "\"categories:%s\"";
    private static final String MODRINTH_VERSIONS_FACET = "\"versions:%s\"";
    private static final String MODRINTH_PROJECT_URL = "https://api.modrinth.com/v2/project/%s"; // Project-ID
    private static final String MODRINTH_VERSIONS_URL = "https://api.modrinth.com/v2/project/%s/version"; // %s := project id
    private static final String MODRINTH_VERSIONS_GAMEVERSIONS_PARAM = "game_versions"; // list of quoted game versions
    private static final String MODRINTH_VERSIONS_LOADERS_PARAM = "loaders"; // list of quoted mod loaders
    private static final String MODRINTH_VERSION_URL = "https://api.modrinth.com/v2/version/%s";

    private static String curseforgeApiKey;
    public static String modrinthUserAgent;

    /**
     * Downloads a mod version to the specified directory.
     * @param data The version to download
     * @param parentDir The directory to download the mod to
     * @return The downloaded launcher mod data
     * @throws FileDownloadException If there is an error downloading or writing the mod
     */
    public static LocalModVersion downloadModFile(ModVersionData data, File parentDir) throws FileDownloadException {
        return data.download(parentDir);
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
        return ModData.searchCombined(query, gameVersions, modLoaders, limit, offset);
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
        return ModrinthSearch.search(query, versions, loaders, limit, offset);
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
        return ModrinthVersion.getAll(modId, parent, versions, modLoaders);
    }

    /**
     * Gets a specific Modrinth version
     * @param versionId the version id
     * @param parent parent mod data for the version to reference
     * @return the version data
     * @throws FileDownloadException if there is an error downloading the version
     */
    public static ModrinthVersion getModrinthVersion(String versionId, ModData parent) throws FileDownloadException {
        return ModrinthVersion.get(versionId, parent);
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
        return CurseforgeSearch.search(query, gameVersions, modLoaders, limit, offset);
    }

    /**
     * Gets all versions for a specific Curseforge project
     * @param modId the project id
     * @param parent parent mod data for the versions to reference
     * @return a list of versions for the project sorted descending by date
     * @throws FileDownloadException if there is an error downloading the project versions
     */
    public static List<CurseforgeFile> getCurseforgeVersions(int modId, ModData parent, List<String> gameVersions, List<String> modLoaders, int index) throws FileDownloadException {
        return CurseforgeFile.getAll(modId, parent, gameVersions, modLoaders, index);
    }

    /**
     * Gets all versions for a specific Curseforge project
     * @param modId the project id
     * @param parent parent mod data for the versions to reference
     * @param gameVersions the game versions to get project versions for
     * @param modLoaders the mod loaders to get project versions for
     * @return a list of versions for the project sorted descending by date
     * @throws FileDownloadException if there is an error downloading the project versions
     */
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
        return CurseforgeFile.get(modId, versionId);
    }

    /**
     * Gets the Modrinth mod with the specified id.
     * @param modId The mod id
     * @return The modrinth mod
     * @throws FileDownloadException If there is an error loading or parsing the mod
     */
    public static ModrinthMod getModrinthMod(String modId) throws FileDownloadException {
       return ModrinthMod.get(modId);
    }

    /**
     * Gets the Curseforge mod with the specified id.
     * @param projectId The project id
     * @return The curseforge mod
     * @throws FileDownloadException If there is an error loading or parsing the mod
     */
    public static CurseforgeMod getCurseforgeMod(long projectId) throws FileDownloadException {
        return CurseforgeMod.get(projectId);
    }

    /**
     * Checks if a Modrinth mod with the specified id exists.
     * @param modId The mod id
     * @return Whether the mod exists
     * @throws FileDownloadException If there is an error making the mod request
     */
    public static boolean checkModrinthValid(String modId) throws FileDownloadException {
        if(getModrinthUserAgent() == null) {
            throw new FileDownloadException("Modrinth user agent not set");
        }
        try {
            String result = HttpUtil.getString(getModrinthProjectUrl(modId), getModrinthHeaders(getModrinthUserAgent()), Map.of());
            return !result.isBlank();
        } catch (IOException e) {
            throw new FileDownloadException("Unable to check modrinth mod", e);
        }
    }

    /**
     * Checks if a Curseforge mod with the specified id exists.
     * @param projectId The project id
     * @return Whether the mod exists
     * @throws FileDownloadException If there is an error making the mod request
     */
    public static boolean checkCurseforgeValid(long projectId) throws FileDownloadException {
        if(getCurseforgeApiKey() == null) {
            throw new FileDownloadException("Curseforge api key not set");
        }
        try {
            String result = HttpUtil.getString(getCurseforgeProjectUrl(projectId), getCurseforgeHeaders(curseforgeApiKey), Map.of());
            return !result.isBlank();
        } catch (IOException e) {
            throw new FileDownloadException("Unable to check curseforge mod", e);
        }
    }

    public static String getCurseforgeApiKey() {
        return curseforgeApiKey;
    }

    /**
     * Sets the curseforge api key. This must be done before any requests involving curseforge are made.
     * @param curseforgeApiKey The curseforge api key
     */
    public static void setCurseforgeApiKey(String curseforgeApiKey) {
        ModsDL.curseforgeApiKey = curseforgeApiKey;
    }

    public static String getCurseforgeSearchUrl() {
        return CURSEFORGE_SEARCH_URL;
    }

    public static Map<String, String> getCurseforgeSearchDefaultParams() {
        return CURSEFORGE_SEARCH_DEFAULT_PARAMS;
    }

    public static String getCurseforgeSearchQueryParam() {
        return CURSEFORGE_SEARCH_QUERY_PARAM;
    }

    public static String getCurseforgeSearchGameversionsParam() {
        return CURSEFORGE_SEARCH_GAMEVERSIONS_PARAM;
    }

    public static String getCurseforgeSearchLoadersParam() {
        return CURSEFORGE_SEARCH_LOADERS_PARAM;
    }

    public static String getCurseforgeSearchLimitParam() {
        return CURSEFORGE_SEARCH_LIMIT_PARAM;
    }

    public static String getCurseforgeSearchIndexParam() {
        return CURSEFORGE_SEARCH_INDEX_PARAM;
    }

    public static String getCurseforgeProjectUrl(long projectId)  {
        return String.format(CURSEFORGE_PROJECT_URL, projectId);
    }

    public static String getCurseforgeProjectVersionsUrl(long projectId) {
        return String.format(CURSEFORGE_VERSIONS_URL, projectId);
    }

    public static String getCurseforgeVersionsGameversionsParam() {
        return CURSEFORGE_VERSIONS_GAMEVERSIONS_PARAM;
    }

    public static String getCurseforgeVersionsLoadersParam() {
        return CURSEFORGE_VERSIONS_LOADERS_PARAM;
    }

    public static String getCurseforgeVersionUrl(long projectId, long versionId) {
        return String.format(CURSEFORGE_VERSION_URL, projectId, versionId);
    }

    public static Map<String, String> getCurseforgeHeaders(String apiKey) {
        return Map.of("x-api-key", apiKey, "Accept", "application/json");
    }

    public static String getModrinthUserAgent() {
        return modrinthUserAgent;
    }

    /**
     * Sets the modrinth user agent. This must be done before any requests involving modrinth are made.
     * @param modrinthUserAgent The modrinth user agent
     */
    public static void setModrinthUserAgent(String modrinthUserAgent) {
        ModsDL.modrinthUserAgent = modrinthUserAgent;
    }

    public static String getModrinthSearchUrl() {
        return MODRINTH_SEARCH_URL;
    }

    public static String getModrinthSearchQueryParam() {
        return MODRINTH_SEARCH_QUERY_PARAM;
    }

    public static String getModrinthSearchLimitParam() {
        return MODRINTH_SEARCH_LIMIT_PARAM;
    }

    public static String getModrinthSearchOffsetParam() {
        return MODRINTH_SEARCH_OFFSET_PARAM;
    }

    public static String getModrinthSearchFacetsParam() {
        return MODRINTH_SEARCH_FACETS_PARAM;
    }

    public static String getModrinthCategoryFacet(String categories) {
        if(categories == null || categories.isBlank()) {
            throw new IllegalArgumentException("Invalid categories=" + categories);
        }
        return String.format(MODRINTH_CATEGORY_FACET, categories);
    }

    public static String getModrinthVersionsFacet(String versions) {
        if(versions == null || versions.isBlank()) {
            throw new IllegalArgumentException("Invalid versions=" + versions);
        }
        return String.format(MODRINTH_VERSIONS_FACET, versions);
    }

    public static String getModrinthProjectUrl(String projectId) {
        if(projectId == null || projectId.isBlank()) {
            throw new IllegalArgumentException("Invalid projectId="+projectId);
        }
        return String.format(MODRINTH_PROJECT_URL, projectId);
    }

    public static String getModrinthProjectVersionsUrl(String projectId) {
        if(projectId == null || projectId.isBlank()) {
            throw new IllegalArgumentException("Invalid projectId="+projectId);
        }
        return String.format(MODRINTH_VERSIONS_URL, projectId);
    }

    public static String getModrinthVersionsGameversionsParam() {
        return MODRINTH_VERSIONS_GAMEVERSIONS_PARAM;
    }

    public static String getModrinthVersionsLoadersParam() {
        return MODRINTH_VERSIONS_LOADERS_PARAM;
    }

    public static String getModrinthVersionUrl(String versionId) {
        if(versionId == null || versionId.isBlank()) {
            throw new IllegalArgumentException("Invalid versionId=" + versionId);
        }
        return String.format(MODRINTH_VERSION_URL, versionId);
    }

    public static Map<String, String> getModrinthHeaders(String userAgent) {
        return Map.of("User-Agent", userAgent);
    }
}
