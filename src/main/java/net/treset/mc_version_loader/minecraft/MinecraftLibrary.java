package net.treset.mc_version_loader.minecraft;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.GenericJsonParsable;
import net.treset.mc_version_loader.json.JsonUtils;
import net.treset.mc_version_loader.json.SerializationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MinecraftLibrary {
    private String name;
    private Downloads downloads;
    private List<MinecraftRule> rules;
    private Natives natives;

    public static class Natives {
        private String windows;
        private String linux;
        private String osx;

        public Natives(String windows, String linux, String osx) {
            this.windows = windows;
            this.linux = linux;
            this.osx = osx;
        }

        public String getWindows() {
            return windows;
        }

        public void setWindows(String windows) {
            this.windows = windows;
        }

        public String getLinux() {
            return linux;
        }

        public void setLinux(String linux) {
            this.linux = linux;
        }

        public String getOsx() {
            return osx;
        }

        public void setOsx(String osx) {
            this.osx = osx;
        }
    }

    public static class Downloads {
        private Artifact artifact;
        private Classifiers classifiers;

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

        public static class Classifiers {
            List<Native> natives;

            public static class Native {
                private String name;
                private Downloads.Artifact artifact;

                public Native(String name, Downloads.Artifact artifact) {
                    this.name = name;
                    this.artifact = artifact;
                }

                public static Native from(String name, JsonObject nativeObj) throws SerializationException {
                    if(nativeObj == null) {
                        return null;
                    }
                    return new Native(
                            name,
                            GenericJsonParsable.fromJson(nativeObj.toString(), Downloads.Artifact.class)
                    );
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public Downloads.Artifact getArtifact() {
                    return artifact;
                }

                public void setArtifact(Downloads.Artifact artifact) {
                    this.artifact = artifact;
                }
            }

            public static Classifiers from(JsonObject classifiers) throws SerializationException {
                Set<Map.Entry<String, JsonElement>> entries = JsonUtils.getMembers(classifiers);
                if(entries == null) {
                    return null;
                }
                List<Native> natives = new ArrayList<>();
                for(Map.Entry<String, JsonElement> e : entries) {
                    natives.add(Native.from(e.getKey(), JsonUtils.getAsJsonObject(e.getValue())));
                }
                return new Classifiers(natives);
            }

            public Classifiers(List<Native> natives) {
                this.natives = natives;
            }

            public List<Native> getNatives() {
                return natives;
            }

            public void setNatives(List<Native> natives) {
                this.natives = natives;
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

        public Artifact getArtifact() {
            return artifact;
        }

        public void setArtifact(Artifact artifact) {
            this.artifact = artifact;
        }

        public Classifiers getClassifiers() {
            return classifiers;
        }

        public void setClassifiers(Classifiers classifiers) {
            this.classifiers = classifiers;
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

    public Natives getNatives() {
        return natives;
    }

    public void setNatives(Natives natives) {
        this.natives = natives;
    }
}
