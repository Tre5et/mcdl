package net.treset.mcdl.auth;

import com.microsoft.aad.msal4j.IAuthenticationResult;
import net.treset.mcdl.auth.data.MinecraftTokenResponse;
import net.treset.mcdl.auth.data.TokenResponse;

public class AuthenticationData {
    private IAuthenticationResult msal;
    private TokenResponse xbl;
    private TokenResponse xsts;
    private MinecraftTokenResponse minecraft;
    private UserData user;

    public AuthenticationData(IAuthenticationResult msal, TokenResponse xbl, TokenResponse xsts, MinecraftTokenResponse minecraft, UserData user) {
        this.msal = msal;
        this.xbl = xbl;
        this.xsts = xsts;
        this.minecraft = minecraft;
        this.user = user;
    }

    public IAuthenticationResult getMsal() {
        return msal;
    }

    public void setMsal(IAuthenticationResult msal) {
        this.msal = msal;
    }

    public TokenResponse getXbl() {
        return xbl;
    }

    public void setXbl(TokenResponse xbl) {
        this.xbl = xbl;
    }

    public TokenResponse getXsts() {
        return xsts;
    }

    public void setXsts(TokenResponse xsts) {
        this.xsts = xsts;
    }

    public MinecraftTokenResponse getMinecraft() {
        return minecraft;
    }

    public void setMinecraft(MinecraftTokenResponse minecraft) {
        this.minecraft = minecraft;
    }

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }
}
