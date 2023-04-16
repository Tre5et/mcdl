package net.treset.mc_version_loader.java;

import java.util.List;

public class JavaManifest {
    private List<JavaFile> files;

    public JavaManifest(List<JavaFile> files) {
        this.files = files;
    }

    public List<JavaFile> getFiles() {
        return files;
    }

    public void setFiles(List<JavaFile> files) {
        this.files = files;
    }
}
