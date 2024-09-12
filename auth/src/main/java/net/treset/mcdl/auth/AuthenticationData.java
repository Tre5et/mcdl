package net.treset.mcdl.auth;

import com.microsoft.aad.msal4j.IAuthenticationResult;
import net.treset.mcdl.auth.data.MinecraftTokenResponse;
import net.treset.mcdl.auth.data.ProfileResponse;
import net.treset.mcdl.auth.data.TokenResponse;
import net.treset.mcdl.auth.data.UserData;

public class AuthenticationData {
    private IAuthenticationResult msal;
    private TokenResponse xbl;
    private TokenResponse xsts;
    private MinecraftTokenResponse minecraft;
    private ProfileResponse profile;

    public AuthenticationData(IAuthenticationResult msal, TokenResponse xbl, TokenResponse xsts, MinecraftTokenResponse minecraft, ProfileResponse profile) {
        this.msal = msal;
        this.xbl = xbl;
        this.xsts = xsts;
        this.minecraft = minecraft;
        this.profile = profile;
    }

    public UserData toUserData() {
        return new UserData(
                profile.getName(),
                profile.getId(),
                minecraft.getAccessToken(),
                xbl.getDisplayClaims().getXui()[0].getUhs(),
                profile.getSkins(),
                profile.getCapes()
        );
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

    public ProfileResponse getProfile() {
        return profile;
    }

    public void setProfile(ProfileResponse profile) {
        this.profile = profile;
    }
}
