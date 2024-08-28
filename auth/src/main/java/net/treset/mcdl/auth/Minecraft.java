package net.treset.mcdl.auth;

import net.treset.mcdl.auth.data.MinecraftTokenRequest;
import net.treset.mcdl.auth.data.MinecraftTokenResponse;
import net.treset.mcdl.json.SerializationException;
import net.treset.mcdl.util.HttpUtil;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Map;

public class Minecraft {
    public static MinecraftTokenResponse authenticate(String userHash, String xstsToken) throws AuthenticationException {
        MinecraftTokenRequest request = new MinecraftTokenRequest(userHash, xstsToken);
        Map<String, String> headers = Map.of(
                "Content-Type", "application/json",
                "Accept", "application/json"
        );
        try {
            HttpResponse<byte[]> response = HttpUtil.post(MinecraftTokenRequest.getUrl(), request.toJson().getBytes(), headers);
            if(response.statusCode() != 200) {
                throw new AuthenticationException("Failed to authenticate with Minecraft: Wrong status code: " + response.statusCode() + ": " + new String(response.body()));
            }
            try {
                return MinecraftTokenResponse.fromJson(new String(response.body()));
            } catch (SerializationException e) {
                throw new AuthenticationException("Failed to authenticate with Minecraft: Failed to parse response: " +  new String(response.body()), e);
            }
        } catch (IOException | InterruptedException e) {
            throw new AuthenticationException("Failed to authenticate with Minecraft", e);
        }
    }
}
