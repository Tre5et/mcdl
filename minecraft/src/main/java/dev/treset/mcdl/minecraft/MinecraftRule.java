package dev.treset.mcdl.minecraft;

import dev.treset.mcdl.util.OsUtil;

import java.util.List;
import java.util.Map;

public class MinecraftRule {
    private String action;
    private Os os;
    private Map<String, Boolean> features;

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

    public MinecraftRule(String action, Os os, Map<String, Boolean> features) {
        this.action = action;
        this.os = os;
        this.features = features;
    }

    public boolean isAllowFeatures(List<String> features) {
        if (this.features == null) {
            return true;
        }
        return this.features.entrySet().stream()
                .allMatch(entry -> entry.getValue() == features.contains(entry.getKey()));
    }

    public boolean isApplicable(List<String> features) {
        if(getAction() == null) {
            return true;
        }
        if(getAction().equals("allow")) {
            return (getOs() == null || getOs().isAllow()) && isAllowFeatures(features);
        } else if(action.equals("disallow")) {
            return (getOs() == null || !getOs().isAllow()) && (features == null || !isAllowFeatures(features));
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

    public Map<String, Boolean> getFeatures() {
        return features;
    }

    public void setFeatures(Map<String, Boolean> features) {
        this.features = features;
    }
}
