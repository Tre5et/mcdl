package dev.treset.mcdl.servermanagement.incoming;

import dev.treset.mcdl.servermanagement.exception.RpcCommunicationException;

public interface RpcErrorFunction<T,R> {
    R apply(T data) throws RpcCommunicationException;
}
