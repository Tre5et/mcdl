package net.treset.mc_version_loader.minecraft;

import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;
import net.treset.mc_version_loader.os.OsDetails;

import java.util.List;
import java.util.Objects;

public class MinecraftRule {
    private String action;
    private boolean allow;
    private String osName;
    private String osArch;
    private String osVersion;
    private MinecraftLaunchFeature feature;
    private boolean featureState;

    public MinecraftRule(String action, String osName, String osArch, String osVersion, MinecraftLaunchFeature feature, boolean featureState) {
        this.action = action;
        this.allow = Objects.equals(action, "allow");
        this.osName = osName;
        this.osArch = osArch;
        this.osVersion = osVersion;
        this.feature = feature;
        this.featureState = featureState;
    }

    public static MinecraftRule fromJson(JsonObject ruleObj) {
        if(ruleObj == null) {
            return new MinecraftRule(null, null, null, null, null, false);
        }
        JsonObject featuresObj = JsonUtils.getAsJsonObject(ruleObj, "features");
        MinecraftLaunchFeature feature = MinecraftLaunchFeature.NONE;
        boolean featureValue = false;
        if(featuresObj != null) {
            if(featuresObj.has("has_custom_resolution")) {
                feature = MinecraftLaunchFeature.HAS_CUSTOM_RESOLUTION;
                featureValue = JsonUtils.getAsBoolean(featuresObj, "has_custom_resolution");
            } else if(featuresObj.has("is_demo_user")) {
                feature = MinecraftLaunchFeature.IS_DEMO_USER;
                featureValue = JsonUtils.getAsBoolean(featuresObj, "is_demo_user");
            }
        }
        JsonObject osObj = JsonUtils.getAsJsonObject(ruleObj, "os");
        return new MinecraftRule(
                JsonUtils.getAsString(ruleObj, "action"),
                JsonUtils.getAsString(osObj, "name"),
                JsonUtils.getAsString(osObj, "arch"),
                JsonUtils.getAsString(osObj, "version"),
                feature,
                featureValue
        );
    }

    public boolean isApplicable(List<MinecraftLaunchFeature> activeFeatures) {
        if(!isAllow()) {
            return false;
        }

        if(getFeature() != null && getFeature() != MinecraftLaunchFeature.NONE && activeFeatures.contains(getFeature()) != getFeatureState()) {
            return false;
        }

        if(getOsName() != null && !OsDetails.isOsName(getOsName())) {
            return false;
        }
        if(getOsVersion() != null && !OsDetails.isOsVersion(getOsVersion())) {
            return false;
        }
        if(getOsArch() != null && !OsDetails.isOsArch(getOsArch())) {
            return false;
        }
        return true;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isAllow() {
        return allow;
    }

    public void setAllow(boolean allow) {
        this.allow = allow;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsArch() {
        return osArch;
    }

    public void setOsArch(String osArch) {
        this.osArch = osArch;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public MinecraftLaunchFeature getFeature() {
        return feature;
    }

    public void setFeature(MinecraftLaunchFeature feature) {
        this.feature = feature;
    }

    public boolean getFeatureState() {
        return featureState;
    }

    public void setFeatureState(boolean featureState) {
        this.featureState = featureState;
    }
}
