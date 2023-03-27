package org.roiugit.rules;

import org.roiugit.formula.Formula;
import org.roiugit.proof.Proof;

import java.util.List;

public abstract class Rule {
    protected String ruleName;
    protected String schema;
    protected int numPremises;

    public abstract Formula applyRule(List<Formula> premises, Proof proof);

    public Formula applyRule(List<Formula> premises, String expression) {
        throw new UnsupportedOperationException("This rule does not support user input");
    }

    public int getNumPremises() {
        return numPremises;
    }

    public String getRuleName() {
        return ruleName;
    }

    public String getSchema() {
        return schema;
    }

    @Override
    public String toString() {
        return ruleName;
    }
}