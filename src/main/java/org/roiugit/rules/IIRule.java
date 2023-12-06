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
            if ((proof.getActiveSubproof() == null || !proof.getActiveSubproof().isNotClosed()) && proof.getAssumptionDepth() > 0) {
                Formula antecedent = premises.get(0);
                result = new Formula("(%s->%s)".formatted(proof.getFormula(0), antecedent));
                proof.close();
            } else if (proof.getActiveSubproof().isNotClosed()) result = applyRule(premises, proof.getActiveSubproof());
        }
        return result;
    }
}
