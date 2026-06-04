package dev.treset.mcdl.servermanagement.incoming;

import dev.treset.mcdl.servermanagement.exception.RpcCommunicationException;

public interface RpcErrorSupplier<T> {
    T get() throws RpcCommunicationException;
}
