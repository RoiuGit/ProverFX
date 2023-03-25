package org.roiugit.rules;

import org.roiugit.formula.Formula;
import org.roiugit.proof.Proof;

import java.util.List;

public class DERule extends Rule {

    public DERule() {
        numPremises = 2;
        ruleName = "DE";
        schema = """
                (AvB), ~A
                ---------
                    B   \s""";
    }

    @Override
    public Formula applyRule(List<Formula> premises, Proof proof) {
        Formula result = null;
        if (premises.size() == numPremises) {
            Formula disjunction = premises.get(0);
            Formula disjunct1Negation = premises.get(1);
            if (disjunct1Negation.getAntecedent().equals(disjunction.getAntecedent()))
                result = disjunction.getConsequent();
        }
        return result;
    }
}
