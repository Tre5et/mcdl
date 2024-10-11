package dev.treset.mcdl.auth;

import dev.treset.mcdl.auth.data.TokenResponse;
import dev.treset.mcdl.auth.data.XblRequest;
import dev.treset.mcdl.json.SerializationException;
import dev.treset.mcdl.util.HttpUtil;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Map;

public class XblAuth {
    public static TokenResponse authenticate(String msalAccessToken) throws AuthenticationException {
        XblRequest request = new XblRequest(msalAccessToken);
        Map<String, String> headers = Map.of(
                "Content-Type", "application/json",
                "Accept", "application/json"
        );
        try {
            HttpResponse<byte[]> response = HttpUtil.post(XblRequest.getUrl(), request.toJson().getBytes(), headers, Map.of());
            if(response.statusCode() != 200) {
                throw new AuthenticationException("Failed to authenticate with Xbox Live: Wrong status code: " + response.statusCode() + ": " + new String(response.body()));
            }
            try {
                return TokenResponse.fromJson(new String(response.body()));
            } catch (SerializationException e) {
                throw new AuthenticationException("Failed to authenticate with Xbox Live: Failed to parse response: " +  new String(response.body()), e);
            }
        } catch (IOException e) {
            throw new AuthenticationException("Failed to authenticate with Xbox Live", e);
        }
    }
}
