package dev.treset.mcdl.forge;

import dev.treset.mcdl.forge.pre1_13.ForgeInstallerPre1_13;
import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.forge.modern.ForgeInstallerModern;
import dev.treset.mcdl.util.DownloadStatus;
import dev.treset.mcdl.util.FileUtil;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.function.Consumer;

public abstract class ForgeInstaller {
    private final String version;
    private ForgeInstallData installData;

    public ForgeInstaller(String version) {
        this.version = version;
    }

    public static ForgeInstaller getForVersion(String version) throws FileDownloadException {
        String[] parts = version.split("-");
        if(parts.length != 2) {
            throw new FileDownloadException("Invalid forge version: " + version);
        }

        Runtime.Version minecraftVersion = Runtime.Version.parse(parts[0]);
        Runtime.Version forgeVersion = Runtime.Version.parse(parts[1]);


        if(minecraftVersion.compareTo(Runtime.Version.parse("1.13")) >= 0 || (parts[0].equals("1.12.2") && forgeVersion.compareTo(Runtime.Version.parse("14.23.5.2851")) >= 0)) {
            return new ForgeInstallerModern(version);
        } else if(minecraftVersion.compareTo(Runtime.Version.parse("1.5.2")) >= 0) {
            return new ForgeInstallerPre1_13(version);
        } else {
            throw new FileDownloadException("Unsupported forge version: " + version);
        }
    }

    public ForgeInstallData getInstallData() throws FileDownloadException {
        if(installData == null) {
            installData = loadInstallData();
        }
        return installData;
    }

    public void createClient(File librariesDir, File minecraftClient, File javaExecutable, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        createNewClient(getInstallData(), librariesDir, minecraftClient, javaExecutable, onStatus);
    }

    public List<String> downloadLibraries(File librariesDir, File nativesDir, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        return downloadNewLibraries(getInstallData(), librariesDir, nativesDir, onStatus);
    }

    protected abstract ForgeInstallData loadInstallData() throws FileDownloadException;
    protected abstract void createNewClient(ForgeInstallData installData, File librariesDir, File minecraftClient, File javaExecutable, Consumer<DownloadStatus> onStatus) throws FileDownloadException;
    protected abstract List<String> downloadNewLibraries(ForgeInstallData installData, File librariesDir, File nativesDir, Consumer<DownloadStatus> onStatus) throws FileDownloadException;

    protected File downloadInstaller() throws FileDownloadException {
        File jarFile = new File(FileUtil.getTempDir(),"forge-" + getVersion() + "-installer.jar");

        if(!jarFile.exists()) {
            if (!jarFile.getParentFile().exists() && !jarFile.getParentFile().mkdirs()) {
                throw new FileDownloadException("Failed to create directory for forge installer");
            }

            try {
                FileUtil.downloadFile(new URL(ForgeDL.getInstallerUrl(getVersion())), jarFile, ForgeDL.getCaching());
            } catch (MalformedURLException e) {
                throw new FileDownloadException("Failed to parse forge installer Url", e);
            }
        }

        jarFile.deleteOnExit();

        return jarFile;
    }

    public String getVersion() {
        return version;
    }
}
