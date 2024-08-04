package net.treset.mc_version_loader.minecraft;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;
import net.treset.mc_version_loader.json.SerializationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MinecraftLaunchArguments {
    private List<MinecraftLaunchArgument> game;
    private List<MinecraftLaunchArgument> jvm;

    public MinecraftLaunchArguments(List<MinecraftLaunchArgument> game, List<MinecraftLaunchArgument> jvm) {
        this.game = game;
        this.jvm = jvm;
    }

    public static MinecraftLaunchArguments fromJson(JsonObject argumentsObj) throws SerializationException {
        JsonArray gameArgumentArray = JsonUtils.getAsJsonArray(argumentsObj, "game");
        JsonArray jvmArgumentArray = JsonUtils.getAsJsonArray(argumentsObj, "jvm");

        List<MinecraftLaunchArgument> gameArguments = MinecraftLaunchArgument.fromJsonArray(gameArgumentArray);
        List<MinecraftLaunchArgument> jvmArguments = MinecraftLaunchArgument.fromJsonArray(jvmArgumentArray);

        return new MinecraftLaunchArguments(gameArguments, jvmArguments);
    }

    public MinecraftLaunchCommand getLaunchCommand(String javaBaseDir, String gameLaunchFile, String mainClass, String libsDir, List<MinecraftLibrary> libraries, List<String> features) {


        StringBuilder outString = new StringBuilder();
        outString.append(javaBaseDir).append("\\bin\\java ");

        List<String> outKeys = new ArrayList<>();

        for(MinecraftLaunchArgument a : getJvm()) {
            if(a.isReplaced() && Objects.equals(a.getReplacementValue(), "classpath")) {
                outString.append(buildClasspathString(gameLaunchFile, mainClass, libsDir, libraries));
            } else {
                addArgument(features, outString, outKeys, a);
            }
        }

        for(MinecraftLaunchArgument a : getGame()) {
            addArgument(features, outString, outKeys, a);
        }

        return new MinecraftLaunchCommand(outString.toString(), outKeys);
    }

    private String buildClasspathString(String gameLaunchFile, String mainClass, String libsDir, List<MinecraftLibrary> libraries) {
        if(libsDir.endsWith("/")) {
            libsDir = libsDir.substring(0, libsDir.length() - 1);
        }
        StringBuilder out = new StringBuilder();
        out.append("\"").append(gameLaunchFile);
        for(MinecraftLibrary l : libraries) {
            if(l.getDownloads().getArtifact().getPath() != null && !l.getDownloads().getArtifact().getPath().isBlank()) {
                out.append(";").append(libsDir).append("/").append(l.getDownloads().getArtifact().getPath());
            }
        }
        out.append("\" ").append(mainClass).append(" ");
        return out.toString();
    }

    private void addArgument(List<String> features, StringBuilder outString, List<String> outKeys, MinecraftLaunchArgument a) {
        if(a.isGated()) {
            boolean shouldBeAdded = true;
            for(MinecraftRule r : a.getRules()) {
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

    public List<MinecraftLaunchArgument> getGame() {
        return game;
    }

    public void setGame(List<MinecraftLaunchArgument> game) {
        this.game = game;
    }

    public List<MinecraftLaunchArgument> getJvm() {
        return jvm;
    }

    public void setJvm(List<MinecraftLaunchArgument> jvm) {
        this.jvm = jvm;
    }
}
