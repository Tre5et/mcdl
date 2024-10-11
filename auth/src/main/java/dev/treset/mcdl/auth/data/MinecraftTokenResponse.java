package dev.treset.mcdl.auth.data;

import com.google.gson.annotations.SerializedName;
import dev.treset.mcdl.json.GenericJsonParsable;
import dev.treset.mcdl.json.SerializationException;

public class MinecraftTokenResponse extends GenericJsonParsable {
    @SerializedName("username")
    private String username;
    @SerializedName("roles")
    private String[] roles;
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("token_type")
    private String tokenType;
    @SerializedName("expires_in")
    private int expiresIn;

    public MinecraftTokenResponse(String username, String[] roles, String accessToken, String tokenType, int expiresIn) {
        this.username = username;
        this.roles = roles;
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }

    public static MinecraftTokenResponse fromJson(String json) throws SerializationException {
        return fromJson(json, MinecraftTokenResponse.class);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}
