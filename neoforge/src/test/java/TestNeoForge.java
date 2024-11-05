import dev.treset.mcdl.neoforge.NeoForgeDL;
import dev.treset.mcdl.neoforge.NeoForgeInstallerExecutor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestNeoForge {

    public static Stream<Arguments> testNeoForge() {
        return Stream.of(
                Arguments.of("1.21.3"),
                Arguments.of("1.20.5")
        );
    }

    @ParameterizedTest
    @MethodSource("testNeoForge")
    public void testNeoForge(String mcVersion) {
        List<String> neoForgeVersions = assertDoesNotThrow(() -> NeoForgeDL.getNeoForgeVersionsList(mcVersion));

        String neoForgeVersion = neoForgeVersions.get(0);

        File libraries = new File("download/libraries-neoforge-" + neoForgeVersion);
        if(libraries.isDirectory()) {
            assertDoesNotThrow(libraries::delete);
        }
        assertDoesNotThrow(libraries::mkdirs);

        NeoForgeInstallerExecutor installer = new NeoForgeInstallerExecutor(neoForgeVersion);
        assertDoesNotThrow(() -> installer.install(
                libraries,
                new File(System.getenv("JAVA_HOME"), "bin/java.exe"),
                status -> {
                    System.out.println("Status: " + status.getCurrentFile());
                }
        ));

        assertTrue(new File(libraries, "net/neoforged/neoforge/" + neoForgeVersion).isDirectory(), "Client jar does not exist");

        assertTrue(libraries.isDirectory(), "Libraries directory does not exist");
        assertTrue(libraries.listFiles().length > 1, "Libraries directory is empty");
    }
}
