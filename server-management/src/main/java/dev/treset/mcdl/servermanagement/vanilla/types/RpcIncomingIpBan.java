package dev.treset.mcdl.servermanagement.vanilla.types;

import java.time.Instant;

public record RpcIncomingIpBan(
        String reason,
        Instant expires,
        String ip,
        String source,
        RpcPlayer player
) {}
