import dev.treset.mcdl.saves.Save;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class TestSaves {
    @ParameterizedTest
    @ValueSource(strings = {"testfiles/parkour-spiral", "testfiles/tschui-iwa"})
    public void testSave(String path) {
        File file = new File(path);
        assertDoesNotThrow(() -> Save.get(file), "cant parse save: " + file.getName());
    }
}
