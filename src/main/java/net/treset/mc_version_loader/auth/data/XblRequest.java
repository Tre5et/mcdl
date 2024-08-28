package net.treset.mc_version_loader.auth.data;

import com.google.gson.annotations.SerializedName;
import net.treset.mc_version_loader.json.GenericJsonParsable;

import java.net.URI;

public class XblRequest extends GenericJsonParsable {
    @SerializedName("Properties")
    private Properties properties;
    @SerializedName("RelyingParty")
    private String relyingParty = "http://auth.xboxlive.com";
    @SerializedName("TokenType")
    private String tokenType = "JWT";

    public static class Properties {
        @SerializedName("AuthMethod")
        private String authMethod = "RPS";
        @SerializedName("SiteName")
        private String siteName = "user.auth.xboxlive.com";
        @SerializedName("RpsTicket")
        private String rpsTicket;

        public Properties(String authMethod, String siteName, String rpsTicket) {
            this.authMethod = authMethod;
            this.siteName = siteName;
            this.rpsTicket = rpsTicket;
        }

        public Properties(String accessToken) {
            this.rpsTicket = "d=" + accessToken;
        }

        public String getAuthMethod() {
            return authMethod;
        }

        public void setAuthMethod(String authMethod) {
            this.authMethod = authMethod;
        }

        public String getSiteName() {
            return siteName;
        }

        public void setSiteName(String siteName) {
            this.siteName = siteName;
        }

        public String getRpsTicket() {
            return rpsTicket;
        }

        public void setRpsTicket(String rpsTicket) {
            this.rpsTicket = rpsTicket;
        }
    }

    public XblRequest(Properties properties, String relyingParty, String tokenType) {
        this.properties = properties;
        this.relyingParty = relyingParty;
        this.tokenType = tokenType;
    }

    public XblRequest(String accessToken) {
        this.properties = new Properties(accessToken);
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getRelyingParty() {
        return relyingParty;
    }

    public void setRelyingParty(String relyingParty) {
        this.relyingParty = relyingParty;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public static URI getUrl() {
        try {
            return new URI("https://user.auth.xboxlive.com/user/authenticate");
        } catch (Exception e) {
            // This doesn't happen
            throw new RuntimeException("Failed to create URL", e);
        }
    }
}
