package net.treset.mc_version_loader.quiltmc;

import com.google.gson.annotations.SerializedName;

public class QuiltMainClass {
    private String server;
    private String client;
    @SerializedName("serverLauncher")
    private String serverLauncher;

    public QuiltMainClass(String server, String client, String serverLauncher) {
        this.server = server;
        this.client = client;
        this.serverLauncher = serverLauncher;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getServerLauncher() {
        return serverLauncher;
    }

    public void setServerLauncher(String serverLauncher) {
        this.serverLauncher = serverLauncher;
    }
}
