import net.treset.mc_version_loader.assets.MinecraftAssets;

import java.io.File;
import java.io.IOException;

public class Assets {
    public static void main(String... args) {
        testExtract("pre-1.6");
        testExtract("1.16");
        testExtract("pre-1.6");
    }

    public static void testExtract(String version) {
        try {
            MinecraftAssets.createVirtualAssets(version, new File("assets"));
            System.out.println("Assets:testExtract/"+version+" PASSED");
        } catch (IOException e) {
            System.out.println("Assets:testExtract/"+version+" FAILED: ");
            e.printStackTrace();
        }
    }
}
