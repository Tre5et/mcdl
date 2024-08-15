package net.treset.mc_version_loader.resoucepacks;

import net.treset.mc_version_loader.util.FileUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Texturepack {
    String name;
    String description;
    BufferedImage image;

    public Texturepack(String name, String description, BufferedImage image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public static Texturepack from(File file) throws IOException {
        if(!file.exists()) {
            throw new IOException("File does not exist: " + file.getAbsolutePath());
        }
        if(file.isDirectory()) {
            File packTxt = new File(file, "pack.txt");
            File packPng = new File(file, "pack.png");
            if(!packTxt.exists()) {
                throw new IOException("pack.txt does not exist in directory: " + file.getAbsolutePath());
            }
            String description = FileUtil.readFileAsString(packTxt);
            BufferedImage image = packPng.exists() ? FileUtil.loadImage(packPng) : null;
            return new Texturepack(file.getName(), description, image);
        }

        if(!file.getName().endsWith(".zip")) {
            throw new IOException("File is not a zip file: " + file.getAbsolutePath());
        }

        try(ZipFile zipFile = new ZipFile(file)) {

            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            String description = null;
            BufferedImage image = null;
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (!entry.isDirectory() && entry.getName().equals("pack.txt")) {
                    InputStream stream = zipFile.getInputStream(entry);
                    description = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
                } else if (!entry.isDirectory() && entry.getName().equals("pack.png")) {
                    InputStream stream = zipFile.getInputStream(entry);
                    try (InputStream is = new ByteArrayInputStream(stream.readAllBytes())) {
                        image = ImageIO.read(is);
                    }
                }
            }
            if (description == null) {
                throw new IOException("pack.txt does not exist");
            }
            return new Texturepack(file.getName().substring(0, file.getName().length() - 4), description, image);
        }
    }
}
