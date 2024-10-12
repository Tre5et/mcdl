package dev.treset.mcdl.mods;

import java.util.List;

public class LocalModVersion {
    private ModVersionData version;
    private ModProvider activeProvider;
    private String fileName;
    private List<LocalModDownload> downloads;

    public static class LocalModDownload {
        private ModProvider provider;
        private String id;

        public LocalModDownload(ModProvider provider, String id) {
            this.provider = provider;
            this.id = id;
        }

        public ModProvider getProvider() {
            return provider;
        }

        public void setProvider(ModProvider provider) {
            this.provider = provider;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public LocalModVersion(ModVersionData version, ModProvider activeProvider, String fileName, List<LocalModDownload> downloads) {
        this.version = version;
        this.activeProvider = activeProvider;
        this.fileName = fileName;
        this.downloads = downloads;
    }

    public ModVersionData getVersion() {
        return version;
    }

    public void setVersion(ModVersionData version) {
        this.version = version;
    }

    public ModProvider getActiveProvider() {
        return activeProvider;
    }

    public void setActiveProvider(ModProvider activeProvider) {
        this.activeProvider = activeProvider;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<LocalModDownload> getDownloads() {
        return downloads;
    }

    public void setDownloads(List<LocalModDownload> downloads) {
        this.downloads = downloads;
    }
}
