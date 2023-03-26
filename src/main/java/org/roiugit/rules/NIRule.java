package org.roiugit.rules;

import org.roiugit.formula.Formula;
import org.roiugit.proof.Proof;

import java.util.List;
import java.util.Objects;

public class NIRule extends Rule {

    public NIRule() {
        numPremises = 2;
        ruleName = "NI";
        schema = """
                A, ~A
                -----
                  C  , where C is the last unclosed assumption.""";
    }

    @Override
    public Formula applyRule(List<Formula> premises, Proof proof) {
        Formula result = null;
        if (premises.size() == numPremises) {
            Formula affirmation = premises.get(0);
            Formula negation = premises.get(1);
            if (!Objects.equals(affirmation.getSign(), "~") && Objects.equals(negation.getSign(), "~") && affirmation.equals(negation.getAntecedent())) {
                if ((proof.getSubProof() == null || !proof.getSubProof().isNotClosed()) && proof.getAssumptionDepth() > 0) {
                    result = new Formula("~" + proof.getFormula(0));
                    proof.close();
                } else if (proof.getSubProof().isNotClosed()) result = applyRule(premises, proof.getSubProof());
            }
        }
        return result;
    }
}
