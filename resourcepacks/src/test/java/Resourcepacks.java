import net.treset.mcdl.resourcepacks.Resourcepack;
import net.treset.mcdl.resourcepacks.Texturepack;

import java.io.File;
import java.io.IOException;

public class Resourcepacks {
    public static void main(String... args) {
        testResourcePack(new File("resourcepacks/testfiles/CCD_RP.zip"));
        testResourcePack(new File("resourcepacks/testfiles/CCD_RP"));
        testResourcePack(new File("resourcepacks/testfiles/VT_RP.zip"));
        testResourcePack(new File("resourcepacks/testfiles/VT_RP"));
        testTexturePack(new File("resourcepacks/testfiles/Sphax_TP.zip"));
        testTexturePack(new File("resourcepacks/testfiles/Sphax_TP"));
        testTexturePack(new File("resourcepacks/testfiles/SC_TP.zip"));
        testTexturePack(new File("resourcepacks/testfiles/SC_TP"));
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
