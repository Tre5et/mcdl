package dev.treset.mcdl.servermanagement;

import dev.treset.mcdl.servermanagement.data.TriConsumer;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.function.Consumer;

public class ManagementClient extends WebSocketClient {
    private Consumer<String> messageHandler;
    private Consumer<ServerHandshake> openHandler;
    private TriConsumer<Integer,String,Boolean> closeHandler;
    private Consumer<Exception> errorHandler;

    private boolean expectFailureOnStart = true;

    public ManagementClient(
            URI uri,
            String secret,
            Consumer<String> messageHandler,
            Consumer<ServerHandshake> openHandler,
            TriConsumer<Integer,String,Boolean> closeHandler,
            Consumer<Exception> errorHandler
    ) throws IOException {
        super(
                uri,
                Map.of("Authorization", "Bearer " + secret)
        );
        this.messageHandler = messageHandler;
        this.openHandler = openHandler;
        this.closeHandler = closeHandler;
        this.errorHandler = errorHandler;
        try {
            boolean connected = this.connectBlocking();
            if(!connected) {
                throw new IOException("Could not connect to server.");
            }
        } catch (InterruptedException e) {
            throw new IOException("Failed to connect to Server", e);
        }
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        expectFailureOnStart = false;
        openHandler.accept(serverHandshake);
    }

    @Override
    public void onMessage(String s) {
        messageHandler.accept(s);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        if(!expectFailureOnStart) {
            closeHandler.accept(code, reason, remote);
        }
    }

    @Override
    public void onError(Exception e) {
        if(!expectFailureOnStart) {
            errorHandler.accept(e);
        }
    }

    public void setMessageHandler(Consumer<String> messageHandler) {
        this.messageHandler = messageHandler;
    }

    public void setOpenHandler(Consumer<ServerHandshake> openHandler) {
        this.openHandler = openHandler;
    }

    public void setCloseHandler(TriConsumer<Integer, String, Boolean> closeHandler) {
        this.closeHandler = closeHandler;
    }

    public void setErrorHandler(Consumer<Exception> errorHandler) {
        this.errorHandler = errorHandler;
    }
}
