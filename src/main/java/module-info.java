module proverfx {
    requires javafx.controls;
    requires com.google.gson;
    requires javafx.fxml;

    opens org.roiugit to javafx.fxml;
    opens org.roiugit.proof to com.google.gson;
    opens org.roiugit.formula to com.google.gson;
    opens org.roiugit.rules to com.google.gson;
    exports org.roiugit;
}
