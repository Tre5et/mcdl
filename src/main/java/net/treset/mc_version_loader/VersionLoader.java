package net.treset.mc_version_loader;

import net.treset.mc_version_loader.fabric.FabricVersionDetails;
import net.treset.mc_version_loader.files.*;
import net.treset.mc_version_loader.format.FormatUtils;
import net.treset.mc_version_loader.launcher.LauncherMod;
import net.treset.mc_version_loader.minecraft.MinecraftVersion;
import net.treset.mc_version_loader.minecraft.MinecraftVersionDetails;
import net.treset.mc_version_loader.mods.CombinedModData;
import net.treset.mc_version_loader.mods.ModData;
import net.treset.mc_version_loader.mods.ModVersionData;
import net.treset.mc_version_loader.mods.ModVersionType;
import net.treset.mc_version_loader.mods.curseforge.CurseforgeFile;
import net.treset.mc_version_loader.mods.curseforge.CurseforgeFiles;
import net.treset.mc_version_loader.mods.curseforge.CurseforgeMod;
import net.treset.mc_version_loader.mods.curseforge.CurseforgeSearch;
import net.treset.mc_version_loader.mods.modrinth.ModrinthSearch;
import net.treset.mc_version_loader.mods.modrinth.ModrinthSearchHit;
import net.treset.mc_version_loader.mods.modrinth.ModrinthVersion;

import java.io.File;
import java.util.*;

public class VersionLoader {
    public static void main(String[] args) {
        MinecraftVersionDetails details = MinecraftVersionDetails.fromJson(Sources.getFileFromUrl("https://piston-meta.mojang.com/v1/packages/23b6e0e0d0f87da36075a4290cd98df1a76e2415/1.18.2.json"));
        List<String> libs = MinecraftVersionFileDownloader.downloadVersionLibraries(details.getLibraries(), new File("./download/libraries"), List.of());
        FabricVersionDetails fabricDetails = FabricVersionDetails.fromJson(Sources.getFabricVersion("1.19.4", "0.14.19"));
        List<String> fabricLibs = FabricFileDownloader.downloadFabricLibraries(new File("./download/libraries"), fabricDetails.getLauncherMeta().getLibraries().getCommon());
        return;
    }

    public static List<MinecraftVersion> getVersions() {
        return MinecraftVersion.fromVersionManifest(Sources.getVersionManifestJson());
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

    public static List<ModData> searchCombinedMods(String query, String gameVersion, String modLoader, int limit, int offset) {
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
        return combinedMods;
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
        return ModrinthVersion.fromJsonArray(Sources.getFileFromHttpGet(String.format(Sources.getModrinthVersionsUrl(), modId), Sources.getModrinthHeaders(), params), parent);
    }

    public static ModrinthVersion getModrinthVersion(String versionId, ModData parent) {
        return ModrinthVersion.fromJson(Sources.getFileFromHttpGet(String.format(Sources.getModrinthVersionUrl(), versionId), Sources.getModrinthHeaders(), List.of()), parent);
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

    public static CurseforgeFiles getCurseforgeVersions(int modId, ModData parent, String gameVersion, int modLoader) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        if(gameVersion != null && !gameVersion.isBlank()) {
            params.add(Map.entry(Sources.getCurseforgeSearchGameversionParam(), gameVersion));
        }
        if(modLoader >= 0) {
            params.add(Map.entry(Sources.getCurseforgeSearchLoaderParam(), String.valueOf(modLoader)));
        }
        return CurseforgeFiles.fromJson(Sources.getFileFromHttpGet(String.format(Sources.getCurseforgeVersionsUrl(), modId), Sources.getCurseforgeHeaders(), params), parent);
    }

    public static CurseforgeFile getCurseforgeVersion(int modId, int versionId) {
        return CurseforgeFile.fromJson(Sources.getFileFromHttpGet(String.format(Sources.getCurseforgeVersionUrl(), modId, versionId), Sources.getCurseforgeHeaders(), List.of()));
    }
 }