package dev.treset.mcdl.servermanagement.vanilla.types;

import java.time.Instant;

public record RpcUserBan(
        String reason,
        Instant expires,
        String source,
        RpcPlayer player
) {}
