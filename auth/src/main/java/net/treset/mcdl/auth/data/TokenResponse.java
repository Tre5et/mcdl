package net.treset.mcdl.auth.data;

import com.google.gson.annotations.SerializedName;
import net.treset.mcdl.json.GenericJsonParsable;
import net.treset.mcdl.json.SerializationException;

public class TokenResponse extends GenericJsonParsable {
    @SerializedName("IssueInstant")
    private String issueInstant;
    @SerializedName("NotAfter")
    private String notAfter;
    @SerializedName("Token")
    private String token;
    @SerializedName("DisplayClaims")
    private DisplayClaims displayClaims;

    public static class DisplayClaims {
        @SerializedName("xui")
        private Xui[] xui;

        public DisplayClaims(Xui[] xui) {
            this.xui = xui;
        }

        public static class Xui {
            @SerializedName("uhs")
            private String uhs;

            public Xui(String uhs) {
                this.uhs = uhs;
            }

            public String getUhs() {
                return uhs;
            }

            public void setUhs(String uhs) {
                this.uhs = uhs;
            }
        }

        public Xui[] getXui() {
            return xui;
        }

        public void setXui(Xui[] xui) {
            this.xui = xui;
        }
    }

    public TokenResponse(String issueInstant, String notAfter, String token, DisplayClaims displayClaims) {
        this.issueInstant = issueInstant;
        this.notAfter = notAfter;
        this.token = token;
        this.displayClaims = displayClaims;
    }

    public static TokenResponse fromJson(String json) throws SerializationException {
        return fromJson(json, TokenResponse.class);
    }

    public String getIssueInstant() {
        return issueInstant;
    }

    public void setIssueInstant(String issueInstant) {
        this.issueInstant = issueInstant;
    }

    public String getNotAfter() {
        return notAfter;
    }

    public void setNotAfter(String notAfter) {
        this.notAfter = notAfter;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public DisplayClaims getDisplayClaims() {
        return displayClaims;
    }

    public void setDisplayClaims(DisplayClaims displayClaims) {
        this.displayClaims = displayClaims;
    }
}
