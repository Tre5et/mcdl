package net.treset.mc_version_loader.fabric;

public class FabricLauncherMeta {
    private FabricLauncherMetaLibraries libraries;
    private int version;

    public FabricLauncherMeta(FabricLauncherMetaLibraries libraries, int version) {
        this.libraries = libraries;
        this.version = version;
    }

    public FabricLauncherMetaLibraries getLibraries() {
        return libraries;
    }

    public void setLibraries(FabricLauncherMetaLibraries libraries) {
        this.libraries = libraries;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
