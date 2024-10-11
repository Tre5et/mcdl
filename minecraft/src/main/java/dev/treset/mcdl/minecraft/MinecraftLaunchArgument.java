package dev.treset.mcdl.minecraft;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import dev.treset.mcdl.format.FormatUtils;
import dev.treset.mcdl.json.JsonUtils;
import dev.treset.mcdl.json.SerializationException;

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
        if(replaced) {
            this.replacementValue = FormatUtils.firstGroup(name, "\\$\\{(.*)\\}");
        }
        this.gated = rules != null && !rules.isEmpty();
    }

    public static List<MinecraftLaunchArgument> fromJsonArray(JsonArray argumentArray) throws SerializationException {
        List<MinecraftLaunchArgument> arguments = new ArrayList<>();
        if(argumentArray != null) {
            for (JsonElement e : argumentArray) {
                try {
                    String ruleString = JsonUtils.getAsString(e);
                    if(ruleString != null) {
                        arguments.add(new MinecraftLaunchArgument(ruleString, null));
                    }
                } catch (SerializationException ex1) {
                    JsonObject eObj = JsonUtils.getAsJsonObject(e);
                    JsonArray rules = JsonUtils.getAsJsonArray(eObj, "rules");
                    List<MinecraftRule> currentRules = JsonUtils.getGson().fromJson(rules, new TypeToken<>(){});
                    try {
                        JsonArray values = JsonUtils.getAsJsonArray(eObj, "value");
                        if (values != null) {
                            for (JsonElement v : values) {
                                arguments.add(new MinecraftLaunchArgument(JsonUtils.getAsString(v), currentRules));
                            }
                        }
                    } catch (SerializationException ex2) {
                        try {
                            String value = JsonUtils.getAsString(eObj, "value");
                            if(value != null) {
                                arguments.add(new MinecraftLaunchArgument(value, currentRules));
                            }
                        } catch (SerializationException ex3) {
                            throw new SerializationException("No valid json format for launch argument: " + eObj);
                        }
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
