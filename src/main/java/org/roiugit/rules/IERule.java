package org.roiugit.rules;

import org.roiugit.formula.Formula;
import org.roiugit.proof.Proof;

import java.util.List;

public class IERule extends Rule {

    public IERule() {
        numPremises = 2;
        ruleName = "IE";
        schema = """
                (A->B), A
                ---------
                    B   \s""";
    }

    @Override
    public Formula applyRule(List<Formula> premises, Proof proof) {
        Formula result = null;
        if (premises.size() == numPremises) {
            Formula implication = premises.get(0);
            Formula antecedent = premises.get(1);
            if (implication.getSign().equals("->") && implication.getAntecedent().toString().equals(antecedent.toString())) {
                result = implication.getConsequent();
            }
        }
        return result;
    }
}
