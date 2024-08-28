package net.treset.mcdl.auth.token;

public class DefaultTokenPolicy extends TokenPolicy {
    @Override
    public String injectData() {
        return null;
    }

    @Override
    public void extractData(String data) {}
}
