package net.treset.mcdl.auth.token;

import net.treset.mcdl.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.function.Function;

public class FileTokenPolicy extends TokenPolicy {
    protected File file;
    protected Function<IOException, String> onException;

    public FileTokenPolicy(File file, Function<IOException, String> onException) {
        this.file = file;
        this.onException = onException;
    }

    public FileTokenPolicy(File file) {
        this(file, e -> null);
    }

    @Override
    public String injectData() {
        try {
            return FileUtil.readFileAsString(file);
        } catch (IOException e) {
            return onException.apply(e);
        }
    }

    @Override
    public void extractData(String data) {
        try {
            FileUtil.writeToFile(data.getBytes(), file);
        } catch (IOException e) {
            onException.apply(e);
        }
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Function<IOException, String> getOnException() {
        return onException;
    }

    public void setOnException(Function<IOException, String> onException) {
        this.onException = onException;
    }
}
