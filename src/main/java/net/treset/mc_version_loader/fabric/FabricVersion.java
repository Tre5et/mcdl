package net.treset.mc_version_loader.fabric;

public class FabricVersion {
    private String minecraftVersion;
    private String loaderVersion;

    public FabricVersion(String minecraftVersion, String loaderVersion) {
        this.minecraftVersion = minecraftVersion;
        this.loaderVersion = loaderVersion;
    }

    public String getMinecraftVersion() {
        return minecraftVersion;
    }

    public void setMinecraftVersion(String minecraftVersion) {
        this.minecraftVersion = minecraftVersion;
    }

    public String getLoaderVersion() {
        return loaderVersion;
    }

    public void setLoaderVersion(String loaderVersion) {
        this.loaderVersion = loaderVersion;
    }
}
