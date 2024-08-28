package net.treset.mcdl.quiltmc;

import java.util.List;

public class QuiltLibraries {
    private List<QuiltLibrary> client;
    private List<QuiltLibrary> common;
    private List<QuiltLibrary> server;
    private List<QuiltLibrary> development;

    public QuiltLibraries(List<QuiltLibrary> client, List<QuiltLibrary> common, List<QuiltLibrary> server, List<QuiltLibrary> development) {
        this.client = client;
        this.common = common;
        this.server = server;
        this.development = development;
    }

    public List<QuiltLibrary> getClient() {
        return client;
    }

    public void setClient(List<QuiltLibrary> client) {
        this.client = client;
    }

    public List<QuiltLibrary> getCommon() {
        return common;
    }

    public void setCommon(List<QuiltLibrary> common) {
        this.common = common;
    }

    public List<QuiltLibrary> getServer() {
        return server;
    }

    public void setServer(List<QuiltLibrary> server) {
        this.server = server;
    }

    public List<QuiltLibrary> getDevelopment() {
        return development;
    }

    public void setDevelopment(List<QuiltLibrary> development) {
        this.development = development;
    }
}
