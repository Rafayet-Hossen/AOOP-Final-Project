package com.zcare.finalproject;

import javafx.beans.property.*;

public class BabySetterRequestRow {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty parentName = new SimpleStringProperty();
    private final StringProperty parentPhone = new SimpleStringProperty();
    private final StringProperty message = new SimpleStringProperty();

    public BabySetterRequestRow(int id, String parentName, String parentPhone, String message) {
        this.id.set(id);
        this.parentName.set(parentName);
        this.parentPhone.set(parentPhone);
        this.message.set(message);
    }

    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }

    public String getParentName() { return parentName.get(); }
    public StringProperty parentNameProperty() { return parentName; }

    public String getParentPhone() { return parentPhone.get(); }
    public StringProperty parentPhoneProperty() { return parentPhone; }

    public String getMessage() { return message.get(); }
    public StringProperty messageProperty() { return message; }
}
