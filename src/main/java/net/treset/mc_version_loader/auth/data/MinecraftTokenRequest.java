package net.treset.mc_version_loader.auth.data;

import com.google.gson.annotations.SerializedName;
import net.treset.mc_version_loader.json.GenericJsonParsable;

import java.net.URI;

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

    public static URI getUrl() {
        try {
            return new URI("https://api.minecraftservices.com/authentication/login_with_xbox");
        } catch (Exception e) {
            // This doesn't happen
            throw new RuntimeException("Failed to create URL", e);
        }
    }
}
