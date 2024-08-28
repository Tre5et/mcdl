package net.treset.mcdl.auth.data;

import net.treset.mcdl.json.GenericJsonParsable;

public class ProfileResponse extends GenericJsonParsable {
    private String id;
    private String name;
    private Skin[] skins;
    private String[] capes;

    public static class Skin {
        private String id;
        private String state;
        private String url;
        private String variant;
        private String alias;

        public Skin(String id, String state, String url, String variant, String alias) {
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
}
