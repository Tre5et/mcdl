import dev.treset.mcdl.fabric.FabricDL;
import dev.treset.mcdl.fabric.FabricVersion;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestFabric {
    public static Stream<Arguments> testFabric() {
        return Stream.of(
                Arguments.of("1.21", "0.16.3"),
                Arguments.of("1.16.5", "0.14.21"),
                Arguments.of("1.14", "0.10.7")
        );
    }

    @ParameterizedTest
    @MethodSource("testFabric")
    public void testFabric(String mcVersion, String fabricVersion) {
        List<FabricVersion> versions = assertDoesNotThrow(() -> FabricDL.getFabricVersions(mcVersion));
        FabricVersion version = assertDoesNotThrow(() -> versions.stream().filter(v -> v.getLoader().getVersion().equals(fabricVersion)).findFirst().get());

        File client = new File("download/client-" + mcVersion + "-" + fabricVersion + ".jar");
        if(client.isFile()) {
            assertDoesNotThrow(client::delete);
        }
        assertDoesNotThrow(() -> FabricDL.downloadFabricClient(version.getLoader(), client));
        assertTrue(client.isFile(), "Client jar does not exist");

        File libraries = new File("download/libraries-" + mcVersion + "-" + fabricVersion);
        if(libraries.isDirectory()) {
            assertDoesNotThrow(libraries::delete);
        }
        assertDoesNotThrow(libraries::mkdirs);
        assertDoesNotThrow(() -> version.getLauncherMeta().downloadClientLibraries(libraries, status -> {}));
        assertDoesNotThrow(() -> version.getLauncherMeta().downloadCommonLibraries(libraries, status -> {}));
        assertTrue(libraries.isDirectory(), "Libraries directory does not exist");
        assertTrue(libraries.listFiles().length > 0, "Libraries directory is empty");
    }
}
