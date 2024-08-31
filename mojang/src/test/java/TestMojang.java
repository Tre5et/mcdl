import net.treset.mcdl.mojang.MinecraftProfile;
import net.treset.mcdl.mojang.MinecraftUser;
import net.treset.mcdl.mojang.MojangDL;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMojang {

    public static Stream<Arguments> params() {
        return Stream.of(
                Arguments.of("TreSet", "956cadbb0ea449858a6012ca3a9ad1bb"),
                Arguments.of("Mumbo", "ac224782efff4296b08cdbde8e47abdb"),
                Arguments.of("Skizzleman", "64de994da7074d8c8a483ffb786c9b99")
        );
    }

    @ParameterizedTest
    @MethodSource("params")
    public void testProfile(String name, String uuid) {
        MinecraftProfile profile = assertDoesNotThrow(() -> MojangDL.getMinecraftProfile(uuid));
        assertEquals(profile.getName(), name);
    }

    @ParameterizedTest
    @MethodSource("params")
    public void testUser(String name, String uuid) {
        MinecraftUser user = assertDoesNotThrow(() -> MojangDL.getMinecraftUser(name));
        assertEquals(user.getId(), uuid);
    }
}
