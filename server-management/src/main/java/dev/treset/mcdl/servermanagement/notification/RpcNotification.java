package dev.treset.mcdl.servermanagement.notification;

import java.util.List;

public interface RpcNotification {
    String jsonrpc();
    String method();
    List<Object> params();
}

