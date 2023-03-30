package org.roiugit;

public interface UIConstants {
    String RULES_WINDOW_TITLE = "Rules";
    String NEW_PROOF_WINDOW_TITLE = "New Proof";
    String PROOF_FILES_EXTENSION = ".proof";
    String PROOF_FILES_EXTENSION_FILTER = "*" + PROOF_FILES_EXTENSION;
    String EXTENSION_ALERT = "File must be a proof file (" + PROOF_FILES_EXTENSION_FILTER + ")";
    String PROOF_FILES_EXTENSION_REGEX = ".*\\" + PROOF_FILES_EXTENSION;
    String PROOF_FILES_DESCRIPTION = "Proof files";
    String RULES_FXML_PATH = "rules.fxml";
    String NEW_PROOF_FXML_PATH = "newproof.fxml";
    String COULD_NOT_APPLY_RULE_MESSAGE = "Could not apply rule ";
    String EMPTY_PROOF_MESSAGE = "Cannot apply rule to an empty proof.";
    String CLOSED_PROOF_MESSAGE = "Cannot apply rule to a closed proof.";
    String CANNOT_ADD_ASSUMPTION_MESSAGE = "Cannot add assumptions to a closed proof.";
}
