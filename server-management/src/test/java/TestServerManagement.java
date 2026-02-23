import dev.treset.mcdl.servermanagement.ManagementHandler;
import dev.treset.mcdl.servermanagement.ServerManagementDL;
import dev.treset.mcdl.servermanagement.data.RpcResponse;
import dev.treset.mcdl.servermanagement.outgoing.OutgoingMethod;
import dev.treset.mcdl.servermanagement.serialization.DataSerializer;
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

        handler.addNotificationReceiver(
                "discman:notification/players/death",
                DataSerializer.forType(RpcDeath.class),
                (d) -> System.out.println(d.player.name)
        );

        assertDoesNotThrow(() -> handler.connect());

        OutgoingMethod.Parameterless<Integer> method = OutgoingMethod.of("discman:server/time")
                .withResponse(DataSerializer.INTEGER);

        int result = assertDoesNotThrow(() -> method.sendBlocking(handler));
        System.out.println("Time=" + result);

        RpcDeath death = assertDoesNotThrow(() -> handler.awaitNotification("discman:notification/players/death", DataSerializer.forType(RpcDeath.class), () -> {}));
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