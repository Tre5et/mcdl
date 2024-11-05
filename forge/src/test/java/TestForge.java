import dev.treset.mcdl.forge.ForgeDL;
import dev.treset.mcdl.forge.ForgeInstallerExecutor;
import dev.treset.mcdl.forge.ForgeMetaVersion;
import dev.treset.mcdl.java.JavaFile;
import dev.treset.mcdl.java.JavaRuntimeRelease;
import dev.treset.mcdl.java.JavaDL;
import dev.treset.mcdl.minecraft.MinecraftDL;
import dev.treset.mcdl.minecraft.MinecraftVersion;
import dev.treset.mcdl.minecraft.MinecraftProfile;
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
                Arguments.of("1.21.3", "1.21.3-53.0.8"),
                Arguments.of("1.12.2", "1.12.2-14.23.5.2859"),
                Arguments.of("1.5.2", "1.5.2-7.8.0.699")
        );
    }

    @ParameterizedTest
    @MethodSource("testForge")
    public void testForge(String mcVersion, String forgeVersion) {
        List<ForgeMetaVersion> metaVersions = assertDoesNotThrow(() -> ForgeDL.getForgeVersions());

        File mcClient = new File("download/mc-" + mcVersion + ".jar");
        List<MinecraftVersion> versions = assertDoesNotThrow(MinecraftDL::getVersions);
        MinecraftVersion version = assertDoesNotThrow(() -> versions.stream().filter(v -> v.getId().equals(mcVersion)).findFirst().get());
        MinecraftProfile profile = assertDoesNotThrow(() -> MinecraftDL.getProfile(version.getUrl()));

        File java = new File("download/java-" + mcVersion);
        if (!java.isDirectory()) {
            assertDoesNotThrow(java::mkdirs);
            JavaRuntimeRelease release = assertDoesNotThrow(() -> JavaDL.getJavaRuntimes().get("windows-x64").get(profile.getJavaVersion().component).get(0));
            List<JavaFile> files = assertDoesNotThrow(() -> JavaDL.getJavaFiles(release.getManifest().getUrl()));
            assertDoesNotThrow(() -> JavaDL.downloadJavaFiles(files, java));
        }

        File libraries = new File("download/libraries-" + forgeVersion);
        if(libraries.isDirectory()) {
            assertDoesNotThrow(libraries::delete);
        }
        assertDoesNotThrow(libraries::mkdirs);

        ForgeInstallerExecutor installer = new ForgeInstallerExecutor(forgeVersion);
        assertDoesNotThrow(() -> installer.install(
                libraries,
                new File(System.getenv("JAVA_HOME"), "bin/java.exe"),
                status -> {
                    System.out.println("Status: " + status.getCurrentFile());
                }
        ));

        assertTrue(new File(libraries, "net/minecraftforge/forge/" + forgeVersion).isDirectory(), "Client jar does not exist");

        File natives = new File("download/natives-" + forgeVersion);
        if(natives.isDirectory()) {
            assertDoesNotThrow(natives::delete);
        }
        assertDoesNotThrow(natives::mkdirs);

        assertTrue(libraries.isDirectory(), "Libraries directory does not exist");
        assertTrue(libraries.listFiles().length > 1, "Libraries directory is empty");
    }
}
