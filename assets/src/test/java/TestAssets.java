import net.treset.mcdl.assets.AssetsDL;
import net.treset.mcdl.assets.AssetIndex;
import net.treset.mcdl.minecraft.MinecraftGame;
import net.treset.mcdl.minecraft.MinecraftVersion;
import net.treset.mcdl.minecraft.MinecraftVersionDetails;
import net.treset.mcdl.util.FileUtil;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestAssets {
    @ParameterizedTest
    @ValueSource(strings = {"1.21", "1.16.5", "1.0"})
    public void testDownload(String version) {
        List<MinecraftVersion> versions = assertDoesNotThrow(MinecraftGame::getVersions);
        MinecraftVersion mcVersion = assertDoesNotThrow(() -> versions.stream().filter(v -> v.getId().equals(version)).findFirst().get());
        MinecraftVersionDetails details = assertDoesNotThrow(() -> MinecraftGame.getVersionDetails(mcVersion.getUrl()));
        File dir = new File("assets");

        AssetIndex index = assertDoesNotThrow(() -> AssetsDL.getAssetIndex(details.getAssetIndex().getUrl()));
        assertDoesNotThrow(() -> AssetsDL.downloadAssets(index, dir, true));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1.16", "pre-1.6"})
    public void testResolve(String version) {
        File dir = new File("assets/virtual/"+version);
        if(dir.isDirectory()) {
            assertDoesNotThrow(() -> FileUtil.delete(dir));
        }

        assertDoesNotThrow(() -> AssetsDL.resolveAssets(version, new File("assets")));
        assertTrue(dir.isDirectory(), "Virtual directory does not exist");
        assertTrue(new File(dir, "pack.mcmeta").isFile(), "McMeta file does not exist");
    }
}
