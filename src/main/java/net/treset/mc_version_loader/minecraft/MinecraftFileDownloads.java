package net.treset.mc_version_loader.minecraft;

import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;

public class MinecraftFileDownloads {
    private Downloads client;
    private Downloads clientMappings;
    private Downloads server;
    private Downloads serverMappings;

    public MinecraftFileDownloads(Downloads client, Downloads clientMappings, Downloads server, Downloads serverMappings) {
        this.client = client;
        this.clientMappings = clientMappings;
        this.server = server;
        this.serverMappings = serverMappings;
    }

    public static MinecraftFileDownloads fromJson(JsonObject downloadsObj) {
        JsonObject clientObj = JsonUtils.getAsJsonObject(downloadsObj, "client");
        JsonObject clientMappingsObj = JsonUtils.getAsJsonObject(downloadsObj, "client_mappings");
        JsonObject serverObj = JsonUtils.getAsJsonObject(downloadsObj, "server");
        JsonObject serverMappingsObj = JsonUtils.getAsJsonObject(downloadsObj, "server_mappings");



        return new MinecraftFileDownloads(
                Downloads.parseVersionDownload(clientObj),
                Downloads.parseVersionDownload(clientMappingsObj),
                Downloads.parseVersionDownload(serverObj),
                Downloads.parseVersionDownload(serverMappingsObj)
        );
    }

    public Downloads getClient() {
        return client;
    }

    public void setClient(Downloads client) {
        this.client = client;
    }

    public Downloads getClientMappings() {
        return clientMappings;
    }

    public void setClientMappings(Downloads clientMappings) {
        this.clientMappings = clientMappings;
    }

    public Downloads getServer() {
        return server;
    }

    public void setServer(Downloads server) {
        this.server = server;
    }

    public Downloads getServerMappings() {
        return serverMappings;
    }

    public void setServerMappings(Downloads serverMappings) {
        this.serverMappings = serverMappings;
    }

    public static class Downloads {
        private String sha1;
        private int size;
        private String url;

        public Downloads(String sha1, int size, String url) {
            this.sha1 = sha1;
            this.size = size;
            this.url = url;
        }

        public static Downloads parseVersionDownload(JsonObject downloadObj) {
            return new Downloads(
                    JsonUtils.getAsString(downloadObj, "sha1"),
                    JsonUtils.getAsInt(downloadObj, "size"),
                    JsonUtils.getAsString(downloadObj, "url")
            );
        }

        public String getSha1() {
            return sha1;
        }

        public void setSha1(String sha1) {
            this.sha1 = sha1;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
