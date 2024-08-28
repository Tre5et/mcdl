package net.treset.mc_version_loader.auth;

import com.microsoft.aad.msal4j.*;
import net.treset.mc_version_loader.auth.data.MinecraftTokenResponse;
import net.treset.mc_version_loader.auth.data.TokenResponse;
import net.treset.mc_version_loader.auth.token.TokenPolicy;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Set;

public class MinecraftAuth {
    public static AuthenticationData authenticate() throws AuthenticationException {
        IAuthenticationResult msalResult = Msa.authenticate();
        if(msalResult.accessToken() == null) {
            throw new AuthenticationException("No access token");
        }
        TokenResponse xblResult = Xbl.authenticate(msalResult.accessToken());
        if(xblResult.getToken() == null) {
            throw new AuthenticationException("No XBL token");
        }
        TokenResponse xstsResult = Xsts.authenticate(xblResult.getToken());
        if(xstsResult.getToken() == null) {
            throw new AuthenticationException("No XSTS token");
        }
        if(xstsResult.getDisplayClaims() == null || xstsResult.getDisplayClaims().getXui() == null || xstsResult.getDisplayClaims().getXui().length == 0) {
            throw new AuthenticationException("No XUI claims");
        }
        MinecraftTokenResponse minecraftResult = Minecraft.authenticate(xstsResult.getToken(), xstsResult.getDisplayClaims().getXui()[0].getUhs());

        return new AuthenticationData(
                msalResult,
                xblResult,
                xstsResult,
                minecraftResult,
                null
        );
    }

    public static String getClientId() {
        return Msa.getClientId();
    }

    public static void setClientId(String clientId) {
        Msa.setClientId(clientId);
    }

    public static TokenPolicy getTokenPolicy() {
        return Msa.getTokenPolicy();
    }

    public static void setTokenPolicy(TokenPolicy tokenPolicy) {
        Msa.setTokenPolicy(tokenPolicy);
    }

    public static String getAuthority() {
        return Msa.getAuthority();
    }

    public static void setAuthority(String authority) throws MalformedURLException {
        Msa.setAuthority(authority);
    }

    public static Set<String> getScopes() {
        return Msa.getScopes();
    }

    public static void setScopes(Set<String> scopes) {
        Msa.setScopes(scopes);
    }

    public static String getRedirectUri() {
        return Msa.getRedirectUri();
    }

    public static void setRedirectUri(String redirectUri) throws URISyntaxException {
        Msa.setRedirectUri(redirectUri);
    }
}
