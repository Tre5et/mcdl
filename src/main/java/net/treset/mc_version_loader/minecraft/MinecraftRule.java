package net.treset.mc_version_loader.minecraft;

import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;
import net.treset.mc_version_loader.os.OsDetails;

import java.util.List;
import java.util.Objects;

public class MinecraftRule {
    private String action;
    private Os os;
    private Features features;

    private static class Os {
        private String name;
        private String version;
        private String arch;

        public Os(String name, String version, String arch) {
            this.name = name;
            this.version = version;
            this.arch = arch;
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

    private static class Features {
        private boolean hasCustomResolution;
        private boolean isDemoUser;

        public Features(boolean hasCustomResolution, boolean isDemoUser) {
            this.hasCustomResolution = hasCustomResolution;
            this.isDemoUser = isDemoUser;
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

    public boolean isApplicable(List<String> feature) {
        return feature != null && getFeatures() != null && (feature.contains("has_custom_resolution") && getFeatures().hasCustomResolution || feature.contains("is_demo_user") && getFeatures().isDemoUser);
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
