package net.treset.mc_version_loader.json;

import java.io.IOException;

public interface JsonParsable {
    void writeToFile(String filePath) throws IOException;
}
