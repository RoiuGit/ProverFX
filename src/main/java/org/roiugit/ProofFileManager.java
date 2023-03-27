package org.roiugit;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ProofFileManager implements UIConstants {
    private final FileChooser proofChooser = new FileChooser();

    ProofFileManager() {
        proofChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(PROOF_FILES_DESCRIPTION, PROOF_FILES_EXTENSION_FILTER));
        String userDirectoryString = System.getProperty("user.home");
        userDirectoryString += "/Documents/myProofs/";
        File userDirectory = new File(userDirectoryString);
        if (!userDirectory.canRead()) userDirectory = new File("C:/");
        proofChooser.setInitialDirectory(userDirectory);
    }

    public File saveProof() {
        return proofChooser.showSaveDialog(new Stage());
    }

    public File loadProof() {
        return proofChooser.showOpenDialog(new Stage());
    }
}
