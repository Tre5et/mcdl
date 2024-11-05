import dev.treset.mcdl.minecraft.MinecraftDL;
import dev.treset.mcdl.minecraft.MinecraftVersion;
import dev.treset.mcdl.minecraft.MinecraftProfile;
import dev.treset.mcdl.util.FileUtil;
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
    public void testVersion(String id) {
        if(new File("download/client-" + id + ".jar").isFile()) {
            assertDoesNotThrow(() -> FileUtil.delete(new File("download/client-" + id + ".jar")));
        }
        if(new File("download/libraries-" + id).isDirectory()) {
            assertDoesNotThrow(() -> FileUtil.delete(new File("download/libraries-" + id)));
        }
        if(new File("download/natives-" + id).isDirectory()) {
            assertDoesNotThrow(() -> FileUtil.delete(new File("download/natives-" + id)));
        }

        MinecraftVersion version = assertDoesNotThrow(() -> MinecraftDL.getVersion(id));
        MinecraftProfile details = assertDoesNotThrow(() -> MinecraftDL.getVersionDetails(version.getUrl()));
        assertDoesNotThrow(() -> MinecraftDL.downloadVersionDownload(details.getDownloads().getClient(), new File("download/client-" + id + ".jar")));
        assertTrue(new File("download/client-" + id + ".jar").isFile(), "Client jar does not exist");
        assertDoesNotThrow(() -> Files.createDirectories(new File("download/libraries-" + id).toPath()));
        List<String> libs = assertDoesNotThrow(() -> MinecraftDL.downloadVersionLibraries(details.getLibraries(), new File("download/libraries-" + id), List.of(), new File("download/natives-" + id), (v) -> {}));
        assertTrue(new File("download/libraries-" + id).isDirectory(), "Libraries directory does not exist");
        assertTrue(Objects.requireNonNull(new File("download/libraries-" + id).listFiles()).length > 0, "Libraries directory is empty");
    }
}
