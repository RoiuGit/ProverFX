package org.roiugit.proof;

// Import necessary \classes

import org.roiugit.formula.Formula;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// The Proof class represents a logical proof with premises, steps, and subproofs
public class Proof {
    // Fields to store the depth of assumptions, indices, and lists for subproofs, proof steps, and premises
    private final int assumptionDepth;
    private final List<Proof> subProofList = new ArrayList<>();
    private final List<ProofStep> proofSteps = new ArrayList<>();
    private Formula target;
    private boolean isClosed;

    // Default constructor initializing a new Proof with default values
    public Proof() {
        isClosed = false;
        assumptionDepth = 0;
    }

    // Constructor initializing a new Proof with given premises
    public Proof(List<Formula> premises) {
        this.proofSteps.addAll(ProofStep.toStepsAsPremises(premises));
        this.assumptionDepth = 0;
        isClosed = false;
    }

    // Constructor for a subproof with specified depth and starting index
    Proof(int assumptionDepth) {
        this.assumptionDepth = assumptionDepth;
        isClosed = false;
    }

    // Method to append a proof step to the proof
    public void append(ProofStep proofStep) {
        proofSteps.add(proofStep);
        if (getActiveSubproof() != null && getActiveSubproof().isNotClosed()) {
            getActiveSubproof().append(proofStep);
        }
    }

    // Method to assume a new formula as part of the proof
    public void assume(Formula assumption) {
        addSubProof();
        append(new ProofStep(getEndingIndex() + 1, assumption, 'a'));
    }

    // Method to retrieve a formula at a specific index
    public Formula getFormula(int index) {
        return proofSteps.get(index).getFormula();
    }

    // Method to get the depth of assumptions for a specific line
    public int getLineAssumptionDepth(int lineNumber) {
        int depth = 0;
        for (Proof sp : subProofList) {
            if (lineNumber >= sp.getStartingIndex() && lineNumber <= sp.getEndingIndex()) {
                depth++;
                depth += sp.getLineAssumptionDepth(lineNumber);
            }
        }
        return depth;
    }

    // Method to check if a line belongs to a closed subproof
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

    // Various getter methods for proof properties
    public List<ProofStep> getProofSteps() {
        return proofSteps;
    }

    public int getAssumptionDepth() {
        return assumptionDepth;
    }

    public Proof getActiveSubproof() {
        if (!subProofList.isEmpty())
            return subProofList.get(subProofList.size() - 1);
        else return null;
    }

    public int getStartingIndex() {
        if (proofSteps.isEmpty()) return 0;
        else return proofSteps.get(0).getIndex();
    }

    public int getEndingIndex() {
        if (proofSteps.isEmpty()) return 0;
        else return proofSteps.get(proofSteps.size() - 1).getIndex();
    }

    public List<Formula> getPremises() {
        return proofSteps.stream().filter(ProofStep::isPremise).map(ProofStep::getFormula).toList();
    }

    public Formula getTarget() {
        return target;
    }

    public void setTarget(Formula target) {
        this.target = target;
    }

    // Method to close the current proof
    public void close() {
        isClosed = true;
    }

    // Method to check if the proof is not closed
    public boolean isNotClosed() {
        return !isClosed;
    }

    // Method to add a subproof to the current proof
    public void addSubProof() {
        if (getActiveSubproof() != null && getActiveSubproof().isNotClosed()) getActiveSubproof().addSubProof();
        else {
            Proof subProof = new Proof(assumptionDepth + 1);
            subProofList.add(subProof);
        }
    }

    public void removeProofStep(int lineNumber) {
        proofSteps.remove(ProofStep.findByIndex(proofSteps, lineNumber));
        for (Proof sp : subProofList)
            if (lineNumber >= sp.getStartingIndex() && lineNumber <= sp.getEndingIndex()) {
                sp.removeProofStep(lineNumber);
            }
    }

    public void minimizeProof() {
        Set<Integer> necessaryStepIndexes = new HashSet<>();
        ProofStep minTargetStep = proofSteps.get(proofSteps.size() - 1);
        Formula minTargetFormula = minTargetStep.getFormula();
        Integer minTargetIndex = proofSteps.stream().filter(step -> step.getFormula().equals(minTargetFormula) && getLineAssumptionDepth(step.getIndex()) == 0).findFirst().orElseThrow().getIndex();
        markForMinimization(minTargetIndex - 1, necessaryStepIndexes);

        // Iterate from the end to avoid index shifting issues
        for (int i = getEndingIndex(); i >= getStartingIndex(); i--) {
            if (!necessaryStepIndexes.contains(i)) {
                removeProofStep(i);
            }
        }

        // Update startingIndex and endingIndex if necessary
        updateIndexes();
    }

    private void updateIndexes() {
        // Assuming proofSteps are always sequentially indexed
        if (!proofSteps.isEmpty()) {

            Map<Integer, Integer> indexMapping = new HashMap<>();
            for (int i = 0; i < proofSteps.size(); i++) {
                int oldIndex = proofSteps.get(i).getIndex();
                int newIndex = i + 1;
                updateIndex(oldIndex, newIndex);
                indexMapping.put(oldIndex, newIndex);
            }

            for (ProofStep step : proofSteps) {
                Map<Integer, Formula> updatedPremises = new LinkedHashMap<>();
                for (Map.Entry<Integer, Formula> entry : step.getPremises().entrySet()) {
                    Integer oldPremiseIndex = entry.getKey();
                    Integer newPremiseIndex = indexMapping.get(oldPremiseIndex);
                    if (newPremiseIndex != null) {
                        updatedPremises.put(newPremiseIndex, entry.getValue());
                    }
                }
                step.setPremises(updatedPremises);
            }
            removeEmpty();
        }
    }

    private void removeEmpty() {
        subProofList.forEach(Proof::removeEmpty);
        subProofList.removeIf(sp -> sp.getProofSteps().isEmpty());
    }

    private void updateIndex(Integer oldIndex, Integer newIndex) {
        ProofStep step = ProofStep.findByIndex(proofSteps, oldIndex);
        if (step != null) step.setIndex(newIndex);
        for (Proof sp : subProofList)
            if (oldIndex >= sp.getStartingIndex() && oldIndex <= sp.getEndingIndex()) {
                sp.updateIndex(oldIndex, newIndex);
            }
    }

    private void markForMinimization(Integer index, Set<Integer> necessarySteps) {
        ProofStep step = proofSteps.get(index);
        necessarySteps.add(step.getIndex());
        step.getPremises().forEach((ind, premise) -> markForMinimization(ind - 1, necessarySteps));
        if (step.getRule() != null && (step.getRule().getRuleName().equals("NI") || step.getRule().getRuleName().equals("II"))) {
            //Finds the closest ending index of the subproof, i.e. the one which the rule was applied to
            Optional<Integer> subProofPremise = findDeepestSubProofStartingIndexBefore(step.getIndex());
            necessarySteps.add(subProofPremise.orElseThrow());
            markForMinimization(subProofPremise.orElseThrow() - 1, necessarySteps);
        }
    }

    private Optional<Integer> findDeepestSubProofStartingIndexBefore(int index) {
        return subProofList.stream()
                .flatMap(sp -> Stream.concat(Stream.of(sp), sp.getAllNestedSubProofs().stream()))
                .filter(sp -> sp.getAssumptionDepth() == getLineAssumptionDepth(index) + 1)
                .map(Proof::getStartingIndex)
                .filter(ind -> ind < index)
                .max(Integer::compareTo);
    }

    private List<Proof> getAllNestedSubProofs() {
        List<Proof> allSubProofs = new ArrayList<>(subProofList);
        for (Proof subProof : subProofList) {
            allSubProofs.addAll(subProof.getAllNestedSubProofs());
        }
        return allSubProofs;
    }

    @Override
    public String toString() {
        // StringBuilder is used for efficient string concatenation
        StringBuilder proof = new StringBuilder();

        // Determine the number format based on the length of the ending index
        int numberFormat = Integer.toString(getEndingIndex()).length() + 1;

        // Find the maximum length of the formulas in the proof steps
        Optional<Integer> maxFormulaLength = proofSteps.stream().map(ProofStep::getFormula).map(Formula::toString).map(String::length).max(Integer::compareTo);

        // Find the maximum depth of assumptions in the proof
        Optional<Integer> maxDepth = IntStream.rangeClosed(getStartingIndex(), getEndingIndex()).boxed().toList().stream().map(this::getLineAssumptionDepth).max(Integer::compareTo);

        // Calculate the format width for the formula column
        int formulaFormat = numberFormat + maxFormulaLength.orElse(10) + maxDepth.orElse(0) + 1;

        // Define the format string for printing each line of the proof
        String format = "%-" + formulaFormat + "s %s%n";

        // Iterate through each proof step
        for (ProofStep proofStep : proofSteps) {
            // Calculate the depth of assumptions for the current step
            int depth = getLineAssumptionDepth(proofStep.getIndex());

            // Format the line number
            String lineNumber = "%d.".formatted(proofStep.getIndex());

            // Create a string of depth bars ('|') indicating the level of nested assumptions
            String depthBars = "|".repeat(Math.max(0, depth));

            // Get the formula as a string
            String formula = proofStep.getFormula().toString();

            // Format the formula column with line number, depth bars, and formula
            String formulaColumn = "%s%s %s".formatted(lineNumber, depthBars, formula);

            // Get the annotation for the proof step
            String annotation = proofStep.getAnnotation();

            // Append the formatted line to the proof string
            proof.append(format.formatted(formulaColumn, annotation));
        }

        // Return the entire proof as a string
        return proof.toString();
    }

}
