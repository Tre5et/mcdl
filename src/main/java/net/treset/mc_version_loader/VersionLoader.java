package net.treset.mc_version_loader;

import net.treset.mc_version_loader.files.Sources;
import net.treset.mc_version_loader.minecraft.*;
import net.treset.mc_version_loader.mods.ModData;
import net.treset.mc_version_loader.mods.curseforge.CurseforgeFile;
import net.treset.mc_version_loader.mods.curseforge.CurseforgeSearch;
import net.treset.mc_version_loader.mods.modrinth.ModrinthSearch;
import net.treset.mc_version_loader.mods.modrinth.ModrinthVersion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VersionLoader {
    public static void main(String[] args) {
        ModrinthSearch search = searchModrinth("vanillaconfig", List.of("1.19.4"), List.of("fabric"), 20, -1);
        List<ModrinthVersion> versions = getModrinthVersion(search.getHits().get(0).getProjectId(), search.getHits().get(0), List.of("1.19.4"), List.of("fabric"));
        CurseforgeSearch cfSearch = searchCurseforge("vanillaconfig", "1.19.4", 4, 20, 0);
        List<CurseforgeFile> cfVersions = getCurseforgeVersions(cfSearch.getData().get(0).getId(), cfSearch.getData().get(0), "1.19.4", 4);
        return;
    }

    public static List<MinecraftVersion> getVersions() {
        return MinecraftVersion.parseVersionManifest(Sources.getVersionManifestJson());
    }

    public static List<MinecraftVersion> getReleases() {
        List<MinecraftVersion> releases = new ArrayList<>();
        List<MinecraftVersion> minecraftVersions = getVersions();
        for(MinecraftVersion v : minecraftVersions) {
            if(v.isRelease()) {
                releases.add(v);
            }
        }
        return releases;
    }

    public static ModrinthSearch searchModrinth(String query, List<String> versions, List<String> loaders, int limit, int offset) {
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
                for(String v : versions) {
                    facets.append(String.format(Sources.getModrinthVersionsFacet(), v)).append(",");
                }
            }
            if(loaders != null && !loaders.isEmpty()) {
                for(String l : loaders) {
                    facets.append(String.format(Sources.getModrinthCategoryFacet(), l)).append(",");
                }
            }
            params.add(Map.entry(Sources.getModrinthSearchFacetsParam(), facets.substring(0, facets.length() - 1) + "]"));
        }
        return ModrinthSearch.fromJson(Sources.getFileFromHttpGet(Sources.getModrinthSearchUrl(), Sources.getModrinthHeaders(), params));
    }

    public static List<ModrinthVersion> getModrinthVersion(String modId, ModData parent, List<String> versions, List<String> modLoaders) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        if(versions != null && !versions.isEmpty()) {
            StringBuilder ver = new StringBuilder("[");
            for(String v : versions) {
                ver.append("\"").append(v).append("\",");
            }
            params.add(Map.entry(Sources.getModrinthVersionsGameversionsParam(), ver.substring(0, ver.length() - 1) + "]"));
        }
        if(modLoaders != null && !modLoaders.isEmpty()) {
            StringBuilder loaders = new StringBuilder("[");
            for(String l : modLoaders) {
                loaders.append("\"").append(l).append("\",");
            }
            params.add(Map.entry(Sources.getModrinthVersionsLoadersParam(), loaders.substring(0, loaders.length() - 1) + "]"));
        }
        return ModrinthVersion.parseModrinthVersions(Sources.getFileFromHttpGet(String.format(Sources.getModrinthVersionsUrl(), modId), Sources.getModrinthHeaders(), params), parent);
    }

    public static CurseforgeSearch searchCurseforge(String query, String gameVersion, int modLoader, int limit, int offset) {
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

    public static List<CurseforgeFile> getCurseforgeVersions(int modId, ModData parent, String gameVersion, int modLoader) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        if(gameVersion != null && !gameVersion.isBlank()) {
            params.add(Map.entry(Sources.getCurseforgeSearchGameversionParam(), gameVersion));
        }
        if(modLoader >= 0) {
            params.add(Map.entry(Sources.getCurseforgeSearchLoaderParam(), String.valueOf(modLoader)));
        }
        return CurseforgeFile.parseCurseforgeFiles(Sources.getFileFromHttpGet(String.format(Sources.getCurseforgeVersionUrl(), modId), Sources.getCurseforgeHeaders(), params), parent);
    }
 }