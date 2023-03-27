package org.roiugit.proof;

import org.roiugit.formula.Formula;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Proof {
    private final List<Proof> subProofList = new ArrayList<>();
    private final int assumptionDepth;
    private final int startingIndex;
    private final List<ProofStep> proofSteps = new ArrayList<>();
    private final List<Formula> premises = new ArrayList<>();
    private Proof subProof;
    private int endingIndex;
    private boolean isClosed;

    public Proof() {
        isClosed = false;
        assumptionDepth = 0;
        startingIndex = 1;
        endingIndex = 0;
    }

    public Proof(List<Formula> premises) {
        this.premises.addAll(premises);
        this.proofSteps.addAll(ProofStep.toSteps(premises));
        this.startingIndex = 1;
        endingIndex = premises.size();
        this.assumptionDepth = 0;
        isClosed = false;
    }

    Proof(int assumptionDepth, int startingIndex) {
        this.startingIndex = startingIndex;
        endingIndex = startingIndex - 1;
        this.assumptionDepth = assumptionDepth;
        isClosed = false;
    }

    public void append(ProofStep proofStep) {
        proofSteps.add(proofStep);
        endingIndex++;
        if (subProof != null && subProof.isNotClosed()) {
            subProof.append(proofStep);
        }
    }

    public Formula getFormula(int index) {
        return proofSteps.get(index).getFormula();
    }

    public void assume(Formula assumption) {
        addSubProof();
        append(new ProofStep(endingIndex + 1, assumption, 'a'));
    }

    public String getResult() {
        if (subProof != null && subProof.isNotClosed()) return null;
        StringBuilder result = new StringBuilder();
        String premisesStr = premises.stream()
                .map(Formula::toString)
                .collect(Collectors.joining(", "));
        result.append(premisesStr);
        result.append(" => ");
        result.append(proofSteps.get(proofSteps.size() - 1).getFormula());
        return result.toString();
    }

    public int getLineAssumptionDepth(int lineNumber) {
        int depth = 0;
        for (Proof sp : subProofList) {
            if (lineNumber >= sp.startingIndex && lineNumber <= sp.endingIndex) {
                depth++;
                depth += sp.getLineAssumptionDepth(lineNumber);
            }
        }
        return depth;
    }

    public int getAssumptionDepth() {
        return assumptionDepth;
    }

    public boolean belongsToClosedProof(int lineNumber) {
        for (Proof subProof : subProofList) {
            if (lineNumber >= subProof.getStartingIndex() && lineNumber <= subProof.getEndingIndex()) {
                if (subProof.isClosed) {
                    return true;
                } else {
                    return subProof.belongsToClosedProof(lineNumber);
                }
            }
        }
        return false;
    }

    public Proof getSubProof() {
        return subProof;
    }

    public void addSubProof() {
        if (subProof != null && subProof.isNotClosed()) subProof.addSubProof();
        else {
            subProof = new Proof(assumptionDepth + 1, endingIndex + 1);
            subProofList.add(subProof);
        }
    }

    public void close() {
        isClosed = true;
    }

    public boolean isNotClosed() {
        return !isClosed;
    }

    public int getStartingIndex() {
        return startingIndex;
    }

    public int getEndingIndex() {
        return endingIndex;
    }

    public String toString() {
        StringBuilder proof = new StringBuilder();
        int numberFormat = Integer.toString(getEndingIndex()).length() + 1;
        Optional<Integer> maxFormulaLength = proofSteps.stream().map(ProofStep::getFormula).map(Formula::toString).map(String::length).max(Integer::compareTo);
        Optional<Integer> maxDepth = IntStream.rangeClosed(startingIndex, endingIndex).boxed().toList().stream().map(this::getLineAssumptionDepth).max(Integer::compareTo);
        int formulaFormat = numberFormat + maxFormulaLength.orElse(10) + maxDepth.orElse(0) + 1;
        String format = "%-" + formulaFormat + "s %s%n";
        for (ProofStep proofStep : proofSteps) {
            int depth = getLineAssumptionDepth(proofStep.getIndex());
            String lineNumber = "%d.".formatted(proofStep.getIndex());
            String depthBars = "|".repeat(Math.max(0, depth));
            String formula = proofStep.getFormula().toString();
            String formulaColumn = "%s%s %s".formatted(lineNumber, depthBars, formula);
            String annotation = proofStep.getAnnotation();
            proof.append(format.formatted(formulaColumn, annotation));
        }

        return proof.toString();
    }
}
