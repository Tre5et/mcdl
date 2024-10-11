package dev.treset.mcdl.forge.pre1_13;

import dev.treset.mcdl.forge.ForgeInstallData;
import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.json.GenericJsonParsable;
import dev.treset.mcdl.json.JsonUtils;
import dev.treset.mcdl.json.SerializationException;
import dev.treset.mcdl.minecraft.MinecraftLaunchArgument;
import dev.treset.mcdl.minecraft.MinecraftLaunchArguments;
import dev.treset.mcdl.minecraft.MinecraftLibrary;
import dev.treset.mcdl.util.HttpUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ForgeInstallProfilePre1_13 {
    private VersionInfo versionInfo;
    private Install install;

    public static class VersionInfo extends GenericJsonParsable {
        private String minecraftArguments;
        private List<ForgeLibrary> libraries;
        private String mainClass;
        private int minimumLauncherVersion;
        private String time;
        private String id;
        private String type;
        private String processArguments;
        private String releaseTime;

        public VersionInfo(String minecraftArguments, List<ForgeLibrary> libraries, String mainClass, int minimumLauncherVersion, String time, String id, String type, String processArguments, String releaseTime) {
            this.minecraftArguments = minecraftArguments;
            this.libraries = libraries;
            this.mainClass = mainClass;
            this.minimumLauncherVersion = minimumLauncherVersion;
            this.time = time;
            this.id = id;
            this.type = type;
            this.processArguments = processArguments;
            this.releaseTime = releaseTime;
        }

        public String getMinecraftArguments() {
            return minecraftArguments;
        }

        public void setMinecraftArguments(String minecraftArguments) {
            this.minecraftArguments = minecraftArguments;
        }

        public List<ForgeLibrary> getLibraries() {
            return libraries;
        }

        public void setLibraries(List<ForgeLibrary> libraries) {
            this.libraries = libraries;
        }

        public String getMainClass() {
            return mainClass;
        }

        public void setMainClass(String mainClass) {
            this.mainClass = mainClass;
        }

        public int getMinimumLauncherVersion() {
            return minimumLauncherVersion;
        }

        public void setMinimumLauncherVersion(int minimumLauncherVersion) {
            this.minimumLauncherVersion = minimumLauncherVersion;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getProcessArguments() {
            return processArguments;
        }

        public void setProcessArguments(String processArguments) {
            this.processArguments = processArguments;
        }

        public String getReleaseTime() {
            return releaseTime;
        }

        public void setReleaseTime(String releaseTime) {
            this.releaseTime = releaseTime;
        }
    }

    public static class Install {
        private String mirrorList;
        private String target;
        private String filePath;
        private String logo;
        private String welcome;
        private String version;
        private String path;
        private String profileName;
        private String minecraft;

        public Install(String mirrorList, String target, String filePath, String logo, String welcome, String version, String path, String profileName, String minecraft) {
            this.mirrorList = mirrorList;
            this.target = target;
            this.filePath = filePath;
            this.logo = logo;
            this.welcome = welcome;
            this.version = version;
            this.path = path;
            this.profileName = profileName;
            this.minecraft = minecraft;
        }

        public String getMirrorList() {
            return mirrorList;
        }

        public void setMirrorList(String mirrorList) {
            this.mirrorList = mirrorList;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getWelcome() {
            return welcome;
        }

        public void setWelcome(String welcome) {
            this.welcome = welcome;
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

        public String getProfileName() {
            return profileName;
        }

        public void setProfileName(String profileName) {
            this.profileName = profileName;
        }

        public String getMinecraft() {
            return minecraft;
        }

        public void setMinecraft(String minecraft) {
            this.minecraft = minecraft;
        }
    }

    public ForgeInstallProfilePre1_13(VersionInfo versionInfo, Install install) {
        this.versionInfo = versionInfo;
        this.install = install;
    }

    public static ForgeInstallProfilePre1_13 fromJson(String json) throws SerializationException {
        return GenericJsonParsable.fromJson(json, ForgeInstallProfilePre1_13.class, JsonUtils.getGsonCamelCase());
    }

    public ForgeInstallData toInstallData(String mirrorUrl) {
        List<MinecraftLibrary> mcLibs = versionInfo.libraries.stream().map((l) -> {
            boolean localJar = Objects.equals(l.getName(), getInstall().path);
            return l.toMinecraftLibrary(mirrorUrl, localJar);
        }).toList();
        MinecraftLaunchArguments arguments = new MinecraftLaunchArguments(
                Arrays.stream(versionInfo.minecraftArguments.split(" ")).map((n) -> new MinecraftLaunchArgument(n, null)).toList(),
                List.of()
        );

        return new ForgeInstallData(
                versionInfo.id,
                install.minecraft,
                versionInfo.type,
                versionInfo.mainClass,
                mcLibs,
                arguments
        );
    }

    public String getMirrorUrl() throws FileDownloadException {
        String file;
        try {
            file = HttpUtil.getString(getInstall().getMirrorList());
        } catch (IOException e) {
            throw new FileDownloadException("Failed to download mirror list", e);
        }
        List<String> mirrorStrings = file.lines().toList();
        if(mirrorStrings.isEmpty()) {
            throw new FileDownloadException("Failed to get mirror list");
        }
        String mirrorLine = mirrorStrings.stream().filter((s) -> !s.toLowerCase().startsWith("forge")).findFirst().orElse(mirrorStrings.get(0));
        MirrorData mirror;
        try {
            mirror = MirrorData.from(mirrorLine);
        } catch (IOException e) {
            throw new FileDownloadException("Failed to parse mirror data", e);
        }
        return mirror.getMaven();
    }

    public VersionInfo getVersionInfo() {
        return versionInfo;
    }

    public void setVersionInfo(VersionInfo versionInfo) {
        this.versionInfo = versionInfo;
    }

    public Install getInstall() {
        return install;
    }

    public void setInstall(Install install) {
        this.install = install;
    }
}
