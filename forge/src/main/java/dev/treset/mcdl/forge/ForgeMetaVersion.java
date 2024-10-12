package dev.treset.mcdl.forge;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.json.GenericJsonParsable;
import dev.treset.mcdl.json.JsonUtils;
import dev.treset.mcdl.json.SerializationException;
import dev.treset.mcdl.util.HttpUtil;
import dev.treset.mcdl.util.cache.Caching;
import dev.treset.mcdl.util.cache.MemoryCaching;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ForgeMetaVersion extends GenericJsonParsable {
    private static Caching<HttpResponse<byte[]>> caching = new MemoryCaching<>();

    private String name;
    private List<String> versions;

    public ForgeMetaVersion(String name, List<String> versions) {
        this.name = name;
        this.versions = versions;
    }

    public static ForgeMetaVersion from(String name, JsonArray versions) throws SerializationException {
        List<String> versionList = new ArrayList<>();
        for (int i = versions.size() - 1; i >= 0; i--) {
            versionList.add(JsonUtils.getAsString(versions.get(i)));
        }
        return new ForgeMetaVersion(name, versionList);
    }

    public static List<ForgeMetaVersion> fromJson(String json) throws SerializationException {
        JsonElement element = JsonUtils.parseJson(json);
        JsonObject obj = JsonUtils.getAsJsonObject(element);

        List<Map.Entry<String, JsonElement>> members = JsonUtils.getMembers(obj).stream().toList();

        ArrayList<ForgeMetaVersion> versions = new ArrayList<>();
        for (int i = members.size() - 1; i >= 0; i--) {
            Map.Entry<String, JsonElement> entry = members.get(i);
            versions.add(ForgeMetaVersion.from(entry.getKey(), JsonUtils.getAsJsonArray(entry.getValue())));
        }

        return versions;
    }

    /**
     * Get all forge versions
     * @return A list of forge versions
     * @throws FileDownloadException If the versions could not be downloaded
     */
    public static List<ForgeMetaVersion> getAll() throws FileDownloadException {
        try {
            String content = HttpUtil.getString(ForgeDL.getMavenMetaUrl(), caching);
            return ForgeMetaVersion.fromJson(content);
        } catch (SerializationException e) {
            throw new FileDownloadException("Failed to parse forge versions", e);
        } catch (IOException e) {
            throw new FileDownloadException("Failed to read forge versions", e);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getVersions() {
        return versions;
    }

    public void setVersions(List<String> versions) {
        this.versions = versions;
    }

    /**
     * Sets the caching strategy for forge versions. Default: {@link MemoryCaching}
     * @param caching The caching strategy to use
     */
    public static void setCaching(Caching<HttpResponse<byte[]>> caching) {
        ForgeMetaVersion.caching = caching;
    }
}
