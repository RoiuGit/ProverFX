package org.roiugit.rules;

import org.roiugit.formula.Formula;
import org.roiugit.proof.Proof;

import java.util.List;
import java.util.Scanner;

public class DI2Rule extends Rule {
    public DI2Rule() {
        numPremises = 1;
        ruleName = "DI2";
        schema = """
                  A \s
                -----
                (BvA)""";
    }

    @Override
    public Formula applyRule(List<Formula> premises, Proof proof) {
        Formula result = null;
        if (numPremises == premises.size()) {
            Formula disjunct2 = premises.get(0);
            System.out.print("Enter the first disjunct: ");
            String disjunct1 = new Scanner(System.in).nextLine();
            result = new Formula("(%sv%s)".formatted(disjunct1, disjunct2));
        }
        return result;
    }

    @Override
    public Formula applyRule(List<Formula> premises, Proof proof, String disjunct1) {
        Formula result = null;
        if (numPremises == premises.size()) {
            Formula disjunct2 = premises.get(0);
            result = new Formula("(%sv%s)".formatted(disjunct1, disjunct2));
        }
        return result;
    }
}
