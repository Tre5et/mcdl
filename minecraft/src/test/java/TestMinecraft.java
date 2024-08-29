import net.treset.mcdl.minecraft.MinecraftGame;
import net.treset.mcdl.minecraft.MinecraftVersion;
import net.treset.mcdl.minecraft.MinecraftVersionDetails;
import net.treset.mcdl.util.FileUtil;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestMinecraft {
    @ParameterizedTest
    @ValueSource(strings = {"1.0", "1.2.1", "1.5.1", "1.7.10", "1.12.2", "1.16.5", "1.21"})
    public void testVersion(String name) {
        if(new File("download/client-" + name + ".jar").isFile()) {
            assertDoesNotThrow(() -> FileUtil.delete(new File("download/client-" + name + ".jar")));
        }
        if(new File("download/libraries-" + name).isDirectory()) {
            assertDoesNotThrow(() -> FileUtil.delete(new File("download/libraries-" + name)));
        }
        if(new File("download/natives-" + name).isDirectory()) {
            assertDoesNotThrow(() -> FileUtil.delete(new File("download/natives-" + name)));
        }

        List<MinecraftVersion> versions = assertDoesNotThrow(MinecraftGame::getVersions);
        MinecraftVersion version = assertDoesNotThrow(() -> versions.stream().filter(v -> v.getId().equals(name)).findFirst().get());
        MinecraftVersionDetails details = assertDoesNotThrow(() -> MinecraftGame.getVersionDetails(version.getUrl()));
        assertDoesNotThrow(() -> MinecraftGame.downloadVersionDownload(details.getDownloads().getClient(), new File("download/client-" + name + ".jar")));
        assertTrue(new File("download/client-" + name + ".jar").isFile(), "Client jar does not exist");
        assertDoesNotThrow(() -> Files.createDirectories(new File("download/libraries-" + name).toPath()));
        assertDoesNotThrow(() -> MinecraftGame.downloadVersionLibraries(details.getLibraries(), new File("download/libraries-" + name), List.of(), new File("download/natives-" + name), (v) -> {}));
        assertTrue(new File("download/libraries-" + name).isDirectory(), "Libraries directory does not exist");
        assertTrue(Objects.requireNonNull(new File("download/libraries-" + name).listFiles()).length > 0, "Libraries directory is empty");
    }
}
