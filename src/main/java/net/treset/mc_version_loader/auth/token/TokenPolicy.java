package net.treset.mc_version_loader.auth.token;
import com.microsoft.aad.msal4j.ITokenCacheAccessAspect;
import com.microsoft.aad.msal4j.ITokenCacheAccessContext;

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
}
