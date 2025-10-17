package com.zcare.finalproject;

import javafx.beans.property.*;

public class BabySetterRequest {
    private final IntegerProperty id;
    private final StringProperty parentName;
    private final StringProperty parentPhone;
    private final StringProperty message;
    private final StringProperty acceptedByName;
    private final StringProperty acceptedByPhone;
    private final StringProperty createdAt;
    private final StringProperty status;

    public BabySetterRequest(int id, String parentName, String parentPhone, String message) {
        this(id, parentName, parentPhone, message, "Pending", "-", "-", "REQUESTED");
    }

    public BabySetterRequest(int id, String parentName, String acceptedByName, String acceptedByPhone, String createdAt, String status) {
        this(id, parentName, "-", "-", acceptedByName, acceptedByPhone, createdAt, status);
    }

    public BabySetterRequest(int id, String parentName, String parentPhone, String message, String acceptedByName, String acceptedByPhone, String createdAt, String status) {
        this.id = new SimpleIntegerProperty(id);
        this.parentName = new SimpleStringProperty(parentName);
        this.parentPhone = new SimpleStringProperty(parentPhone);
        this.message = new SimpleStringProperty(message);
        this.acceptedByName = new SimpleStringProperty(acceptedByName);
        this.acceptedByPhone = new SimpleStringProperty(acceptedByPhone);
        this.createdAt = new SimpleStringProperty(createdAt);
        this.status = new SimpleStringProperty(status);
    }

    public IntegerProperty idProperty() { return id; }
    public StringProperty parentNameProperty() { return parentName; }
    public StringProperty parentPhoneProperty() { return parentPhone; }
    public StringProperty messageProperty() { return message; }
    public StringProperty acceptedByNameProperty() { return acceptedByName; }
    public StringProperty acceptedByPhoneProperty() { return acceptedByPhone; }
    public StringProperty createdAtProperty() { return createdAt; }
    public StringProperty statusProperty() { return status; }


    public int getId() { return id.get(); }
}
