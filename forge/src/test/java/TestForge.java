import net.treset.mcdl.forge.*;
import net.treset.mcdl.java.JavaFile;
import net.treset.mcdl.java.JavaRuntimeRelease;
import net.treset.mcdl.java.JavaDL;
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
                Arguments.of("1.12.2", "1.12.2-14.23.5.2851"),
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
        MinecraftVersionDetails details = assertDoesNotThrow(() -> MinecraftDL.getVersionDetails(version.getUrl()));
        if(!mcClient.isFile()) {
            assertDoesNotThrow(() -> MinecraftDL.downloadVersionDownload(details.getDownloads().getClient(), mcClient));
        }

        File java = new File("download/java-" + mcVersion);
        if (!java.isDirectory()) {
            assertDoesNotThrow(java::mkdirs);
            JavaRuntimeRelease release = assertDoesNotThrow(() -> JavaDL.getJavaRuntimes().get("windows-x64").get(details.getJavaVersion().component).get(0));
            List<JavaFile> files = assertDoesNotThrow(() -> JavaDL.getJavaFiles(release.getManifest().getUrl()));
            assertDoesNotThrow(() -> JavaDL.downloadJavaFiles(files, java));
        }

        File libraries = new File("download/libraries-" + forgeVersion);
        if(libraries.isDirectory()) {
            assertDoesNotThrow(libraries::delete);
        }
        assertDoesNotThrow(libraries::mkdirs);

        ForgeInstaller installer = assertDoesNotThrow(() -> ForgeInstaller.getForVersion(forgeVersion));

        assertDoesNotThrow(() -> installer.createClient(libraries, mcClient, new File(java, "bin/java"), status -> {
            System.out.println("Client: " + status.getCurrentFile());
        }));

        assertTrue(new File(libraries, "net/minecraftforge/forge/" + forgeVersion).isDirectory(), "Client jar does not exist");

        File natives = new File("download/natives-" + forgeVersion);
        if(natives.isDirectory()) {
            assertDoesNotThrow(natives::delete);
        }
        assertDoesNotThrow(natives::mkdirs);
        List<String> libsList = assertDoesNotThrow(() -> installer.downloadLibraries(libraries, new File("forge/download/natives-1.21-51.0.33"), status -> {
            System.out.println("Library: " + status.getCurrentFile());
        }));

        assertTrue(libraries.isDirectory(), "Libraries directory does not exist");
        assertTrue(libraries.listFiles().length > 0, "Libraries directory is empty");
    }
}
