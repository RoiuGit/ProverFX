package org.roiugit;

import javafx.beans.property.SimpleStringProperty;

public class RuleInfo {
    private final SimpleStringProperty name;
    private final SimpleStringProperty schema;

    RuleInfo(String name, String schema) {
        this.name = new SimpleStringProperty(name);
        this.schema = new SimpleStringProperty(schema);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getSchema() {
        return schema.get();
    }

    public SimpleStringProperty schemaProperty() {
        return schema;
    }
}
