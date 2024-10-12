package dev.treset.mcdl.auth.token;
import com.microsoft.aad.msal4j.ITokenCacheAccessAspect;
import com.microsoft.aad.msal4j.ITokenCacheAccessContext;

import java.io.File;
import java.io.IOException;
import java.util.function.Function;

public abstract class TokenPolicy implements ITokenCacheAccessAspect {
    @Override
    public void beforeCacheAccess(ITokenCacheAccessContext iTokenCacheAccessContext) {
        String data = injectData();
        if(data != null && !data.isEmpty()) {
            iTokenCacheAccessContext.tokenCache().deserialize(data);
        }
    }

    @Override
    public void afterCacheAccess(ITokenCacheAccessContext iTokenCacheAccessContext) {
        String data = iTokenCacheAccessContext.tokenCache().serialize();
        extractData(data);
    }

    public abstract String injectData();

    public abstract void extractData(String data);

    /**
     * Create a token policy that never saves login data
     * @return The default token policy
     */
    public static DefaultTokenPolicy DEFAULT() {
        return new DefaultTokenPolicy();
    }

    /**
     * Create a token policy that always saves login data to a file
     * @param file The file to save the login data to
     * @param onException A function that supplies a string or null to give the authenticator if an exception occurs
     * @return The file token policy
     */
    public static FileTokenPolicy FILE(File file, Function<IOException, String> onException) {
        return new FileTokenPolicy(file, onException);
    }

    /**
     * Create a token policy that always saves login data to a file
     * @param file The file to save the login data to
     * @return The file token policy
     */
    public static FileTokenPolicy FILE(File file) {
        return new FileTokenPolicy(file);
    }

    /**
     * Create a token policy that saves login data to a file if the policy is active
     * @param file The file to save the login data to
     * @param active Whether the policy is active
     * @param onException A function that supplies a string or null to give the authenticator if an exception occurs
     * @return The optional file token policy
     */
    public static OptionalFileTokenPolicy FILE_OPTIONAL(File file, boolean active, Function<IOException, String> onException) {
        return new OptionalFileTokenPolicy(file, active, onException);
    }

    /**
     * Create a token policy that saves login data to a file if the policy is active
     * @param file The file to save the login data to
     * @param active Whether the policy is active
     * @return The optional file token policy
     */
    public static OptionalFileTokenPolicy FILE_OPTIONAL(File file, boolean active) {
        return new OptionalFileTokenPolicy(file, active);
    }
}
