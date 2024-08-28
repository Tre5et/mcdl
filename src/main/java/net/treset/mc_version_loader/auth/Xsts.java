package net.treset.mc_version_loader.auth;

import net.treset.mc_version_loader.auth.data.TokenResponse;
import net.treset.mc_version_loader.auth.data.XstsError;
import net.treset.mc_version_loader.auth.data.XstsRequest;
import net.treset.mc_version_loader.json.SerializationException;
import net.treset.mc_version_loader.util.HttpUtil;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Map;

public class Xsts {
    public static TokenResponse authenticate(String xblToken) throws AuthenticationException {
        XstsRequest request = new XstsRequest(xblToken);
        Map<String, String> headers = Map.of(
                "Content-Type", "application/json",
                "Accept", "application/json"
        );
        try {
            HttpResponse<byte[]> response = HttpUtil.post(XstsRequest.getUrl(), request.toJson().getBytes(), headers);
            if(response.statusCode() != 200) {
                try {
                    XstsError error = XstsError.fromJson(new String(response.body()));
                    throw new AuthenticationException("Failed to authenticate with XSTS: Status: " + response.statusCode() + ", Error:" + error.getxErr() + " <=> " + error.getError().getMessage());
                } catch (SerializationException e) {
                    throw new AuthenticationException("Failed to authenticate with XSTS: Status: " + response.statusCode() + ", Failed to parse error response: " +  new String(response.body()), e);
                }
            }
            try {
                return TokenResponse.fromJson(new String(response.body()));
            } catch (SerializationException e) {
                throw new AuthenticationException("Failed to authenticate with XSTS: Failed to parse response: " +  new String(response.body()), e);
            }
        } catch (IOException | InterruptedException e) {
            throw new AuthenticationException("Failed to authenticate with XSTS", e);
        }
    }
}
