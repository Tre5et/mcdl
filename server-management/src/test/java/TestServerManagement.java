import dev.treset.mcdl.servermanagement.ManagementHandler;
import dev.treset.mcdl.servermanagement.ServerManagementDL;
import dev.treset.mcdl.servermanagement.vanilla.RpcMethods;
import dev.treset.mcdl.servermanagement.vanilla.RpcNotifications;
import dev.treset.mcdl.servermanagement.vanilla.types.RpcDifficulty;
import dev.treset.mcdl.servermanagement.vanilla.types.RpcGameMode;
import dev.treset.mcdl.servermanagement.vanilla.types.RpcPlayer;
import dev.treset.mcdl.servermanagement.vanilla.types.RpcUserBan;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        handler.onError(Throwable::printStackTrace);
        handler.onWarning((m, e) -> System.out.println("Warning: " + m + ": " + e));

        handler.addNotificationMethod(RpcNotifications.Allowlist.added(p -> System.out.println(p.name() + " added")));

        assertDoesNotThrow(() -> handler.connect());

        List<RpcPlayer> result = assertDoesNotThrow(() -> RpcMethods.Allowlist.ADD.sendBlocking(List.of(new RpcPlayer("TreSet", UUID.fromString("956cadbb-0ea4-4985-8a60-12ca3a9ad1bb"))), handler));
        System.out.println("Allowlist = " + result);

        RpcMethods.Bans.REMOVE.send(
                List.of(new RpcPlayer("TreSet", UUID.fromString("956cadbb-0ea4-4985-8a60-12ca3a9ad1bb"))),
                b -> System.out.println("bans = " + b),
                e -> System.out.println("error: " + e),
                handler
        );
        RpcUserBan ban = assertDoesNotThrow(
                () -> handler.awaitNotification(RpcNotifications.Bans.added(),
                        () -> RpcMethods.Bans.ADD.send(List.of(
                                new RpcUserBan("New Reason", Instant.now().plusMillis(100_000_000), "my source", new RpcPlayer("TreSet", UUID.fromString("956cadbb-0ea4-4985-8a60-12ca3a9ad1bb")))
                        ), r -> {}, e -> {}, handler)
                )
        );
        System.out.println("New ban = " + ban);

        for(RpcGameMode mode : RpcGameMode.values()) {
            RpcGameMode newMode = assertDoesNotThrow(() -> RpcMethods.ServerSettings.GAME_MODE_SET.sendBlocking(mode, handler));
            System.out.println("new mode = " + newMode);
            assertEquals(newMode, mode);
        }

        for(RpcDifficulty difficulty : RpcDifficulty.values()) {
            RpcDifficulty newDifficulty = assertDoesNotThrow(() -> RpcMethods.ServerSettings.DIFFICULTY_SET.sendBlocking(difficulty, handler));
            System.out.println("new difficulty = " + difficulty);
            assertEquals(newDifficulty, difficulty);
        }

        assertDoesNotThrow(handler::disconnect);
    }
}