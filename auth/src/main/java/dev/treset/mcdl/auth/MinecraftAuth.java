package dev.treset.mcdl.auth;

import dev.treset.mcdl.auth.data.MinecraftTokenRequest;
import dev.treset.mcdl.auth.data.MinecraftTokenResponse;
import dev.treset.mcdl.auth.data.ProfileResponse;
import dev.treset.mcdl.json.SerializationException;
import dev.treset.mcdl.util.HttpUtil;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Map;

public class MinecraftAuth {
    public static MinecraftTokenResponse authenticate(String userHash, String xstsToken) throws AuthenticationException {
        MinecraftTokenRequest request = new MinecraftTokenRequest(userHash, xstsToken);
        Map<String, String> headers = Map.of(
                "Content-Type", "application/json",
                "Accept", "application/json"
        );
        try {
            HttpResponse<byte[]> response = HttpUtil.post(MinecraftTokenRequest.getUrl(), request.toJson().getBytes(), headers, Map.of());
            if (response.statusCode() != 200) {
                throw new AuthenticationException("Failed to authenticate with Minecraft: Wrong status code: " + response.statusCode() + ": " + new String(response.body()));
            }
            try {
                return MinecraftTokenResponse.fromJson(new String(response.body()));
            } catch (SerializationException e) {
                throw new AuthenticationException("Failed to authenticate with Minecraft: Failed to parse response: " + new String(response.body()), e);
            }
        } catch (IOException e) {
            throw new AuthenticationException("Failed to authenticate with Minecraft", e);
        }
    }

    public static ProfileResponse getProfile(String accessToken) throws AuthenticationException {
        Map<String, String> headers = Map.of(
                "Accept", "application/json",
                "Authorization", "Bearer " + accessToken
        );

        try {
            HttpResponse<byte[]> response = HttpUtil.get(ProfileResponse.getUrl(), headers, Map.of());
            if (response.statusCode() != 200) {
                throw new AuthenticationException("Failed to get profile: Wrong status code: " + response.statusCode() + ": " + new String(response.body()));
            }
            try {
                return ProfileResponse.fromJson(new String(response.body()));
            } catch (SerializationException e) {
                throw new AuthenticationException("Failed to get profile: Failed to parse response: " + new String(response.body()), e);
            }
        } catch (IOException e) {
            throw new AuthenticationException("Failed to get profile", e);
        }
    }
}
