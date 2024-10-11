package dev.treset.mcdl.forge.pre1_13;

import java.io.IOException;

public class MirrorData {
    private String name;
    private String homepage;
    private String maven;

    public MirrorData(String name, String homepage, String maven) {
        this.name = name;
        this.homepage = homepage;
        this.maven = maven;
    }

    public static MirrorData from(String line) throws IOException {
        String[] parts = line.split("!");
        if(parts.length != 4) {
            throw new IOException("Invalid mirror data line: " + line);
        }
        return new MirrorData(parts[0], parts[2], parts[3]);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getMaven() {
        return maven;
    }

    public void setMaven(String maven) {
        this.maven = maven;
    }
}
