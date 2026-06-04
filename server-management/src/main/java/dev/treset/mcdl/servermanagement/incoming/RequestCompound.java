package dev.treset.mcdl.servermanagement.incoming;

public record RequestCompound<T> (
    Integer id,
    T data
) {}
