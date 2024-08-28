package net.treset.mc_version_loader.auth.token;

public class DefaultTokenPolicy extends TokenPolicy {
    @Override
    public String injectData() {
        return null;
    }

    @Override
    public void extractData(String data) {}
}
