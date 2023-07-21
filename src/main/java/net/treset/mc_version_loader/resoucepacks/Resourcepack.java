package net.treset.mc_version_loader.resoucepacks;

import net.treset.mc_version_loader.util.FileUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

    public static Resourcepack from(File file) throws IOException {
        if(!file.exists()) {
            return null;
        }
        if(file.isDirectory()) {
            File packMcmetaFile = new File(file, "pack.mcmeta");
            File packPngFile = new File(file, "pack.png");
            if(!packMcmetaFile.exists()) {
                return null;
            }
            PackMcmeta mcmeta = PackMcmeta.fromJson(Files.readString(packMcmetaFile.toPath()));
            if(mcmeta == null) {
                return null;
            }
            return new Resourcepack(
                    file.getName(),
                    mcmeta,
                    packPngFile.exists() ? FileUtil.loadImage(packPngFile) : null
            );
        }
        if(!file.getName().endsWith(".zip")) {
            return null;
        }

        ZipFile zipFile = new ZipFile(file);

        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        PackMcmeta mcmeta = null;
        BufferedImage image = null;
        while(entries.hasMoreElements()){
            ZipEntry entry = entries.nextElement();
            if(!entry.isDirectory() && entry.getName().equals("pack.mcmeta")) {
                InputStream stream = zipFile.getInputStream(entry);
                mcmeta = PackMcmeta.fromJson(new String(stream.readAllBytes()));
            } else if(!entry.isDirectory() && entry.getName().equals("pack.png")) {
                InputStream stream = zipFile.getInputStream(entry);
                try (InputStream is = new ByteArrayInputStream(stream.readAllBytes()))
                {
                    image = ImageIO.read(is);
                }
            }
        }
        if(mcmeta == null) {
            return null;
        }
        return new Resourcepack(
                file.getName().substring(0, file.getName().length() - 4),
                mcmeta,
                image
        );
    }
}
