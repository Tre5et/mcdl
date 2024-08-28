package net.treset.mcdl.assets;

import net.treset.mcdl.json.GenericJsonParsable;
import net.treset.mcdl.json.SerializationException;

import java.util.Map;

public class AssetIndex extends GenericJsonParsable {
    private boolean mapToResources;
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

    public boolean isMapToResources() {
        return mapToResources;
    }

    public void setMapToResources(boolean mapToResources) {
        this.mapToResources = mapToResources;
    }
}
