package net.treset.mc_version_loader.mojang;

import net.treset.mc_version_loader.json.GenericJsonParsable;
import net.treset.mc_version_loader.json.JsonUtils;
import net.treset.mc_version_loader.json.SerializationException;

public class MinecraftProfileTextures extends GenericJsonParsable {
    public static class Textures {
        public static class Skin {
            public static class Metadata {
                private String model;

                public Metadata(String model) {
                    this.model = model;
                }

                public String getModel() {
                    return model;
                }

                public void setModel(String model) {
                    this.model = model;
                }
            }

            private String url;
            private Metadata metadata;

            public Skin(String url, Metadata metadata) {
                this.url = url;
                this.metadata = metadata;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public Metadata getMetadata() {
                return metadata;
            }

            public void setMetadata(Metadata metadata) {
                this.metadata = metadata;
            }
        }

        public static class Cape {
            private String url;

            public Cape(String url) {
                this.url = url;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        private Skin SKIN;
        private Cape CAPE;

        public Textures(Skin SKIN, Cape CAPE) {
            this.SKIN = SKIN;
            this.CAPE = CAPE;
        }

        public Skin getSKIN() {
            return SKIN;
        }

        public void setSKIN(Skin SKIN) {
            this.SKIN = SKIN;
        }

        public Cape getCAPE() {
            return CAPE;
        }

        public void setCAPE(Cape CAPE) {
            this.CAPE = CAPE;
        }
    }
    private String timestamp;
    private String profileId;
    private String profileName;
    private boolean isPublic;
    private Textures textures;

    public MinecraftProfileTextures(String timestamp, String profileId, String profileName, boolean isPublic, Textures textures) {
        this.timestamp = timestamp;
        this.profileId = profileId;
        this.profileName = profileName;
        this.isPublic = isPublic;
        this.textures = textures;
    }

    public static MinecraftProfileTextures fromJson(String json) throws SerializationException {
        return fromJson(json, MinecraftProfileTextures.class, JsonUtils.getGsonCamelCase());
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public Textures getTextures() {
        return textures;
    }

    public void setTextures(Textures textures) {
        this.textures = textures;
    }
}
