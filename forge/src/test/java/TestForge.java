import net.treset.mcdl.forge.ForgeInstallProfile;
import net.treset.mcdl.forge.ForgeVersion;
import net.treset.mcdl.forge.MinecraftForge;
import net.treset.mcdl.java.JavaFile;
import net.treset.mcdl.java.JavaRuntimeRelease;
import net.treset.mcdl.java.MinecraftJava;
import net.treset.mcdl.minecraft.MinecraftDL;
import net.treset.mcdl.minecraft.MinecraftVersion;
import net.treset.mcdl.minecraft.MinecraftVersionDetails;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestForge {

    public static Stream<Arguments> testForge() {
        return Stream.of(
                Arguments.of("1.21", "1.21-51.0.33"),
                Arguments.of("1.16.5", "1.16.5-36.0.48"),
                Arguments.of("1.12.2", "1.12.2-14.23.5.2851")
        );
    }

    @ParameterizedTest
    @MethodSource("testForge")
    public void testForge(String mcVersion, String forgeVersion) {
        ForgeVersion forgeData = assertDoesNotThrow(() -> MinecraftForge.getForgeVersion(forgeVersion));
        ForgeInstallProfile profile = assertDoesNotThrow(() -> MinecraftForge.getForgeInstallProfile(forgeVersion));

        File mcClient = new File("download/mc-" + mcVersion + ".jar");
        List<MinecraftVersion> versions = assertDoesNotThrow(MinecraftDL::getVersions);
        MinecraftVersion version = assertDoesNotThrow(() -> versions.stream().filter(v -> v.getId().equals(mcVersion)).findFirst().get());
        MinecraftVersionDetails details = assertDoesNotThrow(() -> MinecraftDL.getVersionDetails(version.getUrl()));
        if(!mcClient.isFile()) {
            assertDoesNotThrow(() -> MinecraftDL.downloadVersionDownload(details.getDownloads().getClient(), mcClient));
        }

        File java = new File("download/java-" + mcVersion);
        if (!java.isDirectory()) {
            assertDoesNotThrow(java::mkdirs);
            JavaRuntimeRelease release = assertDoesNotThrow(() -> MinecraftJava.getJavaRuntimes().get("windows-x64").get(details.getJavaVersion().component).get(0));
            List<JavaFile> files = assertDoesNotThrow(() -> MinecraftJava.getJavaFiles(release.getManifest().getUrl()));
            assertDoesNotThrow(() -> MinecraftJava.downloadJavaFiles(java, files));
        }

        File libraries = new File("download/libraries-" + forgeVersion);
        if(libraries.isDirectory()) {
            assertDoesNotThrow(libraries::delete);
        }
        assertDoesNotThrow(libraries::mkdirs);

        assertDoesNotThrow(() -> MinecraftForge.createForgeClient(forgeVersion, libraries, profile, mcClient, new File(java, "bin/java.exe")));
        assertTrue(new File(libraries, "net/minecraftforge/forge/" + forgeVersion).isDirectory(), "Client jar does not exist");

        File natives = new File("download/natives-" + forgeVersion);
        if(natives.isDirectory()) {
            assertDoesNotThrow(natives::delete);
        }
        assertDoesNotThrow(natives::mkdirs);

        assertDoesNotThrow(() -> MinecraftForge.downloadForgeLibraries(libraries, forgeVersion, profile.getLibraries(), natives));
        assertTrue(libraries.isDirectory(), "Libraries directory does not exist");
        assertTrue(libraries.listFiles().length > 0, "Libraries directory is empty");

    }
}
