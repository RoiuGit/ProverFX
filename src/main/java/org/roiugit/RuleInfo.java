package org.roiugit;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class RuleInfo {
    private final SimpleStringProperty name;
    private final SimpleStringProperty schema;

    private final SimpleIntegerProperty numPremises;

    RuleInfo(String name, String schema, int numPremises) {
        this.name = new SimpleStringProperty(name);
        this.schema = new SimpleStringProperty(schema);
        this.numPremises = new SimpleIntegerProperty(numPremises);
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

    public int getNumPremises() {
        return numPremises.get();
    }

    public SimpleIntegerProperty numPremisesProperty() {
        return numPremises;
    }
}
