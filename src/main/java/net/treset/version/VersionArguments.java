package net.treset.version;

import java.util.ArrayList;
import java.util.List;

public class VersionArguments {
    private List<VersionArgument> game;
    private List<VersionArgument> jvm;

    public VersionArguments(List<VersionArgument> game, List<VersionArgument> jvm) {
        this.game = game;
        this.jvm = jvm;
    }

    public VersionLaunchCommand getLaunchCommand(String javaCommand, String gameLaunchFile, List<VersionFeature> features) {
        StringBuilder outString = new StringBuilder();
        outString.append(javaCommand).append(" ");

        List<String> outKeys = new ArrayList<>();

        for(VersionArgument a : getJvm()) {
            addArgument(features, outString, outKeys, a);
        }

        outString.append("-jar ").append(gameLaunchFile).append(" ");

        for(VersionArgument a : getGame()) {
            addArgument(features, outString, outKeys, a);
        }

        return new VersionLaunchCommand(outString.toString(), outKeys);
    }

    private void addArgument(List<VersionFeature> features, StringBuilder outString, List<String> outKeys, VersionArgument a) {
        if(a.isGated()) {
            boolean shouldBeAdded = true;
            for(VersionRule r : a.getRules()) {
                if(!r.isCorrect(features)) {
                    shouldBeAdded = false;
                    break;
                }
            }
            if(!shouldBeAdded) {
                return;
            }
        }
        if(a.isReplaced()) {
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
