package net.treset.mc_version_loader.forge;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.GenericJsonParsable;
import net.treset.mc_version_loader.json.JsonUtils;
import net.treset.mc_version_loader.json.SerializationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ForgeMetaVersion extends GenericJsonParsable {
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
}
