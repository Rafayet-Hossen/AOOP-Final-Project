package com.zcare.finalproject;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DonorRow {
    private final StringProperty name;
    private final StringProperty phone;

    public DonorRow(String name, String phone) {
        this.name = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
    }

    public StringProperty nameProperty() { return name; }
    public StringProperty phoneProperty() { return phone; }
}
