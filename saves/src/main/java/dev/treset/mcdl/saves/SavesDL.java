package dev.treset.mcdl.saves;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SavesDL {
    /**
     * Reads save data from a save directory
     * @param file The save directory
     * @return The save data
     * @throws IOException If there is an error reading or parsing the save data
     */
    public static Save getSave(File file) throws IOException {
        return Save.get(file);
    }

    /**
     * Reads server data from a {@code servers.dat} file
     * @param file The {@code servers.dat} file
     * @return A list of servers
     * @throws IOException If there is an error reading or parsing the server data
     */
    public static List<Server> getServers(File file) throws IOException {
        return Server.getAll(file);
    }
}
