package org.roiugit.proof;

import org.junit.Test;
import org.roiugit.formula.Formula;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ProofTest {

    @Test
    public void testAppend() {
        Proof proof = new Proof();
        Formula formula = new Formula("P");
        proof.append(new ProofStep(proof.getEndingIndex() + 1, formula, 'p'));
        assertEquals("The last formula should be a premise \"P\"", formula, proof.getProofSteps().get(proof.getProofSteps().size() - 1).getFormula());
    }

    @Test
    public void testAssume() {
        Proof proof = new Proof();
        Formula formula = new Formula("P");
        proof.assume(formula);
        assertEquals("The last formula should be an assumption \"P\"", formula, proof.getProofSteps().get(proof.getProofSteps().size() - 1).getFormula());
    }

    @Test
    public void testAssumeEmpty() {

        Proof proof = new Proof();
        proof.assume(null);

    }

    @Test
    public void testGetFormulaWithValidIndex() {
        // Set up a scenario where the method should work
        Proof proof = new Proof();
        Formula formula = new Formula("P");
        proof.assume(formula);

        // Test for a valid index
        assertEquals("Should return the correct formula", formula, proof.getFormula(0));
    }

    @Test
    public void testGetFormulaWithInvalidIndex() {
        // Set up a scenario with an empty or minimal proof
        Proof proof = new Proof();

        // Test for an invalid index - expecting an exception
        assertThrows(IndexOutOfBoundsException.class, () -> {
            proof.getFormula(0); // Assuming this is an invalid index in this scenario
        });
    }

    @Test
    public void getLineAssumptionDepth() {
    }

    @Test
    public void belongsToClosedProof() {
    }

    @Test
    public void getProofSteps() {
    }

    @Test
    public void getAssumptionDepth() {
    }

    @Test
    public void getSubProof() {
    }

    @Test
    public void getStartingIndex() {
    }

    @Test
    public void getEndingIndex() {
    }

    @Test
    public void getPremises() {
    }

    @Test
    public void getTarget() {
    }

    @Test
    public void setTarget() {
    }

    @Test
    public void close() {
    }

    @Test
    public void isNotClosed() {
    }

    @Test
    public void addSubProof() {
    }

    @Test
    public void removeProofStep() {
    }

    @Test
    public void minimizeProof() {
    }

    @Test
    public void testToString() {
    }
}