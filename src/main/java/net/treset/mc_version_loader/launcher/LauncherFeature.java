package net.treset.mc_version_loader.launcher;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LauncherFeature {
    private String feature;
    private String value;

    public LauncherFeature(String feature, String value) {
        this.feature = feature;
        this.value = value;
    }

    public static List<LauncherFeature> parseFeatures(JsonObject featuresObj) {
        Set<Map.Entry<String, JsonElement>> features = JsonUtils.getMembers(featuresObj);
        if(features == null) {
            return null;
        }
        List<LauncherFeature> out = new ArrayList<>();
        for(Map.Entry<String, JsonElement> e : features) {
            if(JsonUtils.getAsString(e.getValue()) != null) {
                out.add(new LauncherFeature(e.getKey(), JsonUtils.getAsString(e.getValue())));
            }
        }
        return out;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
