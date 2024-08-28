package net.treset.mc_version_loader.auth.token;

import net.treset.mc_version_loader.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.function.Function;

public class OptionalFileTokenPolicy extends FileTokenPolicy {
    protected boolean active;

    public OptionalFileTokenPolicy(File file, boolean active, Function<IOException, String> onException) {
        super(file, onException);
        this.active = active;
    }

    public OptionalFileTokenPolicy(File file, boolean active) {
        this(file, active, e -> null);
    }

    @Override
    public String injectData() {
        if(active) {
            return super.injectData();
        } else {
            return null;
        }
    }

    @Override
    public void extractData(String data) {
        if(active) {
            super.extractData(data);
        } else if(file.exists()) {
            try {
                FileUtil.delete(file);
            } catch (IOException e) {
                onException.apply(e);
            }
        }
    }
}
