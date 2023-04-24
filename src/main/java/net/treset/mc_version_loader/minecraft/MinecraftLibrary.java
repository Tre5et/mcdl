package net.treset.mc_version_loader.minecraft;

import java.util.List;

public class MinecraftLibrary {
    private String name;
    private Downloads downloads;
    private List<MinecraftRule> rules;

    public static class Downloads {
        private Artifact artifact;

        public static class Artifact {
            private String path;
            private String sha1;
            private int size;
            private String url;

            public Artifact(String path, String sha1, int size, String url) {
                this.path = path;
                this.sha1 = sha1;
                this.size = size;
                this.url = url;
            }

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
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

        public Downloads(Artifact artifact) {
            this.artifact = artifact;
        }

        public Artifact getArtifacts() {
            return artifact;
        }

        public void setArtifacts(Artifact artifact) {
            this.artifact = artifact;
        }
    }

    public boolean isApplicable(List<String> activeFeatures) {
        for(MinecraftRule r : getRules()) {
            if(!r.isApplicable(activeFeatures)) {
                return false;
            }
        }
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Downloads getDownloads() {
        return downloads;
    }

    public void setDownloads(Downloads downloads) {
        this.downloads = downloads;
    }

    public List<MinecraftRule> getRules() {
        return rules;
    }

    public void setRules(List<MinecraftRule> rules) {
        this.rules = rules;
    }
}
