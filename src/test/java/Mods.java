import net.treset.mc_version_loader.exception.FileDownloadException;
import net.treset.mc_version_loader.mods.*;
import net.treset.mc_version_loader.mods.curseforge.CurseforgeFile;
import net.treset.mc_version_loader.mods.modrinth.ModrinthVersion;

import java.util.List;

public class Mods {
    public static void testMods() {
        MinecraftMods.setModrinthUserAgent("test");
        MinecraftMods.setCurseforgeApiKey("$2a$10$3rdQBL3FRS2RSSS4MF5F5uuOQpFr5flAzUCAdBvZDEfu1fIXFq.DW");

        ModData mod = testSearch();
        testVersions(mod);
        testModrinthVersions(mod);
        testCurseforgeVersions(mod);
        testDependencies();
        textModPreference(mod);
        testVersionPreference(mod);
    }

    public static ModData testSearch() {
        try {
            List<ModData> mods = MinecraftMods.searchCombinedMods(
                    "fabric api",
                    List.of("1.20.4"),
                    List.of("fabric", "quilt"),
                    20,
                    0
            );

            if(mods.isEmpty()) {
                System.err.println("Mods:testSearch FAILED: no mods" );
                return null;
            }

            ModData mod = mods.stream().filter(m -> m.getSlug().equals("fabric-api")).findFirst().orElse(null);
            if(mod == null) {
                System.err.println("Mods:testSearch FAILED: no fabric api mod" );
                return null;
            }
            if(mod instanceof CombinedModData) {
                if(mod.getProjectIds().containsAll(List.of("P7dR8mSH", "306612"))) {
                    System.out.println("Mods:testSearch PASSED");
                } else {
                    System.err.println("Mods:testSearch FAILED: wrong project ids: " + mod.getProjectIds());
                }
            } else {
                System.err.println("Mods:testSearch FAILED: not CombinedModData" );
            }
            return mod;
        } catch (Exception e) {
            System.err.println("Mods:testSearch FAILED: search error" + e.getMessage());
            return null;
        }
    }

    public static void testVersions(ModData mod) {
        if(mod == null) {
            System.err.println("Mods:testVersions FAILED: no mod");
            return;
        }

        mod.setVersionConstraints(List.of("1.20.4"), List.of("fabric", "quilt"), null);
        try {
            List<ModVersionData> versions = mod.getVersions();
            if(versions.isEmpty()) {
                System.err.println("Mods:testVersions FAILED: no versions");
                return;
            }
            ModVersionData version = versions.get(0);
            if(!(version instanceof CombinedModVersion)) {
                System.err.println("Mods:testVersions FAILED: not CombinedModVersion");
                return;
            }
            if(versions.stream().allMatch(v -> v.getGameVersions().contains("1.20.4") && v.getModLoaders().contains("fabric"))) {
                System.out.println("Mods:testVersions PASSED");
            } else {
                System.err.println("Mods:testVersions FAILED: wrong version: " + versions);
            }
        } catch (Exception e) {
            System.err.println("Mods:testVersions FAILED: error getting versions " + e.getMessage());
        }
    }

    public static void testModrinthVersions(ModData mod) {
        if(mod == null) {
            System.err.println("Mods:testModrinthVersions FAILED: no mod");
            return;
        }

        mod.setVersionConstraints(List.of("1.20.4"), List.of("fabric", "quilt"), List.of(ModProvider.MODRINTH));
        try {
            List<ModVersionData> versions = mod.getVersions();
            if(versions.isEmpty()) {
                System.err.println("Mods:testModrinthVersions FAILED: no versions");
                return;
            }

            if(versions.stream().anyMatch(v -> !(v instanceof ModrinthVersion))) {
                System.err.println("Mods:testModrinthVersions FAILED: not ModrinthVersion: " + versions);
                return;
            }
            if(versions.stream().allMatch(v -> v.getGameVersions().contains("1.20.4") && v.getModLoaders().contains("fabric"))) {
                System.out.println("Mods:testModrinthVersions PASSED");
            } else {
                System.err.println("Mods:testModrinthVersions FAILED: wrong version: " + versions);
            }
        } catch (Exception e) {
            System.err.println("Mods:testModrinthVersions FAILED: error getting versions " + e.getMessage());
        }
    }

    public static void testCurseforgeVersions(ModData mod) {
        if(mod == null) {
            System.err.println("Mods:testCurseforgeVersions FAILED: no mod");
            return;
        }

        mod.setVersionConstraints(List.of("1.20.4"), List.of("fabric", "quilt"), List.of(ModProvider.CURSEFORGE));
        try {
            List<ModVersionData> versions = mod.getVersions();
            if(versions.isEmpty()) {
                System.err.println("Mods:testCurseforgeVersions FAILED: no versions");
                return;
            }

            if(versions.stream().anyMatch(v -> !(v instanceof CurseforgeFile))) {
                System.err.println("Mods:testCurseforgeVersions FAILED: not CurseforgeFile: " + versions);
                return;
            }
            if(versions.stream().allMatch(v -> v.getGameVersions().contains("1.20.4") && v.getModLoaders().contains("fabric"))) {
                System.out.println("Mods:testCurseforgeVersions PASSED");
            } else {
                System.err.println("Mods:testCurseforgeVersions FAILED: wrong version: " + versions);
            }
        } catch (Exception e) {
            System.err.println("Mods:testCurseforgeVersions FAILED: error getting versions " + e.getMessage());
        }
    }

    public static void testDependencies() {
        try {
            List<ModData> mods = MinecraftMods.searchCombinedMods(
                    "ridehud",
                    List.of("1.20.4"),
                    List.of("fabric"),
                    20,
                    0
            );
            if(mods.isEmpty()) {
                System.err.println("Mods:testDependencies FAILED: no mods" );
                return;
            }
            ModData mod = mods.get(0);
            mod.setVersionConstraints(List.of("1.20.4"), List.of("fabric"), null);
            List<ModVersionData> versions = mod.getVersions();
            if(versions.isEmpty()) {
                System.err.println("Mods:testDependencies FAILED: no versions" );
                return;
            }
            ModVersionData version = versions.get(0);

            version.setDependencyConstraints(List.of("1.20.4"), List.of("fabric"), null);
            List<ModVersionData> deps1 = version.getRequiredDependencies();
            if(deps1.isEmpty()) {
                System.err.println("Mods:testDependencies FAILED: no dependencies" );
                return;
            }
            ModVersionData dep1 = deps1.get(0);
            if(!(dep1 instanceof CombinedModVersion)) {
                System.err.println("Mods:testDependencies FAILED: not combined dependency: " + deps1.get(0));
                return;
            }
            if(!dep1.getGameVersions().contains("1.20.4") || !dep1.getModLoaders().contains("fabric")) {
                System.err.println("Mods:testDependencies FAILED: wrong dependency: " + dep1);
                return;
            }

            version.setDownloadProviders(List.of(ModProvider.MODRINTH));
            List<ModVersionData> deps2 = version.getRequiredDependencies();
            if(deps2.isEmpty()) {
                System.err.println("Mods:testDependencies FAILED: no modrinth dependencies" );
                return;
            }
            ModVersionData dep2 = deps2.get(0);
            if(!(dep2 instanceof ModrinthVersion)) {
                System.err.println("Mods:testDependencies FAILED: not modrinth dependency: " + deps2.get(0));
                return;
            }
            if(!dep2.getGameVersions().contains("1.20.4") || !dep2.getModLoaders().contains("fabric")) {
                System.err.println("Mods:testDependencies FAILED: wrong modrinth dependency: " + dep2);
                return;
            }

            version.setDownloadProviders(List.of(ModProvider.CURSEFORGE));
            List<ModVersionData> deps3 = version.getRequiredDependencies();
            if(deps3.isEmpty()) {
                System.err.println("Mods:testDependencies FAILED: no curseforge dependencies" );
                return;
            }
            ModVersionData dep3 = deps3.get(0);
            if(!(dep3 instanceof CurseforgeFile)) {
                System.err.println("Mods:testDependencies FAILED: not curseforge dependency: " + deps3.get(0));
                return;
            }
            if(!dep3.getGameVersions().contains("1.20.4") || !dep3.getModLoaders().contains("fabric")) {
                System.err.println("Mods:testDependencies FAILED: wrong curseforge dependency: " + dep3);
                return;
            }

            System.out.println("Mods:testDependencies PASSED");
        } catch (FileDownloadException e) {
            System.err.println("Mods:testDependencies FAILED: search error" + e);
        }
    }

    public static void textModPreference(ModData mod) {
        if(mod == null) {
            System.err.println("Mods:testModPreference FAILED: no mod");
            return;
        }

        if(!(mod instanceof CombinedModData)) {
            System.err.println("Mods:testModPreference FAILED: not CombinedModData");
            return;
        }

        mod.setVersionProviders(List.of(ModProvider.CURSEFORGE, ModProvider.MODRINTH));
        if(!mod.getUrl().contains("curseforge")) {
            System.err.println("Mods:testModPreference FAILED: wrong url 1: " + mod.getUrl());
            return;
        }

        mod.setVersionProviders(List.of(ModProvider.MODRINTH, ModProvider.CURSEFORGE));
        if(!mod.getUrl().contains("modrinth")) {
            System.err.println("Mods:testModPreference FAILED: wrong url 2: " + mod.getUrl());
            return;
        }

        mod.setVersionProviders(List.of(ModProvider.CURSEFORGE));
        if(!mod.getUrl().contains("curseforge")) {
            System.err.println("Mods:testModPreference FAILED: wrong url 3: " + mod.getUrl());
            return;
        }

        mod.setVersionProviders(List.of(ModProvider.MODRINTH));
        if(!mod.getUrl().contains("modrinth")) {
            System.err.println("Mods:testModPreference FAILED: wrong url 4: " + mod.getUrl());
            return;
        }

        System.out.println("Mods:testModPreference PASSED");
    }

    public static void testVersionPreference(ModData mod) {
        if(mod == null) {
            System.err.println("Mods:testVersionPreference FAILED: no mod");
            return;
        }

        mod.setVersionConstraints(List.of("1.20.4"), List.of("fabric", "quilt"), List.of(ModProvider.CURSEFORGE, ModProvider.MODRINTH));
        try {
            List<ModVersionData> versions = mod.getVersions();
            if(versions.isEmpty()) {
                System.err.println("Mods:testVersionPreference FAILED: no versions");
                return;
            }
            ModVersionData version = versions.get(0);
            if(!(version instanceof CombinedModVersion)) {
                System.err.println("Mods:testVersionPreference FAILED: not CombinedModVersion");
                return;
            }

            version.setDownloadProviders(List.of(ModProvider.CURSEFORGE, ModProvider.MODRINTH));
            if(!version.getDownloadUrl().contains("forgecdn")) {
                System.err.println("Mods:testVersionPreference FAILED: wrong download url 1: " + version.getDownloadUrl());
                return;
            }

            version.setDownloadProviders(List.of(ModProvider.MODRINTH, ModProvider.CURSEFORGE));
            if(!version.getDownloadUrl().contains("modrinth")) {
                System.err.println("Mods:testVersionPreference FAILED: wrong download url 2: " + version.getDownloadUrl());
                return;
            }

            version.setDownloadProviders(List.of(ModProvider.CURSEFORGE));
            if(!version.getDownloadUrl().contains("forgecdn")) {
                System.err.println("Mods:testVersionPreference FAILED: wrong download url 3: " + version.getDownloadUrl());
                return;
            }

            version.setDownloadProviders(List.of(ModProvider.MODRINTH));
            if(!version.getDownloadUrl().contains("modrinth")) {
                System.err.println("Mods:testVersionPreference FAILED: wrong download url 4: " + version.getDownloadUrl());
                return;
            }

            System.out.println("Mods:testVersionPreference PASSED");
        } catch (Exception e) {
            System.err.println("Mods:testVersionPreference FAILED: error getting versions " + e.getMessage());
        }
    }
}
