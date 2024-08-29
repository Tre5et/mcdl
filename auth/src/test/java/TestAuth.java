import net.treset.mcdl.auth.AuthenticationData;
import net.treset.mcdl.auth.MinecraftAuth;
import net.treset.mcdl.auth.token.DefaultTokenPolicy;
import net.treset.mcdl.auth.token.FileTokenPolicy;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestAuth {
    @Test
    @Disabled
    public void testInteractive() {
        MinecraftAuth.setClientId("389304a5-70a6-4013-907f-98c4eb4b51fb");
        MinecraftAuth.setTokenPolicy(new DefaultTokenPolicy());
        AuthenticationData data = assertDoesNotThrow(MinecraftAuth::authenticate);
        assertNotNull(data);
        assertNotNull(data.getMsal());
        assertNotNull(data.getXbl());
        assertNotNull(data.getXsts());
        assertNotNull(data.getMinecraft());
        assertNotNull(data.getUser());
    }

    @Test
    @Disabled
    public void testCache() {
        MinecraftAuth.setClientId("389304a5-70a6-4013-907f-98c4eb4b51fb");
        MinecraftAuth.setTokenPolicy(new FileTokenPolicy(new File("token.json"), e -> null));
        AuthenticationData data = assertDoesNotThrow(MinecraftAuth::authenticate);
        assertNotNull(data);
        assertNotNull(data.getMsal());
        assertNotNull(data.getXbl());
        assertNotNull(data.getXsts());
        assertNotNull(data.getMinecraft());
        assertNotNull(data.getUser());
    }
}
