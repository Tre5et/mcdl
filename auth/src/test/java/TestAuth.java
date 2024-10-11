import dev.treset.mcdl.auth.AuthenticationData;
import dev.treset.mcdl.auth.AuthDL;
import dev.treset.mcdl.auth.InteractiveData;
import dev.treset.mcdl.auth.token.DefaultTokenPolicy;
import dev.treset.mcdl.auth.token.FileTokenPolicy;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestAuth {
    @Test
    public void testInteractive() {
        AuthDL.setClientId("389304a5-70a6-4013-907f-98c4eb4b51fb");
        AuthDL.setTokenPolicy(new DefaultTokenPolicy());
        Consumer<InteractiveData> interactiveDataConsumer = deviceCode -> {
            System.out.println(deviceCode.getUrl());
            System.out.println(deviceCode.getUserCode());
        };
        AuthenticationData data = assertDoesNotThrow(() -> AuthDL.runAuthenticationSteps(interactiveDataConsumer, System.out::println));
        assertNotNull(data);
        assertNotNull(data.getMsal());
        assertNotNull(data.getXbl());
        assertNotNull(data.getXsts());
        assertNotNull(data.getMinecraft());
        assertNotNull(data.getProfile());
        assertNotNull(data.toUserData().getUsername());
        assertNotNull(data.toUserData().getUuid());
        assertNotNull(data.toUserData().getAccessToken());
        assertNotNull(data.toUserData().getXuid());
        assertNotNull(data.toUserData().getSkins());
        assertNotNull(data.toUserData().getCapes());
    }

    @Test
    public void testCache() {
        AuthDL.setClientId("389304a5-70a6-4013-907f-98c4eb4b51fb");
        AuthDL.setTokenPolicy(new FileTokenPolicy(new File("token.json"), e -> null));
        Consumer<InteractiveData> interactiveDataConsumer = deviceCode -> {
            System.out.println(deviceCode.getUrl());
            System.out.println(deviceCode.getUserCode());
        };
        AuthenticationData data = assertDoesNotThrow(() -> AuthDL.runAuthenticationSteps(interactiveDataConsumer, System.out::println));
        assertNotNull(data);
        assertNotNull(data.getMsal());
        assertNotNull(data.getXbl());
        assertNotNull(data.getXsts());
        assertNotNull(data.getMinecraft());
        assertNotNull(data.getProfile());
        assertNotNull(data.toUserData().getUsername());
        assertNotNull(data.toUserData().getUuid());
        assertNotNull(data.toUserData().getAccessToken());
        assertNotNull(data.toUserData().getXuid());
        assertNotNull(data.toUserData().getSkins());
        assertNotNull(data.toUserData().getCapes());
    }
}
