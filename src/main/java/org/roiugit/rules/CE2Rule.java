package org.roiugit.rules;

import org.roiugit.formula.Formula;
import org.roiugit.proof.Proof;

import java.util.List;
import java.util.Objects;

public class CE2Rule extends Rule {

    public CE2Rule() {
        numPremises = 1;
        ruleName = "CE2";
        schema = """
                (A&B)
                -----
                  B \s""";
    }

    @Override
    public Formula applyRule(List<Formula> premises, Proof proof) {
        Formula result = null;
        if (premises.size() == numPremises) {
            Formula conjunction = premises.get(0);
            if (Objects.equals(conjunction.getSign(), "&")) result = conjunction.getConsequent();
        }
        return result;
    }
}
