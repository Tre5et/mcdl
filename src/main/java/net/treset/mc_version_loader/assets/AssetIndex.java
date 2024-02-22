package net.treset.mc_version_loader.assets;

import net.treset.mc_version_loader.json.GenericJsonParsable;
import net.treset.mc_version_loader.json.SerializationException;

import java.util.Map;

public class AssetIndex extends GenericJsonParsable {
    private Map<String, AssetObject> objects;

    public AssetIndex(Map<String, AssetObject> objects) {
        this.objects = objects;
    }

    public static AssetIndex fromJson(String json) throws SerializationException {
        return fromJson(json, AssetIndex.class);

        /*JsonObject objectsObj = JsonUtils.getAsJsonObject(JsonUtils.getAsJsonObject(JsonUtils.parseJson(json)), "objects");
        Set<Map.Entry<String, JsonElement>> children = JsonUtils.getMembers(objectsObj);
        List<AssetObject> objects = new ArrayList<>();
        for (Map.Entry<String, JsonElement> child : children) {
            objects.add(AssetObject.fromJson(JsonUtils.getAsJsonObject(child.getValue())));
        }
        return new AssetIndex(objects);*/
    }

    public Map<String, AssetObject> getObjects() {
        return objects;
    }

    public void setObjects(Map<String, AssetObject> objects) {
        this.objects = objects;
    }
}
