package org.roiugit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.function.Consumer;

public class RulesController {
    @FXML
    public TableView<RuleInfo> rulesTableView;
    @FXML
    public TableColumn<RuleInfo, String> ruleNameColumn;
    @FXML
    public TableColumn<RuleInfo, String> ruleSchemaColumn;
    ObservableList<RuleInfo> data;


    public void initialize() {
        ruleNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ruleSchemaColumn.setCellValueFactory(new PropertyValueFactory<>("schema"));
    }

    public void setRules(List<RuleInfo> ruleInfoList) {
        data = FXCollections.observableArrayList(ruleInfoList);
        rulesTableView.setItems(data);
    }
}
