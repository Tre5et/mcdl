package dev.treset.mcdl.java;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.treset.mcdl.json.JsonUtils;
import dev.treset.mcdl.json.SerializationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JavaRuntimeOs {
    String id;
    List<JavaRuntimeRelease> releases;

    public JavaRuntimeOs(String id, List<JavaRuntimeRelease> releases) {
        this.id = id;
        this.releases = releases;
    }

    public static JavaRuntimeOs fromJsonObject(JsonObject object, String id) throws SerializationException {
        JavaRuntimeOs result = new JavaRuntimeOs(id, null);
        Set<Map.Entry<String, JsonElement>> elements = JsonUtils.getMembers(object);
        List<JavaRuntimeRelease> releases = new ArrayList<>();
        for(Map.Entry<String, JsonElement> e : elements) {
            JsonArray array = JsonUtils.getAsJsonArray(e.getValue());
            if(array != null && !array.isEmpty()) {
                releases.add(JavaRuntimeRelease.fromJsonObject(JsonUtils.getAsJsonObject(array.get(0)), e.getKey()));
            }
        }
        result.setReleases(releases);
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<JavaRuntimeRelease> getReleases() {
        return releases;
    }

    public void setReleases(List<JavaRuntimeRelease> releases) {
        this.releases = releases;
    }
}
