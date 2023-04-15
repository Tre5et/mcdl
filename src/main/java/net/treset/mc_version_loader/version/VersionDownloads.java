package net.treset.mc_version_loader.version;

public class VersionDownloads {
    private Downloads client;
    private Downloads clientMappings;
    private Downloads server;
    private Downloads serverMappings;

    public VersionDownloads(Downloads client, Downloads clientMappings, Downloads server, Downloads serverMappings) {
        this.client = client;
        this.clientMappings = clientMappings;
        this.server = server;
        this.serverMappings = serverMappings;
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
