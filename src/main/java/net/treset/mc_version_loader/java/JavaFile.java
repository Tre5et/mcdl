package net.treset.mc_version_loader.java;

import java.util.Objects;

public class JavaFile {
    private String name;
    private boolean executable;
    private String type;
    private JavaDownload lzma;
    private JavaDownload raw;

    public JavaFile(String name, boolean executable, String type, JavaDownload lzma, JavaDownload raw) {
        this.name = name;
        this.executable = executable;
        this.type = type;
        this.lzma = lzma;
        this.raw = raw;
    }

    public boolean isFile() {
        return Objects.equals(getType(), "file");
    }

    public boolean isDir() {
        return Objects.equals(getType(), "directory");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isExecutable() {
        return executable;
    }

    public void setExecutable(boolean executable) {
        this.executable = executable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JavaDownload getLzma() {
        return lzma;
    }

    public void setLzma(JavaDownload lzma) {
        this.lzma = lzma;
    }

    public JavaDownload getRaw() {
        return raw;
    }

    public void setRaw(JavaDownload raw) {
        this.raw = raw;
    }
}
