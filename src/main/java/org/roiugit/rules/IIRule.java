package org.roiugit.rules;

import org.roiugit.formula.Formula;
import org.roiugit.proof.Proof;

import java.util.List;

public class IIRule extends Rule {

    public IIRule() {
        numPremises = 1;
        ruleName = "II";
        schema = """
                   B  \s
                -------
                 (C->B), where C is the last unclosed assumption.""";
    }

    @Override
    public Formula applyRule(List<Formula> premises, Proof proof) {
        Formula result = null;
        if (premises.size() == numPremises) {
            if (proof.getSubProof() == null && proof.getAssumptionDepth() > 0 || !proof.getSubProof().isNotClosed()) {
                Formula antecedent = premises.get(0);
                result = new Formula("(%s->%s)".formatted(proof.getFormula(0), antecedent));
                proof.close();
            } else result = applyRule(premises, proof.getSubProof());
        }
        return result;
    }
}
