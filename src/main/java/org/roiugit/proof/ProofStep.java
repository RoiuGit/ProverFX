package org.roiugit.proof;

import org.roiugit.formula.Formula;
import org.roiugit.rules.Rule;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProofStep {
    private final Formula formula;
    private final Rule ruleApplied;
    private int index;
    private Map<Integer, Formula> premises = new LinkedHashMap<>();
    private boolean isPremise;
    private boolean isAssumption;

    public ProofStep(int index, List<Integer> premisesIndexes, List<Formula> premises, Formula formula, Rule ruleApplied) {
        this.index = index;
        for (int i = 0; i < premises.size(); i++) {
            this.premises.put(premisesIndexes.get(i), premises.get(i));
        }
        this.formula = formula;
        this.ruleApplied = ruleApplied;
    }

    ProofStep(int index, Formula formula, char mode) {
        this.index = index;
        this.formula = formula;
        if (mode == 'p') isPremise = true;
        if (mode == 'a') isAssumption = true;
        ruleApplied = null;
    }

    public static List<ProofStep> toStepsAsPremises(List<Formula> formulas) {
        List<ProofStep> proofStepList = new ArrayList<>();
        for (int i = 0; i < formulas.size(); i++) {
            proofStepList.add(new ProofStep(i + 1, formulas.get(i), 'p'));
        }
        return proofStepList;
    }

    public String getAnnotation() {
        StringBuilder annotation = new StringBuilder();
        if (!isPremise && !isAssumption) {
            annotation.append("(").append(ruleApplied).append(":");
            String premisesIndexes = premises.keySet().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));
            annotation.append(premisesIndexes);
            annotation.append(")");
        } else if (isPremise) annotation.append("(premise)");
        else annotation.append("(assumption)");
        return annotation.toString();
    }

    public Formula getFormula() {
        return formula;
    }

    public int getIndex() {
        return index;
    }

    public static ProofStep findByIndex(List<ProofStep> steps, Integer index) {
        return steps.stream().filter(step -> step.getIndex() == index).findFirst().orElse(null);
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Map<Integer, Formula> getPremises() {
        return premises;
    }

    public void setPremises(Map<Integer, Formula> premises) {
        this.premises = premises;
    }

    public Rule getRule() {
        return ruleApplied;
    }

    public boolean isPremise() {
        return isPremise;
    }

}
