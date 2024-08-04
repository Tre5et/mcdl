import net.treset.mc_version_loader.minecraft.MinecraftGame;
import net.treset.mc_version_loader.minecraft.MinecraftVersion;
import net.treset.mc_version_loader.minecraft.MinecraftVersionDetails;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class Version {
    public static void main(String[] args) {
        testVersions();
    }

    public static void testVersions() {
        testVersion("1.0");
        testVersion("1.2.1");
        testVersion("1.5.1");
        testVersion("1.7.10");
        testVersion("1.12.2");
        testVersion("1.16.5");
        testVersion("1.21");
    }

    public static void testVersion(String name) {
        try {
            List<MinecraftVersion> versions = MinecraftGame.getVersions();
            MinecraftVersion version = versions.stream().filter(v -> v.getId().equals(name)).findFirst().get();
            MinecraftVersionDetails details = MinecraftGame.getVersionDetails(version.getUrl());
            MinecraftGame.downloadVersionDownload(details.getDownloads().getClient(), new File("download/client-"+name+".jar"));
            Files.createDirectories(new File("download/libraries-"+name).toPath());
            MinecraftGame.downloadVersionLibraries(details.getLibraries(), new File("download/libraries-"+name), List.of(), new File("download/natives-"+name), (v) -> {});
            System.out.println("Version:testVersion/" + name + " PASSED");
        } catch (Exception e) {
            System.out.println("Version:testVersion/" + name + " FAILED: ");
            e.printStackTrace();
        }
    }
}
