package net.treset.mc_version_loader.version;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VersionArguments {
    private List<VersionArgument> game;
    private List<VersionArgument> jvm;

    public VersionArguments(List<VersionArgument> game, List<VersionArgument> jvm) {
        this.game = game;
        this.jvm = jvm;
    }

    public VersionLaunchCommand getLaunchCommand(String javaBaseDir, String gameLaunchFile, String mainClass, String libsDir, List<VersionLibrary> libraries, List<VersionFeature> features) {


        StringBuilder outString = new StringBuilder();
        outString.append(javaBaseDir).append("\\bin\\java ");

        List<String> outKeys = new ArrayList<>();

        for(VersionArgument a : getJvm()) {
            if(a.isReplaced() && Objects.equals(a.getReplacementValue(), "classpath")) {
                outString.append(buildClasspathString(gameLaunchFile, mainClass, libsDir, libraries));
            } else {
                addArgument(features, outString, outKeys, a);
            }
        }

        for(VersionArgument a : getGame()) {
            addArgument(features, outString, outKeys, a);
        }

        return new VersionLaunchCommand(outString.toString(), outKeys);
    }

    private String buildClasspathString(String gameLaunchFile, String mainClass, String libsDir, List<VersionLibrary> libraries) {
        if(libsDir.endsWith("/")) {
            libsDir = libsDir.substring(0, libsDir.length() - 1);
        }
        StringBuilder out = new StringBuilder();
        out.append("\"").append(gameLaunchFile);
        for(VersionLibrary l : libraries) {
            if(l.getArtifactPath() != null && !l.getArtifactPath().isBlank()) {
                out.append(";").append(libsDir).append("/").append(l.getArtifactPath());
            }
        }
        out.append("\" ").append(mainClass).append(" ");
        return out.toString();
    }

    private void addArgument(List<VersionFeature> features, StringBuilder outString, List<String> outKeys, VersionArgument a) {
        if(a.isGated()) {
            boolean shouldBeAdded = true;
            for(VersionRule r : a.getRules()) {
                if(!r.isApplicable(features)) {
                    shouldBeAdded = false;
                    break;
                }
            }
            if(!shouldBeAdded) {
                return;
            }
        }
        if(a.isReplaced()) {
            outString.append(a.getName(), 0, Math.max(a.getName().indexOf(a.getReplacementValue()) - 2, 0)).append("%s").append(a.getName(), Math.min(a.getName().indexOf(a.getReplacementValue()) + a.getReplacementValue().length() + 2, a.getName().length()), a.getName().length()).append(" ");
            outKeys.add(a.getReplacementValue());
        } else {
            outString.append(a.getName()).append(" ");
        }
    }

    public List<VersionArgument> getGame() {
        return game;
    }

    public void setGame(List<VersionArgument> game) {
        this.game = game;
    }

    public List<VersionArgument> getJvm() {
        return jvm;
    }

    public void setJvm(List<VersionArgument> jvm) {
        this.jvm = jvm;
    }
}
