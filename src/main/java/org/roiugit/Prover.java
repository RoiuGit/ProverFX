package org.roiugit;

import org.roiugit.formula.Formula;
import org.roiugit.naturaldeduction.NaturalDeduction;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;


public class Prover {
    private final NaturalDeduction nd = new NaturalDeduction();
    private String path = "C:\\Users\\puzik\\Documents\\myProofs\\";

    public void applyRule(String rule, List<Integer> indexes) {
        nd.applyRule(rule, indexes);
    }

    public void makeProofDir() {
        //noinspection ResultOfMethodCallIgnored
        new File(path).mkdirs();
    }

    public String saveProof(File file) {
        nd.toFile(file);
        return "Saved proof as %s to %s".formatted(file.getName(), file.getPath());
    }

    public void assume(String expression) {
        nd.assume(new Formula(expression));
    }

    public List<RuleInfo> getRuleInfo() {
        return nd.getRuleList().stream()
                .map(rule -> new RuleInfo(rule.getRuleName(), rule.getSchema(), rule.getNumPremises()))
                .collect(Collectors.toList());
    }

    public String getMainProof() {
        if (nd.getMainProof() != null) return nd.getMainProof().toString();
        else return null;
    }

    public void setMainProof(List<String> expressions) {
        nd.setMainProof(expressions.stream().map(Formula::new).collect(Collectors.toList()));
    }
    public void setMainProof(){
        nd.setMainProof();
    }

    public String endProof() {
        String result = nd.getResult();
        nd.close();
        return result;
    }

    public int getStartingIndex() {
        return nd.getStartingIndex();
    }

    public int getEndingIndex() {
        return nd.getEndingIndex();
    }

    public void loadProof(File file) {
        nd.fromFile(file);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isNotClosed() {
        return nd.isNotClosed();
    }
}