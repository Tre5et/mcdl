import net.treset.mc_version_loader.assets.MinecraftAssets;

import java.io.File;
import java.io.IOException;

public class Assets {
    public static void main(String[] args) throws IOException {
        testExtract();
    }

    public static void testExtract() throws IOException {
        MinecraftAssets.extractVirtualAssets("pre-1.6", new File("assets"));
    }
}
