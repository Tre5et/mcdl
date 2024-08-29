import net.treset.mcdl.quiltmc.QuiltMC;
import net.treset.mcdl.quiltmc.QuiltProfile;
import net.treset.mcdl.quiltmc.QuiltVersion;
import net.treset.mcdl.util.FileUtil;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class TestQuilt {
    public static Stream<Arguments> testQuilt() {
         return Stream.of(
                 Arguments.of("1.21", "0.26.3"),
                 Arguments.of("1.16.5", "0.19.5"),
                 Arguments.of("1.14.4", "0.16.0")
         );
    }

    @ParameterizedTest
    @MethodSource("testQuilt")
    public void testQuilt(String mcVersion, String quiltVersion) {
        File client = new File("download/client-" + mcVersion + "-" + quiltVersion + ".jar");
        if(client.isFile()) {
            assertDoesNotThrow(() -> FileUtil.delete(client));
        }

        File libraries = new File("download/libraries-" + mcVersion + "-" + quiltVersion);
        if(libraries.isDirectory()) {
            assertDoesNotThrow(() -> FileUtil.delete(libraries));
        }
        assertDoesNotThrow(libraries::mkdirs);

        List<QuiltVersion> versions = assertDoesNotThrow(() -> QuiltMC.getQuiltVersions(mcVersion));
        QuiltVersion version = assertDoesNotThrow(() -> versions.stream().filter(v -> v.getLoader().getVersion().equals(quiltVersion)).findFirst().get());
        QuiltProfile profile = assertDoesNotThrow(() -> QuiltMC.getQuiltProfile(mcVersion, quiltVersion));
        assertDoesNotThrow(() -> QuiltMC.downloadQuiltLibraries(libraries, profile.getLibraries()));
    }
}
