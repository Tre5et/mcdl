package net.treset.mc_version_loader.minecraft;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import net.treset.mc_version_loader.format.FormatUtils;
import net.treset.mc_version_loader.json.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class MinecraftLaunchArgument {
    private String name;
    private boolean replaced;
    private String replacementValue;
    private boolean gated;
    private List<MinecraftRule> rules;

    public MinecraftLaunchArgument(String name, List<MinecraftRule> rules) {
        this.name = name;
        this.rules = rules;
        this.replaced = FormatUtils.matches(name, "\\$\\{(.*)\\}");
        this.replacementValue = FormatUtils.firstGroup(name, "\\$\\{(.*)\\}");
        this.gated = rules != null && !rules.isEmpty();
    }

    public static List<MinecraftLaunchArgument> fromJsonArray(JsonArray argumentArray) {
        List<MinecraftLaunchArgument> arguments = new ArrayList<>();
        if(argumentArray != null) {
            for (JsonElement e : argumentArray) {
                String ruleString = JsonUtils.getAsString(e);
                if (ruleString != null) {
                    arguments.add(new MinecraftLaunchArgument(ruleString, null));
                } else {
                    JsonObject eObj = JsonUtils.getAsJsonObject(e);
                    JsonArray rules = JsonUtils.getAsJsonArray(eObj, "rules");
                    List<MinecraftRule> currentRules = JsonUtils.getGson().fromJson(rules, new TypeToken<>(){});
                    JsonArray values = JsonUtils.getAsJsonArray(eObj, "value");
                    if (values != null) {
                        for (JsonElement v : values) {
                            arguments.add(new MinecraftLaunchArgument(JsonUtils.getAsString(v), currentRules));
                        }
                    }
                    String value = JsonUtils.getAsString(eObj, "value");
                    if(value != null) {
                        arguments.add(new MinecraftLaunchArgument(value, currentRules));
                    }
                }

            }
        }
        return arguments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isReplaced() {
        return replaced;
    }

    public void setReplaced(boolean replaced) {
        this.replaced = replaced;
    }

    public String getReplacementValue() {
        return replacementValue;
    }

    public void setReplacementValue(String replacementValue) {
        this.replacementValue = replacementValue;
    }

    public boolean isGated() {
        return gated;
    }

    public void setGated(boolean gated) {
        this.gated = gated;
    }

    public List<MinecraftRule> getRules() {
        return rules;
    }

    public void setRules(List<MinecraftRule> rules) {
        this.rules = rules;
    }
}
