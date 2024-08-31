package net.treset.mcdl.resourcepacks;

import java.io.File;
import java.io.IOException;

public class ResoucepackDL {
    public static Resourcepack getResourpack(File file) throws IOException {
        return Resourcepack.get(file);
    }

    public static Texturepack getTexturepack(File file) throws IOException {
        return Texturepack.get(file);
    }
}
