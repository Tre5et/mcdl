package dev.treset.mcdl.servermanagement.vanilla.types;

public record RpcOperator(
    Integer permissionLevel,
    Boolean bypassesPlayerLimit,
    RpcPlayer player
) {}
