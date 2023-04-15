package net.treset.version;

import java.util.List;

public class VersionArgument {
    private String name;
    private boolean replaced;
    private String replacementValue;
    private boolean gated;
    private List<VersionRule> rules;

    public VersionArgument(String name, List<VersionRule> rules) {
        this.name = name;
        this.rules = rules;
        this.replaced = name.startsWith("${") && name.endsWith("}");
        this.replacementValue = replaced ? name.substring(2, name.length() - 1) : null;
        this.gated = rules == null || rules.isEmpty();
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

    public List<VersionRule> getRules() {
        return rules;
    }

    public void setRules(List<VersionRule> rules) {
        this.rules = rules;
    }
}
