package net.treset.mc_version_loader.files;

import net.treset.mc_version_loader.exception.FileDownloadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class Sources {
    private static final String VERSION_MANIFEST_URL = "https://piston-meta.mojang.com/mc/game/version_manifest_v2.json";
    private static final String JAVA_RUNTIME_URL = "https://launchermeta.mojang.com/v1/products/java-runtime/2ec0cc96c44e5a76b9c8b7c39df7210883d12871/all.json";
    private static final String ASSETS_BASE_URL = "https://resources.download.minecraft.net/";
    private static final String FABRIC_LOADER_MANIFEST_URL = "https://meta.fabricmc.net/v2/versions/loader/%s";
    private static final String FABRIC_LOADER_VERSION_URL = "https://meta.fabricmc.net/v2/versions/loader/%s/%s";
    private static final String FABRIC_MAVEN_URL = "https://maven.fabricmc.net/";
    private static final String MODRINTH_SEARCH_URL = "https://api.modrinth.com/v2/search";
    private static final String MODRINTH_SEARCH_QUERY_PARAM = "query";
    private static final String MODRINTH_SEARCH_LIMIT_PARAM = "limit";
    private static final String MODRINTH_SEARCH_OFFSET_PARAM = "offset";
    private static final String MODRINTH_SEARCH_FACETS_PARAM = "facets";
    private static final String MODRINTH_CATEGORY_FACET = "[\"categories:%s\"]";
    private static final String MODRINTH_VERSIONS_FACET = "[\"versions:%s\"]";
    private static final String MODRINTH_PROJECT_URL = "https://api.modrinth.com/v2/project/%s"; // %s := project id
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
    private static final String CURSEFORGE_PROJECT_URL = "https://api.curseforge.com/v1/mods/%d"; // %d = modId
    private static final String CURSEFORGE_VERSIONS_URL = "https://api.curseforge.com/v1/mods/%d/files"; // %d = modId
    private static final String CURSEFORGE_VERSIONS_GAMEVERSION_PARAM = CURSEFORGE_SEARCH_GAMEVERSION_PARAM; // %s := game version
    private static final String CURSEFORGE_VERSIONS_LOADER_PARAM = CURSEFORGE_SEARCH_LOADER_PARAM; // %d := mod loader index (1=forge, 4=fabric)
    private static final String CURSEFORGE_VERSION_URL = "https://api.curseforge.com/v1/mods/%d/files/%d";
    private static final List<Map.Entry<String, String>> CURSEFORGE_HEADERS = List.of(Map.entry("Accept", "application/json"), Map.entry("x-api-key", "$2a$10$3rdQBL3FRS2RSSS4MF5F5uuOQpFr5flAzUCAdBvZDEfu1fIXFq.DW"));
    private static final String MOJANG_USER_PROFILE_URL = "https://api.mojang.com/users/profiles/minecraft/%s"; // %s := name
    private static final String MOJANG_SESSION_PROFILE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/%s"; // %s := uuid

    public static String getFabricMavenUrl() {
        return FABRIC_MAVEN_URL;
    }

    /**
     * Gets the minecraft version manifest as string
     * @return minecraft version manifest as string
     * @throws FileDownloadException if the file could not be downloaded
     */
    public static String getVersionManifestJson() throws FileDownloadException {
        return getFileFromUrl(VERSION_MANIFEST_URL);
    }

    /**
     * Gets the java runtime manifest as string
     * @return java runtime manifest as string
     * @throws FileDownloadException if the file could not be downloaded
     */
    public static String getJavaRuntimeJson() throws FileDownloadException {
        return getFileFromUrl(JAVA_RUNTIME_URL);
    }

    public static String getAssetsBaseUrl() {
        return ASSETS_BASE_URL;
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

    public static String getModrinthCategoryFacet() {
        return MODRINTH_CATEGORY_FACET;
    }

    public static String getModrinthVersionsFacet() {
        return MODRINTH_VERSIONS_FACET;
    }

    public static String getModrinthProjectUrl() {
        return MODRINTH_PROJECT_URL;
    }

    public static String getModrinthVersionsUrl() {
        return MODRINTH_VERSIONS_URL;
    }

    public static String getModrinthVersionsGameversionsParam() {
        return MODRINTH_VERSIONS_GAMEVERSIONS_PARAM;
    }

    public static String getModrinthVersionsLoadersParam() {
        return MODRINTH_VERSIONS_LOADERS_PARAM;
    }

    public static String getModrinthVersionUrl() {
        return MODRINTH_VERSION_URL;
    }

    public static List<Map.Entry<String, String>> getCurseforgeHeaders() {
        return CURSEFORGE_HEADERS;
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

    public static String getCurseforgeProjectUrl() {
        return CURSEFORGE_PROJECT_URL;
    }

    public static String getCurseforgeVersionsUrl() {
        return CURSEFORGE_VERSIONS_URL;
    }

    public static String getCurseforgeVersionsGameversionParam() {
        return CURSEFORGE_VERSIONS_GAMEVERSION_PARAM;
    }

    public static String getCurseforgeVersionsLoaderParam() {
        return CURSEFORGE_VERSIONS_LOADER_PARAM;
    }

    public static String getCurseforgeVersionUrl() {
        return CURSEFORGE_VERSION_URL;
    }

    public static String getMojangUserProfileUrl() {
        return MOJANG_USER_PROFILE_URL;
    }

    public static String getMojangSessionProfileUrl() {
        return MOJANG_SESSION_PROFILE_URL;
    }

    /**
     * Gets the fabric loader manifest for the specified minecraft version
     * @param version minecraft version
     * @return the fabric manifest as string
     * @throws FileDownloadException if the file can not be downloaded
     */
    public static String getFabricForMinecraftVersion(String version) throws FileDownloadException {
        return getFileFromHttpGet(String.format(FABRIC_LOADER_MANIFEST_URL, version), List.of(), List.of());
    }

    /**
     * Gets the fabric loader manifest for the specified minecraft version and fabric version
     * @param mcVersion minecraft version
     * @param fabricVersion fabric version
     * @return the fabric manifest as string
     * @throws FileDownloadException if the file can not be downloaded
     */
    public static String getFabricVersion(String mcVersion, String fabricVersion) throws FileDownloadException {
        return getFileFromHttpGet(String.format(FABRIC_LOADER_VERSION_URL, mcVersion, fabricVersion),List.of(), List.of());
    }

    /**
     * Converts the contents of a file from the specified http get url to string
     * @param getUrl url to get the file from
     * @param headers headers to send with the request
     * @param params parameters to send with the request
     * @return the contents of the file as string
     * @throws FileDownloadException if the file can not be downloaded
     */
    public static String getFileFromHttpGet(String getUrl, List<Map.Entry<String, String>> headers, List<Map.Entry<String, String>> params) throws FileDownloadException {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request;
        StringBuilder url = new StringBuilder();
        url.append(getUrl).append("?");
        for(Map.Entry<String, String> p : params) {
            url.append(URLEncoder.encode(p.getKey(), StandardCharsets.UTF_8))
                    .append("=").append(URLEncoder.encode(p.getValue(), StandardCharsets.UTF_8))
                    .append("&");
        }
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(new URI(url.substring(0, url.length() - 1)));
            headers.forEach(h -> requestBuilder.header(h.getKey(), h.getValue()));
            request = requestBuilder.build();
        } catch (URISyntaxException e) {
            throw new FileDownloadException("Unable to build download URI: url=" + getUrl, e);
        }

        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException e) {
            throw new FileDownloadException("Unable to download file: url=" + getUrl, e);
        }
    }

    /**
     * Converts the contents of a file from the specified url to string
     * @param url url to get the file from
     * @return the contents of the file as string
     * @throws FileDownloadException if the file can not be downloaded
     */
    public static String getFileFromUrl(String url) throws FileDownloadException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
            StringBuilder stringBuilder = new StringBuilder();

            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(inputLine);
                stringBuilder.append(System.lineSeparator());
            }

            bufferedReader.close();
            return stringBuilder.toString().trim();
        } catch (IOException e) {
            throw new FileDownloadException("Unable to download file: url=" + url, e);
        }
    }

}
