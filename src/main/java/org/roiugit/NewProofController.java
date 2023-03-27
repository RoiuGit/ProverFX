package org.roiugit;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NewProofController {
    @FXML
    public TextField premiseField;
    Consumer<List<String>> onSubmit;
    private List<String> premises;
    @FXML
    private VBox newProofVBox;
    @FXML
    private Button addPremiseButton;
    @FXML
    private Button createProofButton;

    @FXML
    private void initialize() {
        premises = new ArrayList<>();
    }

    @FXML
    private void addPremise() {
        premises.add(premiseField.getText());
        premiseField.clear();
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
