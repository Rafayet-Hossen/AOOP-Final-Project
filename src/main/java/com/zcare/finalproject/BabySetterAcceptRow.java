package com.zcare.finalproject;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BabySetterAcceptRow {
    private final StringProperty setterName;
    private final StringProperty setterPhone;

    public BabySetterAcceptRow(String setterName, String setterPhone) {
        this.setterName = new SimpleStringProperty(setterName);
        this.setterPhone = new SimpleStringProperty(setterPhone);
    }

    public StringProperty setterNameProperty() { return setterName; }
    public StringProperty setterPhoneProperty() { return setterPhone; }
}
