package net.treset.mcdl.resourcepacks;

import net.treset.mcdl.json.SerializationException;
import net.treset.mcdl.util.FileUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Resourcepack {
    private String name;
    private PackMcmeta packMcmeta;
    private BufferedImage image;

    public Resourcepack(String name, PackMcmeta packMcmeta, BufferedImage image) {
        this.name = name;
        this.packMcmeta = packMcmeta;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PackMcmeta getPackMcmeta() {
        return packMcmeta;
    }

    public void setPackMcmeta(PackMcmeta packMcmeta) {
        this.packMcmeta = packMcmeta;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    /**
     * Reads resourcepack data from a resourcepack directory or zip file.
     * @param file The resourcepack directory or zip file
     * @return The resourcepack data
     * @throws IOException If there is an error reading or parsing the resourcepack
     */
    public static Resourcepack from(File file) throws IOException {
        if(!file.exists()) {
            throw new IOException("File does not exist: " + file.getAbsolutePath());
        }
        if(file.isDirectory()) {
            File packMcmetaFile = new File(file, "pack.mcmeta");
            File packPngFile = new File(file, "pack.png");
            if(!packMcmetaFile.exists()) {
                throw new IOException("pack.mcmeta does not exist");
            }
            PackMcmeta mcmeta;
            try {
                mcmeta = PackMcmeta.fromJson(Files.readString(packMcmetaFile.toPath()));
            } catch (SerializationException e) {
                throw new IOException("pack.mcmeta is not valid json", e);
            }
            if(mcmeta == null) {
                throw new IOException("pack.mcmeta is not valid json");
            }
            return new Resourcepack(
                    file.getName(),
                    mcmeta,
                    packPngFile.exists() ? FileUtil.loadImage(packPngFile) : null
            );
        }
        if(!file.getName().endsWith(".zip")) {
            throw new IOException("File is not a zip file");
        }

        try(ZipFile zipFile = new ZipFile(file)) {

            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            PackMcmeta mcmeta = null;
            BufferedImage image = null;
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (!entry.isDirectory() && entry.getName().equals("pack.mcmeta")) {
                    InputStream stream = zipFile.getInputStream(entry);
                    String content = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
                    try {
                        mcmeta = PackMcmeta.fromJson(content);
                    } catch (SerializationException e) {
                        throw new IOException("pack.mcmeta is not valid json: " + content, e);
                    }
                } else if (!entry.isDirectory() && entry.getName().equals("pack.png")) {
                    InputStream stream = zipFile.getInputStream(entry);
                    try (InputStream is = new ByteArrayInputStream(stream.readAllBytes())) {
                        image = ImageIO.read(is);
                    }
                }
            }
            if (mcmeta == null) {
                throw new IOException("pack.mcmeta does not exist");
            }
            return new Resourcepack(
                    file.getName().substring(0, file.getName().length() - 4),
                    mcmeta,
                    image
            );
        }
    }
}
