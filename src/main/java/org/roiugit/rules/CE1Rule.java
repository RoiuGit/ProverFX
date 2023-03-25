package org.roiugit.rules;

import org.roiugit.formula.Formula;
import org.roiugit.proof.Proof;

import java.util.List;
import java.util.Objects;

public class CE1Rule extends Rule {

    public CE1Rule() {
        numPremises = 1;
        ruleName = "CE1";
        schema = """
                (A&B)
                -----
                  A \s""";
    }

    @Override
    public Formula applyRule(List<Formula> premises, Proof proof) {
        Formula result = null;
        if (premises.size() == numPremises) {
            Formula conjunction = premises.get(0);
            if (Objects.equals(conjunction.getSign(), "&")) result = conjunction.getAntecedent();
        }
        return result;
    }
}
