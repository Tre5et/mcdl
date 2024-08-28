package net.treset.mc_version_loader.auth.data;

import com.google.gson.annotations.SerializedName;
import net.treset.mc_version_loader.json.GenericJsonParsable;

import java.net.URI;

public class XstsRequest extends GenericJsonParsable {
    @SerializedName("Properties")
    private Properties properties;
    @SerializedName("RelyingParty")
    private String relyingParty = "rp://api.minecraftservices.com/";
    @SerializedName("TokenType")
    private String tokenType = "JWT";

    public static class Properties {
        @SerializedName("SandboxId")
        private String sandboxId = "RETAIL";
        @SerializedName("UserTokens")
        private String[] userTokens;

        public Properties(String sandboxId, String[] userTokens) {
            this.sandboxId = sandboxId;
            this.userTokens = userTokens;
        }

        public Properties(String xblToken) {
            this.userTokens = new String[] { xblToken };
        }

        public String getSandboxId() {
            return sandboxId;
        }

        public void setSandboxId(String sandboxId) {
            this.sandboxId = sandboxId;
        }

        public String[] getUserTokens() {
            return userTokens;
        }

        public void setUserTokens(String[] userTokens) {
            this.userTokens = userTokens;
        }
    }

    public XstsRequest(Properties properties, String relyingParty, String tokenType) {
        this.properties = properties;
        this.relyingParty = relyingParty;
        this.tokenType = tokenType;
    }

    public XstsRequest(String xblToken) {
        this.properties = new Properties(xblToken);
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
            return new URI("https://xsts.auth.xboxlive.com/xsts/authorize");
        } catch (Exception e) {
            // This doesn't happen
            throw new RuntimeException("Failed to create URL", e);
        }
    }
}
