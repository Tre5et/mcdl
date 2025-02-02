package dev.treset.mcdl.auth.data;

import com.google.gson.annotations.SerializedName;
import dev.treset.mcdl.json.GenericJsonParsable;

import java.net.URL;

public class MinecraftTokenRequest extends GenericJsonParsable {
    @SerializedName("identityToken")
    private String identityToken;

    public MinecraftTokenRequest(String userhash, String xstsToken) {
        this.identityToken = "XBL3.0 x=" + userhash + ";" + xstsToken;
    }

    public String getIdentityToken() {
        return identityToken;
    }

    public void setIdentityToken(String identityToken) {
        this.identityToken = identityToken;
    }

    public static URL getUrl() {
        try {
            return new URL("https://api.minecraftservices.com/authentication/login_with_xbox");
        } catch (Exception e) {
            // This doesn't happen
            throw new RuntimeException("Failed to create URL", e);
        }
    }
}
