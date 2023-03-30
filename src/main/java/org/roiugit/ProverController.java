package org.roiugit;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ProverController implements UIConstants {
    private final ProofFileManager proofFileManager = new ProofFileManager();
    @FXML
    public TextField assumptionTextField;
    @FXML
    public ComboBox<String> rulesComboBox;
    @FXML
    public HBox ruleInputContainer;
    @FXML
    public Menu fileMenu;
    @FXML
    public MenuItem saveMenuItem;
    @FXML
    public VBox BottomVBox;
    @FXML
    public MenuItem setTargetMenuItem;
    @FXML
    public Menu deductionMenu;
    Alert extenstion_alert = new Alert(Alert.AlertType.ERROR, EXTENSION_ALERT);
    @FXML
    private TextArea proofTextArea;
    @FXML
    private Label messageLabel;
    private Prover prover;
    private RuleApplication ruleApplication;

    public void initialize() {
        prover = new Prover();
        ruleApplication = new RuleApplication(prover, ruleInputContainer);
        List<String> ruleNames = prover.getRuleInfo().stream().map(RuleInfo::getName).toList();
        rulesComboBox.setItems(FXCollections.observableArrayList(ruleNames));
        rulesComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> ruleApplication.updatePremisesComboBoxes(newValue));
        fileMenu.addEventHandler(Menu.ON_SHOWN, menu_event -> updateSaveMenuItem());
        deductionMenu.addEventHandler(Menu.ON_SHOWN, menu_event -> updateSetTargetMenuItem());
        extenstion_alert.setHeaderText(null);
    }

    private void updateSetTargetMenuItem() {
        setTargetMenuItem.setDisable(prover.isEmpty() || !prover.isNotClosed());
    }

    private void updateSaveMenuItem() {
        saveMenuItem.setDisable(prover.isEmpty());
    }

    @FXML
    private void applyRule() {
        String ruleName = rulesComboBox.getValue();
        try {
            ruleApplication.applyRule(ruleName);
            updateProofTextArea();
        } catch (Exception e) {
            if (prover.isEmpty()) messageLabel.setText(EMPTY_PROOF_MESSAGE);
            else if (!prover.isNotClosed()) messageLabel.setText(CLOSED_PROOF_MESSAGE);
            else messageLabel.setText(COULD_NOT_APPLY_RULE_MESSAGE + ruleName);
        }
    }

    @FXML
    public void displayRules() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(RULES_FXML_PATH));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(RULES_WINDOW_TITLE);
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
            @SuppressWarnings("duplicate")
            FXMLLoader loader = new FXMLLoader(getClass().getResource(NEW_PROOF_FXML_PATH));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(NEW_PROOF_WINDOW_TITLE);
            stage.setScene(new Scene(root));
            stage.setMinWidth(150);
            stage.setMinHeight(150);
            stage.setResizable(false);
            NewProofController controller = loader.getController();
            controller.setOnSubmit(this::handleNewProofSubmission);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateProofTextArea() {
        messageLabel.setText("");
        if (prover.targetReached()) {
            messageLabel.setText("Target %s reached.".formatted(prover.getTarget()));
            prover.endProof();
        }
        if (prover.isNotClosed())
            proofTextArea.setText("%s%s".formatted(prover.getTarget() != null ? "Target:%s%n".formatted(prover.getTarget()) : "", prover.getMainProof()));
        else proofTextArea.setText("%s\n%s".formatted(prover.getMainProof(), prover.getResult()));
    }

    private void handleNewProofSubmission(List<String> premises) {
        prover.setMainProof(premises);
        updateProofTextArea();
    }

    @FXML
    public void addAssumption() {
        if (prover.getMainProof() == null) prover.setMainProof();
        if (prover.isNotClosed()) {
            prover.assume(assumptionTextField.getText());
            updateProofTextArea();
            assumptionTextField.clear();
        } else messageLabel.setText(CANNOT_ADD_ASSUMPTION_MESSAGE);
    }

    @FXML
    public void endProof() {
        prover.endProof();
        updateProofTextArea();
    }

    public void saveProof() {
        File selectedFile = proofFileManager.saveProof();
        if (selectedFile != null)
            if (selectedFile.getName().matches(PROOF_FILES_EXTENSION_REGEX))
                messageLabel.setText(prover.saveProof(selectedFile));
            else extenstion_alert.show();
    }

    public void loadProof() {
        File selectedFile = proofFileManager.loadProof();
        if (selectedFile != null) {
            if (selectedFile.getName().matches(PROOF_FILES_EXTENSION_REGEX)) {
                prover.loadProof(selectedFile);
                updateProofTextArea();
            } else {
                extenstion_alert.show();
            }
        }

    }

    public void setTarget() {
        String target;
        TextInputDialog targetInputDialog = new TextInputDialog("Enter a target for your proof.");
        targetInputDialog.setTitle("Set Target");
        targetInputDialog.setHeaderText(null);
        targetInputDialog.showAndWait();
        target = targetInputDialog.getResult();
        prover.setTarget(target);
        updateProofTextArea();
    }
}
