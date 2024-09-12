package net.treset.mcdl.forge.pre1_5_2;

import net.treset.mcdl.exception.FileDownloadException;
import net.treset.mcdl.forge.ForgeInstallData;
import net.treset.mcdl.forge.ForgeInstaller;
import net.treset.mcdl.util.DownloadStatus;
import net.treset.mcdl.util.FileUtil;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

public class ForgeInstallerPre1_5_2 extends ForgeInstaller {
    public ForgeInstallerPre1_5_2(String version) {
        super(version);
    }

    @Override
    protected ForgeInstallData loadInstallData() throws FileDownloadException {
        throw new FileDownloadException("Unsupported forge version: " + getVersion());
    }

    @Override
    protected void createNewClient(ForgeInstallData installData, File librariesDir, File minecraftClient, File javaExecutable, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        throw new FileDownloadException("Unsupported forge version: " + getVersion());
    }

    @Override
    protected List<String> downloadNewLibraries(ForgeInstallData installData, File librariesDir, File nativesDir, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        throw new FileDownloadException("Unsupported forge version: " + getVersion());
    }

    private File getExtractedDir()  {
        return new File(FileUtil.getTempDir(), "forge-" + getVersion() + "-jar");
    }
}
