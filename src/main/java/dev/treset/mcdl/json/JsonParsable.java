package dev.treset.mcdl.json;

import java.io.IOException;

public interface JsonParsable {
    String toJson();
    void writeToFile(String filePath) throws IOException;
}
