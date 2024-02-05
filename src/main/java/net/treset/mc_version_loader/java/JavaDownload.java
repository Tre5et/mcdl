package net.treset.mc_version_loader.java;

import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.GenericJsonParsable;
import net.treset.mc_version_loader.json.JsonUtils;
import net.treset.mc_version_loader.json.SerializationException;

public class JavaDownload {
    private String sha1;
    private int size;
    private String url;

    public JavaDownload(String sha1, int size, String url) {
        this.sha1 = sha1;
        this.size = size;
        this.url = url;
    }

    public static JavaDownload fromJsonObject(JsonObject jsonObject) throws SerializationException {
        return JsonUtils.getGson().fromJson(jsonObject, JavaDownload.class);
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
