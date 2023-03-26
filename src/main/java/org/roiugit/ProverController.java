package org.roiugit;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ProverController {
    @FXML
    public TextField assumptionTextField;
    public ComboBox<String> rulesComboBox;
    public HBox premisesComboBoxContainer;
    private final FileChooser proofChooser = new FileChooser();
    @FXML
    private TextArea proofTextArea;
    @FXML
    private Label messageLabel;
    private Prover prover;

    public void initialize() {
        prover = new Prover();
        prover.makeProofDir();
        proofChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Proof files", "*.proof"));
        proofChooser.setInitialDirectory(new File(prover.getPath()));
        List<String> ruleNames = prover.getRuleInfo().stream().map(RuleInfo::getName).toList();
        rulesComboBox.setItems(FXCollections.observableArrayList(ruleNames));
        rulesComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> updatePremisesComboBoxes(newValue));
    }

    private void updatePremisesComboBoxes(String ruleName) {
        premisesComboBoxContainer.getChildren().clear();
        RuleInfo selectedRule = prover.getRuleInfo().stream().filter(rule -> rule.getName().equals(ruleName)).findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid rule name: " + ruleName));
        int numPremises = selectedRule.getNumPremises();
        for (int i = 0; i < numPremises; i++) {
            ComboBox<Integer> premiseComboBox = new ComboBox<>();
            if (prover.getMainProof() != null) premiseComboBox.setItems(FXCollections.observableArrayList(IntStream.range(prover.getStartingIndex(), prover.getEndingIndex() + 1).boxed().toList()));
            premisesComboBoxContainer.getChildren().add(premiseComboBox);
        }
    }

    private List<Integer> getSelectedPremises(){
        List<Integer> selectedValues = new ArrayList<>();
        for (Node child : premisesComboBoxContainer.getChildren()){
            if (child instanceof ComboBox){
                @SuppressWarnings("unchecked")
                ComboBox<Integer> premiseComboBox = (ComboBox<Integer>) child;
                Integer selectedValue = premiseComboBox.getValue();
                selectedValues.add(selectedValue);
            }
        }
        return selectedValues;
    }
    @FXML
    private void applyRule() {
        String rule = rulesComboBox.getValue();
        List<Integer> premises = getSelectedPremises();
        if (prover.isNotClosed()) {
            try {
                prover.applyRule(rule, premises);
                proofTextArea.setText(prover.getMainProof());
            } catch (Exception e) {
                messageLabel.setText("Could not apply rule.");
            }
        } else {
            messageLabel.setText("Cannot apply rule to a closed proof.");
        }
    }

    @FXML
    public void displayRules() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("rules.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Rules");
            stage.setScene(new Scene(root));
            List<RuleInfo> ruleInfoList = prover.getRuleInfo();
            RulesController controller = loader.getController();
            controller.setRules(ruleInfoList);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void newProof() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("newproof.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("New Proof");
            stage.setScene(new Scene(root));
            stage.setMinWidth(150);
            stage.setMinHeight(150);
            stage.setResizable(false);
            NewProofController controller = loader.getController();
            controller.setOnSubmit(this::handleNewProof);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleNewProof(List<String> premises) {
        prover.setMainProof(premises);
        proofTextArea.setText(prover.getMainProof());
    }

    public void assume() {
        if (prover.getMainProof() == null) prover.setMainProof();
        prover.assume(assumptionTextField.getText());
        proofTextArea.setText(prover.getMainProof());
        assumptionTextField.clear();
    }

    public void endProof() {
        String result = prover.endProof();
        proofTextArea.setText("%s\n%s".formatted(prover.getMainProof(), result));
    }

    public void saveProof() {
        File selectedFile = proofChooser.showSaveDialog(new Stage());
        if (selectedFile != null) messageLabel.setText(prover.saveProof(selectedFile));
    }

    public void loadProof() {
        File selectedFile = proofChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            prover.loadProof(selectedFile);
            proofTextArea.setText(prover.getMainProof());
        }

    }
}
