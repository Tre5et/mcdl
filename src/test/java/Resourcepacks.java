import net.treset.mc_version_loader.resoucepacks.Resourcepack;
import net.treset.mc_version_loader.resoucepacks.Texturepack;

import java.io.File;
import java.io.IOException;

public class Resourcepacks {
    public static void main(String... args) {
        testResourcePack(new File("testfiles/CCD_RP.zip"));
        testResourcePack(new File("testfiles/CCD_RP"));
        testResourcePack(new File("testfiles/VT_RP.zip"));
        testResourcePack(new File("testfiles/VT_RP"));
        testTexturePack(new File("testfiles/Sphax_TP.zip"));
        testTexturePack(new File("testfiles/Sphax_TP"));
        testTexturePack(new File("testfiles/SC_TP.zip"));
        testTexturePack(new File("testfiles/SC_TP"));
    }

    public static void testResourcePack(File file) {
        try {
            Resourcepack pack = Resourcepack.from(file);
            System.out.println("Resourcepacks:testResourcePack PASSED: " + pack.getName());
        } catch (IOException e) {
            System.out.println("Resourcepacks:testResourcePack FAILED: " + file.getName());
            e.printStackTrace();
        }
    }

    public static void testTexturePack(File file) {
        try {
            Texturepack pack = Texturepack.from(file);
            System.out.println("Resourcepacks:testTexturePack PASSED: " + pack.getName());
        } catch (IOException e) {
            System.out.println("Resourcepacks:testTexturePack FAILED: " + file.getName());
            e.printStackTrace();
        }
    }
}
