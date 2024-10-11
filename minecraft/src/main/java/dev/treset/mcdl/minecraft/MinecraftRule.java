package dev.treset.mcdl.minecraft;

import dev.treset.mcdl.util.OsUtil;

import java.util.List;

public class MinecraftRule {
    private String action;
    private Os os;
    private Features features;

    public static class Os {
        private String name;
        private String version;
        private String arch;

        public Os(String name, String version, String arch) {
            this.name = name;
            this.version = version;
            this.arch = arch;
        }

        public boolean isAllow() {
            return (name == null || OsUtil.isOsName(name)) && (version == null || OsUtil.isOsVersion(version)) && (arch == null || OsUtil.isOsArch(arch));
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getArch() {
            return arch;
        }

        public void setArch(String arch) {
            this.arch = arch;
        }
    }

    public static class Features {
        private boolean hasCustomResolution;
        private boolean isDemoUser;

        public Features(boolean hasCustomResolution, boolean isDemoUser) {
            this.hasCustomResolution = hasCustomResolution;
            this.isDemoUser = isDemoUser;
        }

        public boolean isAllow(List<String> features) {
            return (!isHasCustomResolution() || features.contains("has_custom_resolution")) && (isDemoUser() || features.contains("is_demo_user"));
        }

        public boolean isHasCustomResolution() {
            return hasCustomResolution;
        }

        public void setHasCustomResolution(boolean hasCustomResolution) {
            this.hasCustomResolution = hasCustomResolution;
        }

        public boolean isDemoUser() {
            return isDemoUser;
        }

        public void setDemoUser(boolean demoUser) {
            isDemoUser = demoUser;
        }
    }

    public MinecraftRule(String action, Os os, Features features) {
        this.action = action;
        this.os = os;
        this.features = features;
    }

    public boolean isApplicable(List<String> features) {
        if(getAction() == null) {
            return true;
        }
        if(getAction().equals("allow")) {
            return (getOs() == null || getOs().isAllow()) && (getFeatures() == null || getFeatures().isAllow(features));
        } else if(action.equals("disallow")) {
            return (getOs() == null || !getOs().isAllow()) && (getFeatures() == null || !getFeatures().isAllow(features));
        }
        return true;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Os getOs() {
        return os;
    }

    public void setOs(Os os) {
        this.os = os;
    }

    public Features getFeatures() {
        return features;
    }

    public void setFeatures(Features features) {
        this.features = features;
    }
}
