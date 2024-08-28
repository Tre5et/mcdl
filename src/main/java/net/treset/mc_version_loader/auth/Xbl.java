package net.treset.mc_version_loader.auth;

import net.treset.mc_version_loader.auth.data.TokenResponse;
import net.treset.mc_version_loader.auth.data.XblRequest;
import net.treset.mc_version_loader.json.SerializationException;
import net.treset.mc_version_loader.util.HttpUtil;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Map;

public class Xbl {
    public static TokenResponse authenticate(String msalAccessToken) throws AuthenticationException {
        XblRequest request = new XblRequest(msalAccessToken);
        Map<String, String> headers = Map.of(
                "Content-Type", "application/json",
                "Accept", "application/json"
        );
        try {
            HttpResponse<byte[]> response = HttpUtil.post(XblRequest.getUrl(), request.toJson().getBytes(), headers);
            if(response.statusCode() != 200) {
                throw new AuthenticationException("Failed to authenticate with Xbox Live: Wrong status code: " + response.statusCode() + ": " + new String(response.body()));
            }
            try {
                return TokenResponse.fromJson(new String(response.body()));
            } catch (SerializationException e) {
                throw new AuthenticationException("Failed to authenticate with Xbox Live: Failed to parse response: " +  new String(response.body()), e);
            }
        } catch (IOException | InterruptedException e) {
            throw new AuthenticationException("Failed to authenticate with Xbox Live", e);
        }
    }
}
