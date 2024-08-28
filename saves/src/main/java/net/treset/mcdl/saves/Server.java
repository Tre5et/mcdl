package net.treset.mcdl.saves;

import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Server {
    private BufferedImage image;
    private String ip;
    private String name;
    private boolean hidden;

    public Server(BufferedImage image, String ip, String name, boolean hidden) {
        this.image = image;
        this.ip = ip;
        this.name = name;
        this.hidden = hidden;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    /**
     * Reads a list of servers from a {@code servers.dat} file.
     * @param file The file to read from
     * @return A list of servers
     * @throws IOException If there is an error reading or parsing the file
     */
    public static List<Server> from(File file) throws IOException {
        if(!file.exists()) {
            throw new IOException("File does not exist");
        }

        NamedTag serversDat = NBTUtil.read(file);
        if(!(serversDat.getTag() instanceof CompoundTag levelDatCompound)) {
            throw new IOException("level.dat is not a compound tag");
        }

        ListTag<CompoundTag> servers = levelDatCompound.getListTag("servers").asCompoundTagList();


        ArrayList<Server> serverList = new ArrayList<>();
        for(CompoundTag serverCompound : servers ) {
            String icon64 = serverCompound.getString("icon");
            byte[] imgBytes = Base64.getDecoder().decode(icon64);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imgBytes));

            String ip = serverCompound.getString("ip");
            String name = serverCompound.getString("name");
            byte hidden = serverCompound.getByte("hidden");

            serverList.add(
                new Server(
                    image,
                    ip,
                    name,
                    hidden > 0
                )
            );
        }

        return serverList;
    }
}
