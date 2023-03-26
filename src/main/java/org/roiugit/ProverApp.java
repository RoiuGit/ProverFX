package org.roiugit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ProverApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ui.fxml")));
            primaryStage.setTitle("Prover");
            primaryStage.setScene(new Scene(root));
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            primaryStage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("provericon.png"))));
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
