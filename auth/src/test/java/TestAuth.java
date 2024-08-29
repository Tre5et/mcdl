import net.treset.mcdl.auth.AuthenticationData;
import net.treset.mcdl.auth.MinecraftAuth;
import net.treset.mcdl.auth.token.DefaultTokenPolicy;
import net.treset.mcdl.auth.token.FileTokenPolicy;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestAuth {
    @Test
    @Disabled
    public void testInteractive() {
        MinecraftAuth.setClientId("389304a5-70a6-4013-907f-98c4eb4b51fb");
        MinecraftAuth.setTokenPolicy(new DefaultTokenPolicy());
        AtomicReference<AuthenticationData> data = new AtomicReference<>();
        assertDoesNotThrow(() -> data.set(MinecraftAuth.authenticate()));
        assertNotNull(data.get());
        assertNotNull(data.get().getMsal());
        assertNotNull(data.get().getXbl());
        assertNotNull(data.get().getXsts());
        assertNotNull(data.get().getMinecraft());
        assertNotNull(data.get().getUser());
    }

    @Test
    @Disabled
    public void testCache() {
        MinecraftAuth.setClientId("389304a5-70a6-4013-907f-98c4eb4b51fb");
        MinecraftAuth.setTokenPolicy(new FileTokenPolicy(new File("token.json"), e -> null));
        AtomicReference<AuthenticationData> data = new AtomicReference<>();
        assertDoesNotThrow(() -> data.set(MinecraftAuth.authenticate()));
        assertNotNull(data.get());
        assertNotNull(data.get().getMsal());
        assertNotNull(data.get().getXbl());
        assertNotNull(data.get().getXsts());
        assertNotNull(data.get().getMinecraft());
        assertNotNull(data.get().getUser());
    }
}
