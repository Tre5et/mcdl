import net.treset.mcdl.assets.MinecraftAssets;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestAssets {
    @ParameterizedTest
    @ValueSource(strings = {"1.16", "pre-1.6"})
    public void testVirtualAssets(String version) {
        assertDoesNotThrow(() -> MinecraftAssets.createVirtualAssets(version, new File("assets")));
        assertTrue(new File("assets/virtual/"+version).isDirectory(), "Virtual directory does not exist");
        assertTrue(new File("assets/virtual/"+version+"/pack.mcmeta").isFile(), "McMeta file does not exist");
    }
}
