package org.roiugit;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NewProofController {
    Consumer<List<String>> onSubmit;
    @FXML
    private VBox newProofVBox;
    @FXML
    private TextField premiseField;
    @FXML
    private VBox premisesListVBox; // VBox to display the list of premises
    @FXML
    private Button createProofButton;
    @FXML
    private Button addPremiseButton;
    private List<String> premises;

    @FXML
    private void initialize() {
        premises = new ArrayList<>();
    }

    @FXML
    private void addPremise() {
        String premise = premiseField.getText();
        premises.add(premise);
        updatePremisesList(); // Update the list view
        premiseField.clear();
    }

    private void updatePremisesList() {
        premisesListVBox.getChildren().clear(); // Clear the current list
        for (String premise : premises) {
            premisesListVBox.getChildren().add(new Label(premise));
        }
    }

    @FXML
    private void createProof() {
        onSubmit.accept(premises);
        Stage stage = (Stage) createProofButton.getScene().getWindow();
        stage.close();
    }

    public void setOnSubmit(Consumer<List<String>> onSubmit) {
        this.onSubmit = onSubmit;
    }
}
