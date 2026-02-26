package dev.treset.mcdl.servermanagement.vanilla.types;

import java.util.UUID;

public record RpcPlayer(
        String name,
        UUID id
) {}
