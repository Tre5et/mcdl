package net.treset.mc_version_loader.auth;

import com.microsoft.aad.msal4j.*;
import net.treset.mc_version_loader.auth.token.DefaultTokenPolicy;
import net.treset.mc_version_loader.auth.token.TokenPolicy;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Set;
import java.util.function.Consumer;

public class Msa {
    private static String clientId;
    private static TokenPolicy tokenPolicy = new DefaultTokenPolicy();
    private static String authority = "https://login.microsoftonline.com/consumers/oauth2/v2.0/devicecode";
    private static Set<String> scopes = Set.of("XboxLive.signin");
    private static String redirectUri = "http://localhost:9095";

    public static IAuthenticationResult authenticate() throws AuthenticationException {
        IPublicClientApplication app = createPublicClientApplication();
        return authenticateMsal(app);
    }

    private static void requireReady() throws AuthenticationException {
        if (clientId == null) {
            throw new AuthenticationException("Client ID not set");
        }
    }

    public static IPublicClientApplication createPublicClientApplication() throws AuthenticationException {
        requireReady();
        try {
            return PublicClientApplication
                    .builder(clientId)
                    .authority(authority)
                    .setTokenCacheAccessAspect(tokenPolicy)
                    .build();
        } catch (MalformedURLException e) {
            // This doesn't happen
            throw new RuntimeException("Invalid Url", e);
        }
    }

    public static IAuthenticationResult authenticateMsal(IPublicClientApplication app) throws AuthenticationException {
        Set<IAccount> accounts = app.getAccounts().join();
        if (accounts.isEmpty()) {
            return interactiveMsal(app);
        } else {
            Exception lastException = null;
            for(IAccount account : accounts) {
                try {
                    SilentParameters silentParameters = SilentParameters
                            .builder(scopes, account)
                            .build();
                    return app.acquireTokenSilently(silentParameters).join();
                } catch (Exception ex) {
                    lastException = ex;
                }
            }
            throw new AuthenticationException("Failed to acquire MSA token silently", lastException);
        }
    }

    public static IAuthenticationResult interactiveMsal(IPublicClientApplication app) throws AuthenticationException {
        try {
            Consumer<DeviceCode> deviceCodeConsumer = (DeviceCode deviceCode) -> {
                System.out.println(deviceCode.verificationUri());
                System.out.println(deviceCode.userCode());
            };
            DeviceCodeFlowParameters parameters = DeviceCodeFlowParameters
                    .builder(scopes, deviceCodeConsumer)
                    .build();
            return app.acquireToken(parameters).join();
        } catch (Exception e) {
            throw new AuthenticationException("Failed to acquire MSA token interactively", e);
        }
    }

    public static String getClientId() {
        return clientId;
    }

    public static void setClientId(String clientId) {
        Msa.clientId = clientId;
    }

    public static TokenPolicy getTokenPolicy() {
        return tokenPolicy;
    }

    public static void setTokenPolicy(TokenPolicy tokenPolicy) {
        Msa.tokenPolicy = tokenPolicy;
    }

    public static String getAuthority() {
        return authority;
    }

    public static void setAuthority(String authority) throws MalformedURLException {
        new URL(authority);
        Msa.authority = authority;
    }

    public static Set<String> getScopes() {
        return scopes;
    }

    public static void setScopes(Set<String> scopes) {
        Msa.scopes = scopes;
    }

    public static String getRedirectUri() {
        return redirectUri;
    }

    public static void setRedirectUri(String redirectUri) throws URISyntaxException {
        new URI(redirectUri);
        Msa.redirectUri = redirectUri;
    }
}
