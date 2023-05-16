package net.treset.mc_version_loader.assets;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.GenericJsonParsable;
import net.treset.mc_version_loader.json.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AssetIndex extends GenericJsonParsable {
    private List<AssetObject> objects;

    public AssetIndex(List<AssetObject> objects) {
        this.objects = objects;
    }

    public static AssetIndex fromJson(String json) {
        JsonObject objectsObj = JsonUtils.getAsJsonObject(JsonUtils.getAsJsonObject(JsonUtils.parseJson(json)), "objects");
        Set<Map.Entry<String, JsonElement>> children = JsonUtils.getMembers(objectsObj);
        List<AssetObject> objects = new ArrayList<>();
        for (Map.Entry<String, JsonElement> child : children) {
            objects.add(AssetObject.fromJson(JsonUtils.getAsJsonObject(child.getValue())));
        }
        return new AssetIndex(objects);
    }

    public List<AssetObject> getObjects() {
        return objects;
    }

    public void setObjects(List<AssetObject> objects) {
        this.objects = objects;
    }
}
