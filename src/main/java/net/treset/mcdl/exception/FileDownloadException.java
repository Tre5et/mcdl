package net.treset.mcdl.exception;

import java.io.IOException;

public class FileDownloadException extends IOException {
    public FileDownloadException(String message) {
        super(message);
    }
    public FileDownloadException(String message, Exception parent) {
        super(message, parent);
    }
}
