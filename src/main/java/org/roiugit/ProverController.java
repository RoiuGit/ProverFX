package org.roiugit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ProverController {
    @FXML
    public MenuBar menuBar;
    @FXML
    private TextArea proofTextArea;
    @FXML
    private TextField ruleTextField;
    @FXML
    private Button applyRuleButton;
    @FXML
    private Label messageLabel;
    private Prover prover;

    public void initialize() {
        prover = new Prover();
        prover.makeProofDir();
    }

    @FXML
    private void applyRule() {
        String rule = ruleTextField.getText();
        prover.applyRule(rule);
        proofTextArea.setText(prover.getMainProof());
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
    private void handleNewProof(List<String> premises){
        prover.setMainProof(premises);
        proofTextArea.setText(prover.getMainProof());
    }
}
