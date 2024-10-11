package dev.treset.mcdl.minecraft;

import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.util.FileUtil;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class MinecraftFileDownloads {
    private Downloads client;
    private Downloads client_mappings;
    private Downloads server;
    private Downloads server_mappings;

    public MinecraftFileDownloads(Downloads client, Downloads clientMappings, Downloads server, Downloads serverMappings) {
        this.client = client;
        this.client_mappings = clientMappings;
        this.server = server;
        this.server_mappings = serverMappings;
    }

    public Downloads getClient() {
        return client;
    }

    public void setClient(Downloads client) {
        this.client = client;
    }

    public Downloads getClientMappings() {
        return client_mappings;
    }

    public void setClientMappings(Downloads clientMappings) {
        this.client_mappings = clientMappings;
    }

    public Downloads getServer() {
        return server;
    }

    public void setServer(Downloads server) {
        this.server = server;
    }

    public Downloads getServerMappings() {
        return server_mappings;
    }

    public void setServerMappings(Downloads serverMappings) {
        this.server_mappings = serverMappings;
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

        /**
         * Downloads the file to the target file.
         * @param targetFile the file to download to
         * @throws FileDownloadException if there is an error downloading the file
         */
        public void download(File targetFile) throws FileDownloadException {
            if(getUrl() == null || getUrl().isBlank() || targetFile == null || !targetFile.getParentFile().isDirectory()) {
                throw new FileDownloadException("Unmet requirements for version download: download=" + getUrl());
            }

            URL downloadUrl;
            try {
                downloadUrl = new URL(getUrl());
            } catch (MalformedURLException e) {
                throw new FileDownloadException("Unable to convert version download url: download=" + getUrl(), e);
            }

            FileUtil.downloadFile(downloadUrl, targetFile);
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
