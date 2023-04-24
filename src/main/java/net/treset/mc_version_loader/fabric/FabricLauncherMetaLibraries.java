package net.treset.mc_version_loader.fabric;

import java.util.List;

public class FabricLauncherMetaLibraries {
    private List<FabricLibrary> client;
    private List<FabricLibrary> common;
    private List<FabricLibrary> server;

    public FabricLauncherMetaLibraries(List<FabricLibrary> client, List<FabricLibrary> common, List<FabricLibrary> server) {
        this.client = client;
        this.common = common;
        this.server = server;
    }

    public List<FabricLibrary> getClient() {
        return client;
    }

    public void setClient(List<FabricLibrary> client) {
        this.client = client;
    }

    public List<FabricLibrary> getCommon() {
        return common;
    }

    public void setCommon(List<FabricLibrary> common) {
        this.common = common;
    }

    public List<FabricLibrary> getServer() {
        return server;
    }

    public void setServer(List<FabricLibrary> server) {
        this.server = server;
    }
}
