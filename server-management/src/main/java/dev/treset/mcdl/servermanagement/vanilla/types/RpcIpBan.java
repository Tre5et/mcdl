package dev.treset.mcdl.servermanagement.vanilla.types;

import java.time.Instant;

public record RpcIpBan(
        String reason,
        Instant expires,
        String ip,
        String source
) {}
