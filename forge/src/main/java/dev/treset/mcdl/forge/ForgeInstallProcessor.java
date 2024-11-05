package dev.treset.mcdl.forge;

import java.util.List;
import java.util.Map;

@Deprecated
public class ForgeInstallProcessor {
    private List<String> sides;
    private String jar;
    private List<String> classpath;
    private List<String> args;
    private Map<String, String> outputs;

    public ForgeInstallProcessor(List<String> sides, String jar, List<String> classpath, List<String> args, Map<String, String> outputs) {
        this.sides = sides;
        this.jar = jar;
        this.classpath = classpath;
        this.args = args;
        this.outputs = outputs;
    }

    public List<String> getSides() {
        return sides;
    }

    public void setSides(List<String> sides) {
        this.sides = sides;
    }

    public String getJar() {
        return jar;
    }

    public void setJar(String jar) {
        this.jar = jar;
    }

    public List<String> getClasspath() {
        return classpath;
    }

    public void setClasspath(List<String> classpath) {
        this.classpath = classpath;
    }

    public List<String> getArgs() {
        return args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }

    public Map<String, String> getOutputs() {
        return outputs;
    }

    public void setOutputs(Map<String, String> outputs) {
        this.outputs = outputs;
    }
}
