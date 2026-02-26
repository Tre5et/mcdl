package dev.treset.mcdl.servermanagement.notification;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.treset.mcdl.servermanagement.data.DataProvider;
import dev.treset.mcdl.servermanagement.data.IdentificationProvider;
import dev.treset.mcdl.servermanagement.exception.RpcCommunicationException;

public interface RpcNotification extends DataProvider, IdentificationProvider<String> {
    String jsonrpc();
    String method();
    JsonArray params();

    @Override
    default JsonElement data() throws RpcCommunicationException {
        return params() == null ? null : (params().isEmpty() ? null : params().get(0));
    }

    @Override
    default String identification() {
        return method();
    }
}

