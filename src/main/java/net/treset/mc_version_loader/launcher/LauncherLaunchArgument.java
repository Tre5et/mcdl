package net.treset.mc_version_loader.launcher;

import net.treset.mc_version_loader.format.FormatUtils;
import net.treset.mc_version_loader.os.OsDetails;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LauncherLaunchArgument {
    private String argument;
    private String feature;
    private String osName;
    private String osVersion;
    private String parsedArgument;
    private List<String> replacementValues;

    public LauncherLaunchArgument(String argument, String feature, String osName, String osVersion) {
        this.argument = argument;
        this.feature = feature;
        this.osName = osName;
        this.osVersion = osVersion;
        replacementValues = FormatUtils.findMatches(argument, "\\$\\{([a-zA-z_\\-\\d]*)\\}");
        parsedArgument = argument;
    }

    public boolean replace(Map<String, String> replacements) {
        boolean allReplaced = true;
        for(Map.Entry<String, String> e : replacements.entrySet()) {
            if(getReplacementValues().contains(e.getKey())) {
                int index = Math.max(0, getParsedArgument().indexOf(e.getKey()));
                String firstStringPart = getParsedArgument().substring(0, Math.max(0, index - 2));
                String tempLastPart = getParsedArgument().substring(index);
                String lastStringPart = tempLastPart.substring(Math.max(0, Math.min(tempLastPart.length(), tempLastPart.indexOf('}') + 1)));
                parsedArgument = firstStringPart + e.getValue() + lastStringPart;
                replacementValues.remove(e.getKey());
            } else {
                allReplaced = false;
            }
        }
        return allReplaced;
    }

    public boolean isActive(List<LauncherFeature> features) {
        if(getFeature() != null && !getFeature().isBlank() && features != null) {
            boolean enabled = false;
            for (LauncherFeature f : features) {
                if (Objects.equals(f.getFeature(), feature)) {
                    enabled = true;
                    break;
                }
            }
            if (!enabled) {
                return false;
            }
        }
        if(getOsName() != null && !getOsName().isBlank()) {
            if(!OsDetails.isOsName(getOsName())) {
                return false;
            }
        }
        if(getOsVersion() != null && !getOsVersion().isBlank()) {
            if(!OsDetails.isOsVersion(getOsVersion())) {
                return false;
            }
        }
        return true;
    }

    public boolean isFinished() {
        return getReplacementValues() == null || getReplacementValues().isEmpty();
    }

    public String getParsedArgument() {
        if(parsedArgument == null) {
            parsedArgument = argument;
        }
        return parsedArgument;
    }

    public void setParsedArgument(String parsedArgument) {
        this.parsedArgument = parsedArgument;
    }

    public List<String> getReplacementValues() {
        if(replacementValues == null) {
            replacementValues = FormatUtils.findMatches(argument, "\\$\\{([a-zA-z_\\-\\d]*)\\}");
        }
        return replacementValues;
    }

    public void setReplacementValues(List<String> replacementValues) {
        this.replacementValues = replacementValues;
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }
}
