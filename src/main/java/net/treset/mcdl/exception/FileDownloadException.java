package net.treset.mcdl.exception;

public class FileDownloadException extends Exception {
    public FileDownloadException(String message) {
        super(message);
    }
    public FileDownloadException(String message, Exception parent) {
        super(message, parent);
    }
}
