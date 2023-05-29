package net.treset.mc_version_loader.exception;

import java.util.List;

public class FileDownloadException extends Exception {
    private final List<Exception> parents;
    public FileDownloadException(String message) {
        super(message);
        this.parents = List.of();
    }
    public FileDownloadException(String message, Exception parent) {
        super(message);
        this.parents = List.of(parent);
    }
    public FileDownloadException(String message, List<Exception> parents) {
        super(message);
        this.parents = parents;
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
        parents.forEach(Exception::printStackTrace);
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder(super.toString());
        out.append("\ncause(s): ").append(parents.get(0).toString());
        parents.forEach(e -> out.append(e.toString()));
        return out.toString();
    }
}
