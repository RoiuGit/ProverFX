package org.roiugit;

import org.roiugit.deduction.NaturalDeduction;
import org.roiugit.formula.Formula;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class Prover {
    private final NaturalDeduction nd = new NaturalDeduction();

    public void applyRule(String rule, List<Integer> indexes) {
        nd.applyRule(rule, indexes);
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

    public void setMainProof() {
        nd.setMainProof();
    }

    public void endProof() {
        nd.close();
    }

    public String getResult() {
        StringBuilder result = new StringBuilder();
        Formula resultFormula = nd.getResult();
        String premises = nd.getPremises().stream().map(Formula::toString).collect(Collectors.joining(", "));
        result.append(premises);
        result.append(" => ");
        result.append(resultFormula.toString());
        return result.toString();
    }

    public String getTarget() {
        Formula target = nd.getTarget();
        if (target != null) return nd.getTarget().toString();
        else return null;
    }

    public void setTarget(String target) {
        nd.setTarget(target);
    }

    public boolean targetReached() {
        if (nd.getTarget() != null && nd.getResult() != null) return Objects.equals(nd.getResult(), nd.getTarget());
        else return false;
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

    public boolean isNotClosed() {
        return nd.isNotClosed();
    }

    public boolean isEmpty() {
        return nd.isEmpty();
    }

    public void applyRule(String rule, List<Integer> indexes, String expression) {
        nd.applyRule(rule, indexes, expression);
    }
}