package net.treset.mc_version_loader.util;

import java.util.List;
import java.util.Map;

public class Sources {
    private static final String VERSION_MANIFEST_URL = "https://piston-meta.mojang.com/mc/game/version_manifest_v2.json";
    private static final String JAVA_RUNTIME_URL = "https://launchermeta.mojang.com/v1/products/java-runtime/2ec0cc96c44e5a76b9c8b7c39df7210883d12871/all.json";
    private static final String ASSET_URL = "https://resources.download.minecraft.net/%s/%s"; // AssetId[:2], AssetId
    private static final String FABRIC_INDEX_URL = "https://meta.fabricmc.net/v2/versions/loader/%s"; // MC-Version
    private static final String FABRIC_VERSION_URL = "https://meta.fabricmc.net/v2/versions/loader/%s/%s"; // MC-Version, Fabric Version
    private static final String FABRIC_MAVEN_URL = "https://maven.fabricmc.net/";
    private static final String FABRIC_PROFILE_URL = "https://meta.fabricmc.net/v2/versions/loader/%s/%s/profile/json"; // MC-Versions, Fabric Version
    private static final String MODRINTH_SEARCH_URL = "https://api.modrinth.com/v2/search";
    private static final String MODRINTH_SEARCH_QUERY_PARAM = "query";
    private static final String MODRINTH_SEARCH_LIMIT_PARAM = "limit";
    private static final String MODRINTH_SEARCH_OFFSET_PARAM = "offset";
    private static final String MODRINTH_SEARCH_FACETS_PARAM = "facets";
    private static final String MODRINTH_CATEGORY_FACET = "[\"categories:%s\"]";
    private static final String MODRINTH_VERSIONS_FACET = "[\"versions:%s\"]";
    private static final String MODRINTH_PROJECT_URL = "https://api.modrinth.com/v2/project/%s"; // Project-ID
    private static final String MODRINTH_VERSIONS_URL = "https://api.modrinth.com/v2/project/%s/version"; // %s := project id
    private static final String MODRINTH_VERSIONS_GAMEVERSIONS_PARAM = "game_versions"; // list of quoted game versions
    private static final String MODRINTH_VERSIONS_LOADERS_PARAM = "loaders"; // list of quoted mod loaders
    private static final String MODRINTH_VERSION_URL = "https://api.modrinth.com/v2/version/%s";
    private static final List<Map.Entry<String, String>> MODRINTH_HEADERS = List.of(Map.entry("User-Agent", "Tre5et/minecraft-launcher/0.1-ALPHA"));
    private static final String CURSEFORGE_SEARCH_URL = "https://api.curseforge.com/v1/mods/search";
    private static final List<Map.Entry<String, String>> CURSEFORGE_SEARCH_DEFAULT_PARAMS = List.of(Map.entry("gameId", "432"), Map.entry("sortField", "4"));
    private static final String CURSEFORGE_SEARCH_QUERY_PARAM = "searchFilter"; // search query
    private static final String CURSEFORGE_SEARCH_GAMEVERSION_PARAM = "gameVersion"; // game version
    private static final String CURSEFORGE_SEARCH_LOADER_PARAM = "modLoaderType"; // mod loader index (1=forge, 4=fabric)
    private static final String CURSEFORGE_SEARCH_LIMIT_PARAM = "pageSize";
    private static final String CURSEFORGE_SEARCH_OFFSET_PARAM = "index";
    private static final String CURSEFORGE_PROJECT_URL = "https://api.curseforge.com/v1/mods/%d"; // Mod-ID
    private static final String CURSEFORGE_VERSIONS_URL = "https://api.curseforge.com/v1/mods/%d/files"; // Mod-ID
    private static final String CURSEFORGE_VERSIONS_GAMEVERSION_PARAM = CURSEFORGE_SEARCH_GAMEVERSION_PARAM;
    private static final String CURSEFORGE_VERSIONS_LOADER_PARAM = CURSEFORGE_SEARCH_LOADER_PARAM;
    private static final String CURSEFORGE_VERSION_URL = "https://api.curseforge.com/v1/mods/%d/files/%d";
    private static final List<Map.Entry<String, String>> CURSEFORGE_HEADERS = List.of(Map.entry("Accept", "application/json"), Map.entry("x-api-key", "$2a$10$3rdQBL3FRS2RSSS4MF5F5uuOQpFr5flAzUCAdBvZDEfu1fIXFq.DW"));
    private static final String MOJANG_USER_PROFILE_URL = "https://api.mojang.com/users/profiles/minecraft/%s"; // Playername
    private static final String MOJANG_SESSION_PROFILE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/%s"; // UUID

    public static String getVersionManifestUrl() {
        return VERSION_MANIFEST_URL;
    }

    public static String getJavaRuntimeUrl() {
        return JAVA_RUNTIME_URL;
    }

    public static String getAssetUrl(String assetId) {
        if(assetId == null || assetId.length() < 2) {
            throw new IllegalArgumentException("Invalid assetId=" + assetId);
        }
        return String.format(ASSET_URL, assetId.substring(0,2), assetId);
    }

    public static String getFabricIndexUrl(String mcVersion) {
        if(mcVersion == null) {
            throw new IllegalArgumentException("Invalid mcVersion=" + mcVersion);
        }
        return String.format(FABRIC_INDEX_URL, mcVersion);
    }

    public static String getFabricVersionUrl(String mcVersion, String fabricVersion) {
        if(mcVersion == null || fabricVersion == null) {
            throw new IllegalArgumentException("Invalid arguments: mcVersion=" + mcVersion + ", fabricVersion=" + fabricVersion);
        }
        return String.format(FABRIC_VERSION_URL, mcVersion, fabricVersion);
    }

    public static String getFabricMavenUrl() {
        return FABRIC_MAVEN_URL;
    }

    public static String getFabricProfileUrl(String mcVersion, String fabricVersion) {
        if(mcVersion == null || fabricVersion == null) {
            throw new IllegalArgumentException("Invalid arguments: mcVersion=" + mcVersion + ", fabricVersion=" + fabricVersion);
        }
        return String.format(FABRIC_PROFILE_URL, mcVersion, fabricVersion);
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
        if(projectId == null) {
            throw new IllegalArgumentException("Invalid projectId="+projectId);
        }
        return String.format(MODRINTH_PROJECT_URL, projectId);
    }

    public static String getModrinthProjectVersionsUrl(String projectId) {
        if(projectId == null) {
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

    public static List<Map.Entry<String, String>> getModrinthHeaders() {
        return MODRINTH_HEADERS;
    }

    public static String getCurseforgeSearchUrl() {
        return CURSEFORGE_SEARCH_URL;
    }

    public static List<Map.Entry<String, String>> getCurseforgeSearchDefaultParams() {
        return CURSEFORGE_SEARCH_DEFAULT_PARAMS;
    }

    public static String getCurseforgeSearchQueryParam() {
        return CURSEFORGE_SEARCH_QUERY_PARAM;
    }

    public static String getCurseforgeSearchGameversionParam() {
        return CURSEFORGE_SEARCH_GAMEVERSION_PARAM;
    }

    public static String getCurseforgeSearchLoaderParam() {
        return CURSEFORGE_SEARCH_LOADER_PARAM;
    }

    public static String getCurseforgeSearchLimitParam() {
        return CURSEFORGE_SEARCH_LIMIT_PARAM;
    }

    public static String getCurseforgeSearchOffsetParam() {
        return CURSEFORGE_SEARCH_OFFSET_PARAM;
    }

    public static String getCurseforgeProjectUrl(long projectId)  {
        return String.format(CURSEFORGE_PROJECT_URL, projectId);
    }

    public static String getCurseforgeProjectVersionsUrl(long projectId) {
        return String.format(CURSEFORGE_VERSIONS_URL, projectId);
    }

    public static String getCurseforgeVersionsGameversionParam() {
        return CURSEFORGE_VERSIONS_GAMEVERSION_PARAM;
    }

    public static String getCurseforgeVersionsLoaderParam() {
        return CURSEFORGE_VERSIONS_LOADER_PARAM;
    }

    public static String getCurseforgeVersionUrl(long projectId, long versionId) {
        return String.format(CURSEFORGE_VERSION_URL, projectId, versionId);
    }

    public static List<Map.Entry<String, String>> getCurseforgeHeaders() {
        return CURSEFORGE_HEADERS;
    }

    public static String getMojangUserProfileUrl(String playerName) {
        if(playerName == null || playerName.isBlank()) {
            throw new IllegalArgumentException("Invalid playerName=" + playerName);
        }
        return String.format(MOJANG_USER_PROFILE_URL, playerName);
    }

    public static String getMojangSessionProfileUrl(String uuid) {
        if(uuid == null || uuid.isBlank()) {
            throw new IllegalArgumentException("Invalid uuid=" + uuid);
        }
        return String.format(MOJANG_SESSION_PROFILE_URL, uuid);
    }

}
