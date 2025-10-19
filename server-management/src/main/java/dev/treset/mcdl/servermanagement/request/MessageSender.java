package dev.treset.mcdl.servermanagement.request;

import java.io.IOException;

public interface MessageSender {
    void send(String message) throws IOException;
}
