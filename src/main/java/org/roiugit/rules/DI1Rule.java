package org.roiugit.rules;

import org.roiugit.formula.Formula;
import org.roiugit.proof.Proof;

import java.util.List;
import java.util.Scanner;

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
        Formula result = null;
        if (numPremises == premises.size()) {
            Formula disjunct1 = premises.get(0);
            System.out.print("Enter the second disjunct: ");
            String disjunct2 = new Scanner(System.in).nextLine();
            result = new Formula("(%sv%s)".formatted(disjunct1, disjunct2));
        }
        return result;
    }
}
