import dev.treset.mcdl.servermanagement.ManagementHandler;
import dev.treset.mcdl.servermanagement.ServerManagementDL;
import dev.treset.mcdl.servermanagement.request.RpcResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class TestServerManagement {
    @Test
    public void testServerManagement() {
        ManagementHandler handler = ServerManagementDL.createHandler(
                "localhost",
                25569,
                false,
                "5dWObGq3agoBo9JzSXvWOjxKa3J84TqmmIAl2lCc" // Yes, this secret is in the repo, no it won't help you. This is just a test server
        );

        handler.onOpen(s -> System.out.println("Opened"));
        handler.onClose((c,m,s) -> System.out.println("Closed"));
        handler.onError(e -> System.out.println("Error: " + e));
        handler.onWarning((m, e) -> System.out.println("Warning: " + m + ": " + e));

        handler.addNotificationHandler(
                "discman:notification/players/death",
                RpcDeath.class,
                (d) -> System.out.println(d.player.name)
        );

        assertDoesNotThrow(() -> handler.connect());

        RpcResponse res = assertDoesNotThrow(() -> handler.request("discman:server/time"));
        int result = assertDoesNotThrow(res::resultAsInt);
        System.out.println("Time=" + result);

        RpcDeath death = assertDoesNotThrow(() -> handler.awaitNotification("discman:notification/players/death", RpcDeath.class, 20_000));
        System.out.println(death.message.literal);

        assertDoesNotThrow(handler::disconnect);
    }

    private static class RpcDeath {
        public RpcPlayer player;
        public RpcText message;
    }

    private static class RpcPlayer {
        public String name;
        public String id;
    }

    private static class RpcText {
        public String literal;
        public String key;
        public List<RpcText> args;
    }
}