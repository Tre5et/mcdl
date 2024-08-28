import net.treset.mc_version_loader.auth.AuthenticationData;
import net.treset.mc_version_loader.auth.AuthenticationException;
import net.treset.mc_version_loader.auth.MinecraftAuth;
import net.treset.mc_version_loader.auth.token.DefaultTokenPolicy;
import net.treset.mc_version_loader.auth.token.FileTokenPolicy;

import java.io.File;

public class Authentication {
    public static void main(String[] args) {
        testInteractive();
        testCache();
    }

    public static void testInteractive() {
        MinecraftAuth.setClientId("389304a5-70a6-4013-907f-98c4eb4b51fb");
        MinecraftAuth.setTokenPolicy(new DefaultTokenPolicy());
        try {
            AuthenticationData data = MinecraftAuth.authenticate();
        } catch (AuthenticationException e) {
            System.err.println("Authentication:testInteractive FAILED: ");
            e.printStackTrace();
        }
        System.out.println("Authentication:testInteractive PASSED");
    }

    public static void testCache() {
        MinecraftAuth.setClientId("389304a5-70a6-4013-907f-98c4eb4b51fb");
        MinecraftAuth.setTokenPolicy(new FileTokenPolicy(new File("token.json"), e -> null));
        try {
            AuthenticationData data = MinecraftAuth.authenticate();
        } catch (AuthenticationException e) {
            System.err.println("Authentication:testCache FAILED: ");
            e.printStackTrace();
        }
        System.out.println("Authentication:testCache PASSED");
    }
}
