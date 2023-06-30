package net.treset.mc_version_loader.launcher;
public class LauncherModDownload {
    private String provider;
    private String id;

    public LauncherModDownload(String provider, String id) {
        this.provider = provider;
        this.id = id;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
