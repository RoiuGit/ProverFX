module org.roiugit {
    requires javafx.controls;
    requires com.google.gson;
    requires javafx.fxml;

    opens org.roiugit to javafx.fxml;
    exports org.roiugit;
}
