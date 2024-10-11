package dev.treset.mcdl.resourcepacks;

import java.io.File;
import java.io.IOException;

public class ResoucepackDL {
    /**
     * Reads resourcepack data from a resourcepack directory or zip file.
     * @param file The resourcepack directory or zip file
     * @return The resourcepack data
     * @throws IOException If there is an error reading or parsing the resourcepack
     */
    public static Resourcepack getResourpack(File file) throws IOException {
        return Resourcepack.get(file);
    }

    /**
     * Reads texturepack data from a resourcepack directory or zip file.
     * @param file The texturepack directory or zip file
     * @return The texturepack data
     * @throws IOException If there is an error reading or parsing the texturepack
     */
    public static Texturepack getTexturepack(File file) throws IOException {
        return Texturepack.get(file);
    }
}
