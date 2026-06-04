package dev.treset.mcdl.servermanagement.incoming;

import dev.treset.mcdl.servermanagement.exception.RpcCommunicationException;

public interface RpcErrorRunnable {
    void run() throws RpcCommunicationException;
}
