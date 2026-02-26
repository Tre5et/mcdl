package dev.treset.mcdl.servermanagement.vanilla.types;

import java.util.List;

public record RpcSystemMessage(
        List<RpcPlayer> receivingPlayers,
        Boolean overlay,
        RpcMessage message
) {}
