package dev.treset.mcdl.servermanagement.incoming;

import dev.treset.mcdl.servermanagement.exception.RpcCommunicationException;

public interface RpcErrorConsumer<T> {
    void accept(T data) throws RpcCommunicationException;
}
