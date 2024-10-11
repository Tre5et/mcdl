package dev.treset.mcdl.forge;

import dev.treset.mcdl.json.GenericJsonParsable;
import dev.treset.mcdl.json.JsonUtils;
import dev.treset.mcdl.json.SerializationException;
import dev.treset.mcdl.minecraft.MinecraftLibrary;

import java.util.List;
import java.util.Map;

public class ForgeInstallProfile extends GenericJsonParsable {
    private boolean hideExtract;
    private int spec;
    private String profile;
    private String version;
    private String path;
    private String minecraft;
    private String serverJarPath;
    private Map<String, ForgeInstallDataPath> data;
    private List<ForgeInstallProcessor> processors;
    private List<MinecraftLibrary> libraries;
    private String icon;
    private String json;
    private String logo;
    private String mirrorList;
    private String welcome;

    public ForgeInstallProfile(boolean hideExtract, int spec, String profile, String version, String path, String minecraft, String serverJarPath, Map<String, ForgeInstallDataPath> data, List<ForgeInstallProcessor> processors, List<MinecraftLibrary> libraries, String icon, String json, String logo, String mirrorList, String welcome) {
        this.hideExtract = hideExtract;
        this.spec = spec;
        this.profile = profile;
        this.version = version;
        this.path = path;
        this.minecraft = minecraft;
        this.serverJarPath = serverJarPath;
        this.data = data;
        this.processors = processors;
        this.libraries = libraries;
        this.icon = icon;
        this.json = json;
        this.logo = logo;
        this.mirrorList = mirrorList;
        this.welcome = welcome;
    }

    public static ForgeInstallProfile fromJson(String json) throws SerializationException {
        return fromJson(json, ForgeInstallProfile.class, JsonUtils.getGsonCamelCase());
    }

    public boolean isHideExtract() {
        return hideExtract;
    }

    public void setHideExtract(boolean hideExtract) {
        this.hideExtract = hideExtract;
    }

    public int getSpec() {
        return spec;
    }

    public void setSpec(int spec) {
        this.spec = spec;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMinecraft() {
        return minecraft;
    }

    public void setMinecraft(String minecraft) {
        this.minecraft = minecraft;
    }

    public String getServerJarPath() {
        return serverJarPath;
    }

    public void setServerJarPath(String serverJarPath) {
        this.serverJarPath = serverJarPath;
    }

    public Map<String, ForgeInstallDataPath> getData() {
        return data;
    }

    public void setData(Map<String, ForgeInstallDataPath> data) {
        this.data = data;
    }

    public List<ForgeInstallProcessor> getProcessors() {
        return processors;
    }

    public void setProcessors(List<ForgeInstallProcessor> processors) {
        this.processors = processors;
    }

    public List<MinecraftLibrary> getLibraries() {
        return libraries;
    }

    public void setLibraries(List<MinecraftLibrary> libraries) {
        this.libraries = libraries;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getMirrorList() {
        return mirrorList;
    }

    public void setMirrorList(String mirrorList) {
        this.mirrorList = mirrorList;
    }

    public String getWelcome() {
        return welcome;
    }

    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }
}
