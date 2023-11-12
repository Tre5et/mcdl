package net.treset.mc_version_loader.java;

import java.util.List;

public class JavaRuntimeOs {
    String id;
    List<JavaRuntimeRelease> releases;

    public JavaRuntimeOs(String id, List<JavaRuntimeRelease> releases) {
        this.id = id;
        this.releases = releases;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<JavaRuntimeRelease> getReleases() {
        return releases;
    }

    public void setReleases(List<JavaRuntimeRelease> releases) {
        this.releases = releases;
    }
}
