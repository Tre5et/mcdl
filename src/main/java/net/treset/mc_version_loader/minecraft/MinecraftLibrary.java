package net.treset.mc_version_loader.minecraft;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class MinecraftLibrary {
    private String name;
    private String artifactPath;
    private String artifactSha1;
    private int artifactSize;
    private String artifactUrl;
    private List<MinecraftRule> rules;

    public MinecraftLibrary(String name, String artifactPath, String artifactSha1, int artifactSize, String artifactUrl, List<MinecraftRule> rules) {
        this.name = name;
        this.artifactPath = artifactPath;
        this.artifactSha1 = artifactSha1;
        this.artifactSize = artifactSize;
        this.artifactUrl = artifactUrl;
        this.rules = rules;
    }

    public static MinecraftLibrary fromJson(JsonObject libraryObj) {
        JsonObject artifactObj = JsonUtils.getAsJsonObject(JsonUtils.getAsJsonObject(libraryObj, "downloads"), "artifact");
        JsonArray rulesArray = JsonUtils.getAsJsonArray(libraryObj, "rules");
        List<MinecraftRule> rules = new ArrayList<>();
        if(rulesArray != null) {
            for(JsonElement e: rulesArray) {
                rules.add(MinecraftRule.fromJson(JsonUtils.getAsJsonObject(e)));
            }
        }

        return new MinecraftLibrary(
                JsonUtils.getAsString(libraryObj, "name"),
                JsonUtils.getAsString(artifactObj, "path"),
                JsonUtils.getAsString(artifactObj, "sha1"),
                JsonUtils.getAsInt(artifactObj, "size"),
                JsonUtils.getAsString(artifactObj, "url"),
                rules
        );
    }

    public static List<MinecraftLibrary> parseLibraries(JsonArray librariesArray) {
        List<MinecraftLibrary> libraries = new ArrayList<>();
        if(librariesArray != null) {
            for(JsonElement l : librariesArray) {
                libraries.add(fromJson(JsonUtils.getAsJsonObject(l)));
            }
        }
        return libraries;
    }

    public boolean isApplicable(List<MinecraftLaunchFeature> activeFeatures) {
        for(MinecraftRule r : getRules()) {
            if(!r.isApplicable(activeFeatures)) {
                return false;
            }
        }
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtifactPath() {
        return artifactPath;
    }

    public void setArtifactPath(String artifactPath) {
        this.artifactPath = artifactPath;
    }

    public String getArtifactSha1() {
        return artifactSha1;
    }

    public void setArtifactSha1(String artifactSha1) {
        this.artifactSha1 = artifactSha1;
    }

    public int getArtifactSize() {
        return artifactSize;
    }

    public void setArtifactSize(int artifactSize) {
        this.artifactSize = artifactSize;
    }

    public String getArtifactUrl() {
        return artifactUrl;
    }

    public void setArtifactUrl(String artifactUrl) {
        this.artifactUrl = artifactUrl;
    }

    public List<MinecraftRule> getRules() {
        return rules;
    }

    public void setRules(List<MinecraftRule> rules) {
        this.rules = rules;
    }
}
