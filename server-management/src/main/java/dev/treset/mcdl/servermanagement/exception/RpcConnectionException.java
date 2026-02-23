package dev.treset.mcdl.servermanagement.exception;

import java.io.IOException;

public class RpcConnectionException extends IOException {
    public RpcConnectionException(String message) {
        super(message);
    }

    public RpcConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
