package net.treset.version;

import java.util.List;

public class VersionLaunchCommand {
    private String launchCommand;
    private List<String> replacementKeys;

    public VersionLaunchCommand(String launchCommand, List<String> replacementKeys) {
        this.launchCommand = launchCommand;
        this.replacementKeys = replacementKeys;
    }

    public String getFinalCommand(Object... replacementValues) {
        return String.format(getLaunchCommand(), replacementValues);
    }

    public String getLaunchCommand() {
        return launchCommand;
    }

    public void setLaunchCommand(String launchCommand) {
        this.launchCommand = launchCommand;
    }

    public List<String> getReplacementKeys() {
        return replacementKeys;
    }

    public void setReplacementKeys(List<String> replacementKeys) {
        this.replacementKeys = replacementKeys;
    }
}
