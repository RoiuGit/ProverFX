package org.roiugit;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class RuleApplication {
    private final Prover prover;
    private final HBox ruleInputContainer;

    RuleApplication(Prover prover, HBox ruleInputContainer) {
        this.prover = prover;
        this.ruleInputContainer = ruleInputContainer;
    }

    public void updatePremisesComboBoxes(String ruleName) {
        ruleInputContainer.getChildren().clear();
        RuleInfo selectedRule = prover.getRuleInfo().stream().
                filter(rule -> rule.getName().equals(ruleName)).
                findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid rule name: " + ruleName));
        int numPremises = selectedRule.getNumPremises();
        for (int i = 0; i < numPremises; i++) {
            ComboBox<Integer> premiseComboBox = new ComboBox<>();
            updatePremiseComboBox(premiseComboBox);
            premiseComboBox.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> updatePremiseComboBox(premiseComboBox));
            ruleInputContainer.getChildren().add(premiseComboBox);
        }
        if (ruleName.matches("DI.")) {
            TextField disjunctField = new TextField();
            disjunctField.setPromptText("Enter the disjunct");
            ruleInputContainer.getChildren().add(disjunctField);
        }
    }

    private void updatePremiseComboBox(ComboBox<Integer> premiseComboBox) {
        if (prover.getMainProof() != null)
            premiseComboBox.setItems(FXCollections.observableArrayList(IntStream.range(prover.getStartingIndex(), prover.getEndingIndex() + 1).boxed().toList()));
    }

    private List<Integer> getSelectedPremises() {
        List<Integer> selectedValues = new ArrayList<>();
        for (Node child : ruleInputContainer.getChildren()) {
            if (child instanceof ComboBox<?>) {
                @SuppressWarnings("unchecked")
                ComboBox<Integer> premiseComboBox = (ComboBox<Integer>) child;
                Integer selectedValue = premiseComboBox.getValue();
                selectedValues.add(selectedValue);
            }
        }
        return selectedValues;
    }

    public void applyRule(String ruleName) {
        String expression = null;
        for (Node child : ruleInputContainer.getChildren()) {
            if (child instanceof TextField disjunctField) {
                expression = disjunctField.getText();
            }
        }
        List<Integer> premises = getSelectedPremises();
        if (!prover.isEmpty() && prover.isNotClosed()) {
            try {
                if (ruleName.matches("DI.")) prover.applyRule(ruleName, premises, expression);
                else prover.applyRule(ruleName, premises);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else throw new IllegalArgumentException();
    }
}
