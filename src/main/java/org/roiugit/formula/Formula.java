package org.roiugit.formula;

import java.util.Objects;

public class Formula {
    private final String expression;
    private String sign;
    private Formula antecedent;
    private Formula consequent;
    private boolean isAtomicFormula;

    public Formula(String expression) {
        this.expression = normalize(expression);
        evaluate();
    }

    private static String normalize(String expression) {
        expression = expression.toUpperCase()
                .replaceAll("\\s+", "")
                .replace('V', 'v');
        expression = removeExcessParentheses(expression);
        return expression;
    }

    private static String removeExcessParentheses(String expression) {
        /* TODO: implement */
        return expression;
    }

    private boolean isValid() {
        boolean containsValidCharacters;
        String expression = this.expression.toUpperCase();
        if (!isAtomicFormula) containsValidCharacters = expression.matches("^\\((([A-Z0-9()~&]|->)+)(->|V|&)" +
                "(([A-Z0-9()~&]|->)+)\\)$");
        else containsValidCharacters = expression.matches("^([A-Z&~()]|->)+$");
        return containsValidCharacters;
    }

    private boolean isBalanced() {
        int openParens = 0;
        for (int i = 0; i < expression.length(); i++) {
            switch (expression.charAt(i)) {
                case '(' -> openParens++;
                case ')' -> openParens--;
            }
        }
        return openParens == 0;
    }

    private void evaluate() {
        if (!isBalanced()) throw new IllegalArgumentException();
        int openParens = -1;
        boolean foundSign = false;
        for (int i = 0; i < expression.length(); i++) {
            char currentChar = expression.charAt(i);
            int signIndex;
            if (i == 0 && currentChar == '~') {
                sign = "~";
                signIndex = i;
                isAtomicFormula = true;
                antecedent = new Formula(expression.substring(signIndex + 1));
                foundSign = true;
                break;
            }
            switch (currentChar) {
                case '(' -> openParens++;
                case ')' -> openParens--;
                case '-' -> {
                    if (openParens == 0 && i + 1 < expression.length() && expression.charAt(i + 1) == '>') {
                        foundSign = true;
                        i++;
                    }
                }
                case 'v', '&' -> {
                    if (openParens == 0) {
                        foundSign = true;
                    }
                }
            }
            if (foundSign) {
                signIndex = i;
                sign = currentChar == '-' ? "->" : String.valueOf(currentChar);
                antecedent = new Formula(expression.substring(1, signIndex - (sign.equals("->") ? 1 : 0)));
                consequent = new Formula(expression.substring(signIndex + 1, expression.length() - 1));
                break;
            }
        }
        if (!foundSign) {
            isAtomicFormula = true;
            antecedent = this;
        }
        if (!isValid()) throw new IllegalArgumentException();
    }

    public String getSign() {
        return sign;
    }

    public Formula getAntecedent() {
        return antecedent;
    }

    public Formula getConsequent() {
        return consequent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Formula formula = (Formula) o;

        return Objects.equals(expression, formula.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sign, antecedent, consequent);
    }

    @Override
    public String toString() {
        return expression;
    }
}
