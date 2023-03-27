package org.roiugit.rules;

import org.roiugit.formula.Formula;
import org.roiugit.proof.Proof;

import java.util.List;

public class DI1Rule extends Rule {
    public DI1Rule() {
        numPremises = 1;
        ruleName = "DI1";
        schema = """
                  A \s
                -----
                (AvB)""";
    }

    @Override
    public Formula applyRule(List<Formula> premises, Proof proof) {
        return null;
    }

    @Override
    public Formula applyRule(List<Formula> premises, String disjunct2) {
        Formula result = null;
        if (numPremises == premises.size()) {
            Formula disjunct1 = premises.get(0);
            result = new Formula("(%sv%s)".formatted(disjunct1, disjunct2));
        }
        return result;
    }
}
