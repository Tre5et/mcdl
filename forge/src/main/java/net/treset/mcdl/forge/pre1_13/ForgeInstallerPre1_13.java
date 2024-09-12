package net.treset.mcdl.forge.pre1_13;

import net.treset.mcdl.exception.FileDownloadException;
import net.treset.mcdl.forge.ForgeInstallData;
import net.treset.mcdl.forge.ForgeInstaller;
import net.treset.mcdl.json.SerializationException;
import net.treset.mcdl.minecraft.MinecraftLibrary;
import net.treset.mcdl.util.DownloadStatus;
import net.treset.mcdl.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.function.Consumer;

public class ForgeInstallerPre1_13 extends ForgeInstaller {

    public ForgeInstallerPre1_13(String version) {
        super(version);
    }

    @Override
    protected ForgeInstallData loadInstallData() throws FileDownloadException {
        File installer = downloadInstaller();
        File extracted = getExtractedDir();
        try {
            FileUtil.exctractFile(installer, extracted);
        } catch (IOException e) {
            throw new FileDownloadException("Failed to extract forge installer", e);
        }

        try {
            String content = FileUtil.readFileAsString(new File(extracted, "install_profile.json"));
            ForgeInstallProfilePre1_13 installProfile = ForgeInstallProfilePre1_13.fromJson(content);

            try {
                String fileName = installProfile.getInstall().getFilePath();
                File jarFile = new File(getExtractedDir(), fileName);
                File targetFile = new File(getExtractedDir(), "maven/" + FileUtil.toMavenPath(installProfile.getInstall().getPath(), ".jar"));
                if(!targetFile.getParentFile().exists() && !targetFile.getParentFile().mkdirs()) {
                    throw new FileDownloadException("Failed to create forge client directory");
                }
                Files.copy(jarFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new FileDownloadException("Failed to copy forge jar", e);
            }

            String mirrorUrl = installProfile.getMirrorUrl();
            return installProfile.toInstallData(mirrorUrl);
        } catch (IOException | SerializationException e) {
            throw new FileDownloadException("Failed to read forge install profile", e);
        }
    }

    @Override
    protected void createNewClient(ForgeInstallData installData, File librariesDir, File minecraftClient, File javaExecutable, Consumer<DownloadStatus> onStatus) {
        // Nothing to do :)
    }

    @Override
    protected List<String> downloadNewLibraries(ForgeInstallData installData, File librariesDir, File nativesDir, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        File mavenDir = new File(getExtractedDir(), "maven");
        return MinecraftLibrary.downloadAll(installData.getLibraries(), librariesDir, mavenDir, List.of(), nativesDir, onStatus);
    }

    private File getExtractedDir()  {
        return new File(FileUtil.getTempDir(), "forge-" + getVersion() + "-installer");
    }
}
