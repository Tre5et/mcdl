package net.treset.mc_version_loader.launcher;

import net.treset.mc_version_loader.format.FormatUtils;
import net.treset.mc_version_loader.json.GenericJsonParsable;
import net.treset.mc_version_loader.json.SerializationException;

import java.time.LocalDateTime;
import java.util.List;

public class LauncherInstanceDetails extends GenericJsonParsable {
    private List<LauncherFeature> features;
    private List<String> ignoredFiles;
    private List<LauncherLaunchArgument> jvmArguments;
    private String lastPlayed;
    private long totalTime;
    private String modsComponent;
    private String optionsComponent;
    private String resourcepacksComponent;
    private String savesComponent;
    private String versionComponent;

    public LauncherInstanceDetails(List<LauncherFeature> features, List<String> ignoredFiles, List<LauncherLaunchArgument> jvmArguments, String modsComponent, String optionsComponent, String resourcepacksComponent, String savesComponent, String versionComponent) {
        this.features = features;
        this.ignoredFiles = ignoredFiles;
        this.jvmArguments = jvmArguments;
        this.modsComponent = modsComponent;
        this.optionsComponent = optionsComponent;
        this.resourcepacksComponent = resourcepacksComponent;
        this.savesComponent = savesComponent;
        this.versionComponent = versionComponent;
    }

    public static LauncherInstanceDetails fromJson(String json) throws SerializationException {
        return fromJson(json, LauncherInstanceDetails.class);
    }

    public List<LauncherFeature> getFeatures() {
        return features;
    }

    public void setFeatures(List<LauncherFeature> features) {
        this.features = features;
    }

    public List<String> getIgnoredFiles() {
        return ignoredFiles;
    }

    public void setIgnoredFiles(List<String> ignoredFiles) {
        this.ignoredFiles = ignoredFiles;
    }

    public List<LauncherLaunchArgument> getJvmArguments() {
        return jvmArguments;
    }

    public void setJvmArguments(List<LauncherLaunchArgument> jvmArguments) {
        this.jvmArguments = jvmArguments;
    }

    private LocalDateTime getLastPlayedTime() {
        return FormatUtils.parseLocalDateTime(getLastPlayed());
    }
    public String getLastPlayed() {
        return lastPlayed;
    }
    public void setLastPlayedTime(LocalDateTime lastPlayed) {
        setLastPlayed(FormatUtils.formatLocalDateTime(lastPlayed));
    }

    public void setLastPlayed(String lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public String getModsComponent() {
        return modsComponent;
    }

    public void setModsComponent(String modsComponent) {
        this.modsComponent = modsComponent;
    }

    public String getOptionsComponent() {
        return optionsComponent;
    }

    public void setOptionsComponent(String optionsComponent) {
        this.optionsComponent = optionsComponent;
    }

    public String getResourcepacksComponent() {
        return resourcepacksComponent;
    }

    public void setResourcepacksComponent(String resourcepacksComponent) {
        this.resourcepacksComponent = resourcepacksComponent;
    }

    public String getSavesComponent() {
        return savesComponent;
    }

    public void setSavesComponent(String savesComponent) {
        this.savesComponent = savesComponent;
    }

    public String getVersionComponent() {
        return versionComponent;
    }

    public void setVersionComponent(String versionComponent) {
        this.versionComponent = versionComponent;
    }
}
