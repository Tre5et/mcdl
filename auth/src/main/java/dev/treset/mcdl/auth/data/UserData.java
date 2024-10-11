package dev.treset.mcdl.auth.data;

public class UserData {
    private String username;
    private String uuid;
    private String accessToken;
    private String xuid;
    private ProfileResponse.ProfileObject[] skins;
    private ProfileResponse.ProfileObject[] capes;

    public UserData(String username, String uuid, String accessToken, String xuid, ProfileResponse.ProfileObject[] skins, ProfileResponse.ProfileObject[] capes) {
        this.username = username;
        this.uuid = uuid;
        this.accessToken = accessToken;
        this.xuid = xuid;
        this.skins = skins;
        this.capes = capes;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getXuid() {
        return xuid;
    }

    public void setXuid(String xuid) {
        this.xuid = xuid;
    }

    public ProfileResponse.ProfileObject[] getSkins() {
        return skins;
    }

    public void setSkins(ProfileResponse.ProfileObject[] skins) {
        this.skins = skins;
    }

    public ProfileResponse.ProfileObject[] getCapes() {
        return capes;
    }

    public void setCapes(ProfileResponse.ProfileObject[] capes) {
        this.capes = capes;
    }
}
