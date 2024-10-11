package dev.treset.mcdl.auth.data;

import dev.treset.mcdl.json.GenericJsonParsable;
import dev.treset.mcdl.json.SerializationException;

import java.net.MalformedURLException;
import java.net.URL;

public class ProfileResponse extends GenericJsonParsable {
    private String id;
    private String name;
    private ProfileObject[] skins;
    private ProfileObject[] capes;
    private Object profileActions;

    public static class ProfileObject {
        private String id;
        private String state;
        private String url;
        private String textureKey;
        private String variant;
        private String alias;

        public ProfileObject(String id, String state, String url, String variant, String alias) {
            this.id = id;
            this.state = state;
            this.url = url;
            this.variant = variant;
            this.alias = alias;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getVariant() {
            return variant;
        }

        public void setVariant(String variant) {
            this.variant = variant;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }
    }

    public static ProfileResponse fromJson(String json) throws SerializationException {
        return fromJson(json, ProfileResponse.class);
    }

    public static URL getUrl() {
        try {
            return new URL("https://api.minecraftservices.com/minecraft/profile");
        } catch (MalformedURLException e) {
            // This doesn't happen
            throw new RuntimeException("Failed to create url", e);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProfileObject[] getSkins() {
        return skins;
    }

    public void setSkins(ProfileObject[] skins) {
        this.skins = skins;
    }

    public ProfileObject[] getCapes() {
        return capes;
    }

    public void setCapes(ProfileObject[] capes) {
        this.capes = capes;
    }
}
