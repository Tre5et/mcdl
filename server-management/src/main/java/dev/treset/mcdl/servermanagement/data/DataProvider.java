package dev.treset.mcdl.servermanagement.data;

import com.google.gson.JsonElement;
import dev.treset.mcdl.servermanagement.exception.RpcCommunicationException;

public interface DataProvider {
    JsonElement data() throws RpcCommunicationException;
}
