package net.treset.mc_version_loader.minecraft;

import net.treset.mc_version_loader.format.FormatUtils;

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
