package net.treset.mcdl.fabric;

public class FabricLauncherMetaMainClass {
    private String client;
    private String server;

    public FabricLauncherMetaMainClass(String client, String server) {
        this.client = client;
        this.server = server;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
}
