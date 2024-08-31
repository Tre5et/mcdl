import net.treset.mcdl.resourcepacks.Resourcepack;
import net.treset.mcdl.resourcepacks.Texturepack;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class TestResourcepacks {
    @ParameterizedTest
    @ValueSource(strings = {"testfiles/CCD_RP.zip", "testfiles/CCD_RP", "testfiles/VT_RP.zip", "testfiles/VT_RP"})
    public void testResourcePack(String path) {
        File file = new File(path);
        assertDoesNotThrow(() -> Resourcepack.get(file), "can't parse ressourcepack: " + file.getName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"testfiles/Sphax_TP.zip", "testfiles/Sphax_TP", "testfiles/SC_TP.zip", "testfiles/SC_TP"})
    public void testTexturePack(String path) {
        File file = new File(path);
        assertDoesNotThrow(() -> Texturepack.get(file), "can't parse texturepack: " + file.getName());
    }
}
