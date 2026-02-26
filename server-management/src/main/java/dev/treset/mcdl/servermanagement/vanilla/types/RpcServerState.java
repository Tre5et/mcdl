package dev.treset.mcdl.servermanagement.vanilla.types;

import java.util.List;

public record RpcServerState(
        List<RpcPlayer> players,
        Boolean started,
        RpcVersion version
) { }
