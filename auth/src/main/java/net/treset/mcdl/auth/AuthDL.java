package net.treset.mcdl.auth;

import net.treset.mcdl.auth.data.MinecraftTokenResponse;
import net.treset.mcdl.auth.data.ProfileResponse;
import net.treset.mcdl.auth.data.TokenResponse;
import net.treset.mcdl.auth.data.UserData;
import net.treset.mcdl.auth.token.TokenPolicy;

import com.microsoft.aad.msal4j.IAuthenticationResult;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Set;

public class AuthDL {
    public static UserData authenticate() throws AuthenticationException {
        AuthenticationData data = runAuthenticationSteps();
        return data.toUserData();
    }

    public static AuthenticationData runAuthenticationSteps() throws AuthenticationException {
        IAuthenticationResult msalResult = MsaAuth.authenticate();
        if(msalResult.accessToken() == null) {
            throw new AuthenticationException("No access token");
        }

        TokenResponse xblResult = XblAuth.authenticate(msalResult.accessToken());
        if(xblResult.getToken() == null) {
            throw new AuthenticationException("No XBL token");
        }

        TokenResponse xstsResult = XstsAuth.authenticate(xblResult.getToken());
        if(xstsResult.getToken() == null) {
            throw new AuthenticationException("No XSTS token");
        }
        if(xstsResult.getDisplayClaims() == null || xstsResult.getDisplayClaims().getXui() == null || xstsResult.getDisplayClaims().getXui().length == 0) {
            throw new AuthenticationException("No XUI claims");
        }

        MinecraftTokenResponse minecraftResult = MinecraftAuth.authenticate( xstsResult.getDisplayClaims().getXui()[0].getUhs(), xstsResult.getToken());

        if(minecraftResult.getAccessToken() == null) {
            throw new AuthenticationException("No Minecraft access token");
        }
        ProfileResponse profile = MinecraftAuth.getProfile(minecraftResult.getAccessToken());

        return new AuthenticationData(
                msalResult,
                xblResult,
                xstsResult,
                minecraftResult,
                profile
        );
    }

    public static String getClientId() {
        return MsaAuth.getClientId();
    }

    public static void setClientId(String clientId) {
        MsaAuth.setClientId(clientId);
    }

    public static TokenPolicy getTokenPolicy() {
        return MsaAuth.getTokenPolicy();
    }

    public static void setTokenPolicy(TokenPolicy tokenPolicy) {
        MsaAuth.setTokenPolicy(tokenPolicy);
    }

    public static String getAuthority() {
        return MsaAuth.getAuthority();
    }

    public static void setAuthority(String authority) throws MalformedURLException {
        MsaAuth.setAuthority(authority);
    }

    public static Set<String> getScopes() {
        return MsaAuth.getScopes();
    }

    public static void setScopes(Set<String> scopes) {
        MsaAuth.setScopes(scopes);
    }

    public static String getRedirectUri() {
        return MsaAuth.getRedirectUri();
    }

    public static void setRedirectUri(String redirectUri) throws URISyntaxException {
        MsaAuth.setRedirectUri(redirectUri);
    }
}
