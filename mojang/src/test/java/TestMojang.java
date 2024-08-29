import net.treset.mcdl.mojang.MinecraftProfile;
import net.treset.mcdl.mojang.MinecraftUser;
import net.treset.mcdl.mojang.MojangData;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.concurrent.atomic.AtomicReference;
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
        AtomicReference<MinecraftProfile> profile = new AtomicReference<>();
        assertDoesNotThrow(() -> profile.set(MojangData.getMinecraftProfile(uuid)));
        assertEquals(profile.get().getName(), name);
    }

    @ParameterizedTest
    @MethodSource("params")
    public void testUser(String name, String uuid) {
        AtomicReference<MinecraftUser> user = new AtomicReference<>();
        assertDoesNotThrow(() -> user.set(MojangData.getMinecraftUser(name)));
        assertEquals(user.get().getId(), uuid);
    }
}
