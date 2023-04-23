package net.treset.mc_version_loader.minecraft;

import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;

public class MinecraftLogging {
    private String clientArgument;
    private String clientFileId;
    private String clientFileSha1;
    private int clientFileSize;
    private String clientFileUrl;
    private String clientType;

    public MinecraftLogging(String clientArgument, String clientFileId, String clientFileSha1, int clientFileSize, String clientFileUrl, String clientType) {
        this.clientArgument = clientArgument;
        this.clientFileId = clientFileId;
        this.clientFileSha1 = clientFileSha1;
        this.clientFileSize = clientFileSize;
        this.clientFileUrl = clientFileUrl;
        this.clientType = clientType;
    }

    public static MinecraftLogging fromJson(JsonObject loggingObj) {
        JsonObject clientObj = JsonUtils.getAsJsonObject(loggingObj, "client");
        JsonObject fileObj = JsonUtils.getAsJsonObject(clientObj, "file");

        return new MinecraftLogging(
                JsonUtils.getAsString(clientObj, "argument"),
                JsonUtils.getAsString(fileObj, "id"),
                JsonUtils.getAsString(fileObj, "sha1"),
                JsonUtils.getAsInt(fileObj, "size"),
                JsonUtils.getAsString(fileObj, "url"),
                JsonUtils.getAsString(clientObj, "type")
        );
    }

    public String getClientArgument() {
        return clientArgument;
    }

    public void setClientArgument(String clientArgument) {
        this.clientArgument = clientArgument;
    }

    public String getClientFileId() {
        return clientFileId;
    }

    public void setClientFileId(String clientFileId) {
        this.clientFileId = clientFileId;
    }

    public String getClientFileSha1() {
        return clientFileSha1;
    }

    public void setClientFileSha1(String clientFileSha1) {
        this.clientFileSha1 = clientFileSha1;
    }

    public int getClientFileSize() {
        return clientFileSize;
    }

    public void setClientFileSize(int clientFileSize) {
        this.clientFileSize = clientFileSize;
    }

    public String getClientFileUrl() {
        return clientFileUrl;
    }

    public void setClientFileUrl(String clientFileUrl) {
        this.clientFileUrl = clientFileUrl;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }
}
