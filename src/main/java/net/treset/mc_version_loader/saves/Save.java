package net.treset.mc_version_loader.saves;

import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;
import net.treset.mc_version_loader.util.FileUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Save {
    private String name;
    private String fileName;
    private BufferedImage image;

    public Save(String name, String fileName, BufferedImage image) {
        this.name = name;
        this.fileName = fileName;
        this.image = image;
    }

    public static Save from(File file) throws IOException {
        if(!file.exists() || !file.isDirectory()) {
            throw new IOException("File does not exist or is not a directory");
        }
        File levelDatFile = new File(file, "level.dat");
        if(!levelDatFile.exists()) {
            throw new IOException("level.dat does not exist");
        }

        NamedTag levelDat = NBTUtil.read(levelDatFile);
        if(!(levelDat.getTag() instanceof CompoundTag levelDatCompound)) {
            throw new IOException("level.dat is not a compound tag");
        }
        CompoundTag data = levelDatCompound.getCompoundTag("Data");
        String levelName = data.getString("LevelName");

        File imageFile = new File(file, "icon.png");
        BufferedImage image = null;
        if(imageFile.exists()) {
            image = FileUtil.loadImage(imageFile);
        }
        return new Save(
                levelName,
                file.getName(),
                image
        );
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
