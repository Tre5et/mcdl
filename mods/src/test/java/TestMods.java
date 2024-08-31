

import net.treset.mcdl.mods.*;
import net.treset.mcdl.mods.curseforge.CurseforgeFile;
import net.treset.mcdl.mods.modrinth.ModrinthVersion;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class TestMods {
    private static Stream<Arguments> testMod() {
        return Stream.of(
                Arguments.of("fabric-api", List.of("1.21"), List.of("fabric", "quilt")),
                Arguments.of("macaws-furniture", List.of("1.16.5"), List.of("forge")),
                Arguments.of("litematica", List.of("1.19.4"), List.of("fabric"))
        );
    }

    @ParameterizedTest
    @MethodSource
    public void testMod(String query, List<String> versions, List<String> loaders) {
        ModsDL.setModrinthUserAgent("test");
        ModsDL.setCurseforgeApiKey("$2a$10$3rdQBL3FRS2RSSS4MF5F5uuOQpFr5flAzUCAdBvZDEfu1fIXFq.DW");

        ModData mod = testSearch(query, versions, loaders);
        textModPreference(mod);
        testVersions(mod, versions, loaders);
        testModrinthVersions(mod, versions, loaders);
        testCurseforgeVersions(mod, versions, loaders);
        testDependencies(mod, versions, loaders);
        testVersionPreference(mod, versions, loaders);
    }

    public static ModData testSearch(String query, List<String> versions, List<String> loaders) {
        List<ModData> mods = assertDoesNotThrow(() -> ModsDL.searchCombinedMods(
                query,
                versions,
                loaders,
                20,
                0
        ));

        assertFalse(mods.isEmpty(), "no mods: " + query);

        ModData mod = mods.stream().filter(m -> m.getSlug().equals(query)).findFirst().orElse(null);
        assertNotNull(mod, "no " + query + " mod");

        assertInstanceOf(CombinedModData.class, mod, "not CombinedModData: " + mod);
        return mod;
    }

    public static void testVersions(ModData mod, List<String> versions, List<String> loaders) {
        mod.setVersionConstraints(versions, loaders, null);
        List<ModVersionData> modVersions = assertDoesNotThrow(mod::getVersions);
        assertFalse(modVersions.isEmpty(), "no versions: " + mod);
        for(ModVersionData version: modVersions) {
            assertTrue(modVersions.stream().allMatch(v -> versions.stream().anyMatch(ver -> v.getGameVersions().contains(ver)) && loaders.stream().anyMatch(loader -> v.getModLoaders().contains(loader))), "wrong version or loader: " + modVersions);
        }
    }

    public static void testModrinthVersions(ModData mod, List<String> versions, List<String> loaders) {
        mod.setVersionConstraints(versions, loaders, List.of(ModProvider.MODRINTH));
        List<ModVersionData> modVersions = assertDoesNotThrow(mod::getVersions);
        assertFalse(modVersions.isEmpty(), "no versions: " + mod);

        for(ModVersionData version : modVersions) {
            assertInstanceOf(ModrinthVersion.class, version, "not ModrinthVersion: " + version);
            assertTrue(versions.stream().anyMatch(v -> version.getGameVersions().contains(v)) && loaders.stream().anyMatch(l -> version.getModLoaders().contains(l)), "wrong version or loader: " + version);
        }
    }

    public static void testCurseforgeVersions(ModData mod, List<String> versions, List<String> loaders) {
        mod.setVersionConstraints(versions, loaders, List.of(ModProvider.CURSEFORGE));
        List<ModVersionData> modVersions = assertDoesNotThrow(mod::getVersions);
        assertFalse(modVersions.isEmpty(), "no versions: " + mod);

        for(ModVersionData version : modVersions) {
            assertInstanceOf(CurseforgeFile.class, version, "not CurseforgeFile: " + version);
            assertTrue(versions.stream().anyMatch(v -> version.getGameVersions().contains(v)) && loaders.stream().anyMatch(l -> version.getModLoaders().contains(l)), "wrong version or loader: " + version);
        }
    }

    public static void textModPreference(ModData mod) {
        if(!(mod instanceof CombinedModData)) {
            System.out.println("Mod preference test skipped: not CombinedModData " + mod);
            return;
        }

        mod.setVersionProviders(List.of(ModProvider.CURSEFORGE, ModProvider.MODRINTH));
        assertTrue(mod.getUrl().contains("curseforge"), "wrong url 1: " + mod.getUrl());

        mod.setVersionProviders(List.of(ModProvider.MODRINTH, ModProvider.CURSEFORGE));
        assertTrue(mod.getUrl().contains("modrinth"), "wrong url 2: " + mod.getUrl());

        mod.setVersionProviders(List.of(ModProvider.CURSEFORGE));
        assertTrue(mod.getUrl().contains("curseforge"), "wrong url 3: " + mod.getUrl());

        mod.setVersionProviders(List.of(ModProvider.MODRINTH));
        assertTrue(mod.getUrl().contains("modrinth"), "wrong url 4: " + mod.getUrl());
    }

    public static void testVersionPreference(ModData mod, List<String> versions, List<String> loaders) {
        mod.setVersionConstraints(List.of("1.20.4"), List.of("fabric", "quilt"), List.of(ModProvider.CURSEFORGE, ModProvider.MODRINTH));
        List<ModVersionData> modVersions = assertDoesNotThrow(mod::getVersions);
        assertFalse(modVersions.isEmpty(), "no versions: " + mod);

        for(ModVersionData version: modVersions) {
            if (!(version instanceof CombinedModVersion)) {
                System.out.println("version preference test skipped: not CombinedModVersion: " + version);
                continue;
            }

            version.setDownloadProviders(List.of(ModProvider.CURSEFORGE, ModProvider.MODRINTH));
            assertTrue(version.getDownloadUrl().contains("forgecdn"), "wrong download url 1: " + version.getDownloadUrl());

            version.setDownloadProviders(List.of(ModProvider.MODRINTH, ModProvider.CURSEFORGE));
            assertTrue(version.getDownloadUrl().contains("modrinth"), "wrong download url 2: " + version.getDownloadUrl());

            version.setDownloadProviders(List.of(ModProvider.CURSEFORGE));
            assertTrue(version.getDownloadUrl().contains("forgecdn"), "wrong download url 3: " + version.getDownloadUrl());

            version.setDownloadProviders(List.of(ModProvider.MODRINTH));
            assertTrue(version.getDownloadUrl().contains("modrinth"), "wrong download url 4: " + version.getDownloadUrl());
        }
    }

    public static void testDependencies(ModData mod, List<String> versions, List<String> loaders) {
        mod.setVersionConstraints(versions, loaders, null);
        List<ModVersionData> modVersions = assertDoesNotThrow(mod::getVersions);
        assertFalse(modVersions.isEmpty(), "no versions: " + mod);


        for(ModVersionData version: modVersions) {
            version.setDependencyConstraints(versions, loaders, null);
            List<ModVersionData> deps1 = assertDoesNotThrow(version::getRequiredDependencies);
            for(ModVersionData dep1: deps1) {
                assertTrue(versions.stream().anyMatch(v -> dep1.getGameVersions().contains(v)) && loaders.stream().anyMatch(l -> dep1.getModLoaders().contains(l)), "wrong dependency version or loader: " + dep1);
            }

            version.setDownloadProviders(List.of(ModProvider.MODRINTH));
            List<ModVersionData> deps2 = assertDoesNotThrow(version::getRequiredDependencies);
            for(ModVersionData dep2: deps2) {
                assertInstanceOf(ModrinthVersion.class, dep2, "not modrinth dependency: " + dep2);
                assertTrue(versions.stream().anyMatch(v -> dep2.getGameVersions().contains(v)) && loaders.stream().anyMatch(l -> dep2.getModLoaders().contains(l)), "wrong modrinth dependency version or loader: " + dep2);
            }

            version.setDownloadProviders(List.of(ModProvider.CURSEFORGE));
            List<ModVersionData> deps3 = assertDoesNotThrow(version::getRequiredDependencies);
            for(ModVersionData dep3: deps3) {
                assertInstanceOf(CurseforgeFile.class, dep3, "not curseforge dependency: " + dep3);
                assertTrue(versions.stream().anyMatch(v -> dep3.getGameVersions().contains(v)) && loaders.stream().anyMatch(l -> dep3.getModLoaders().contains(l)), "wrong curseforge dependency version or loader: " + dep3);
            }
        }
    }
}
