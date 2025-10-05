package com.zcare.finalproject;

import javafx.beans.property.*;

public class BloodRequestRow {
    private final IntegerProperty id;
    private final StringProperty parentName;
    private final StringProperty parentPhone;
    private final StringProperty message;

    public BloodRequestRow(int id, String parentName, String parentPhone, String message) {
        this.id = new SimpleIntegerProperty(id);
        this.parentName = new SimpleStringProperty(parentName);
        this.parentPhone = new SimpleStringProperty(parentPhone);
        this.message = new SimpleStringProperty(message);
    }

    public IntegerProperty idProperty() { return id; }
    public StringProperty parentNameProperty() { return parentName; }
    public StringProperty parentPhoneProperty() { return parentPhone; }
    public StringProperty messageProperty() { return message; }

    public int getId() { return id.get(); }
}
