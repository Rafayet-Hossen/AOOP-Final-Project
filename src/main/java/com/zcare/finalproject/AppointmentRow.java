package com.zcare.finalproject;

import javafx.beans.property.*;

public class AppointmentRow {
    private final IntegerProperty id;
    private final StringProperty babyName;
    private final StringProperty scheduledAt;
    private final StringProperty status;
    private final StringProperty notes;

    public AppointmentRow(int id, String babyName, String parentName, String parentPhone, String babyDob, String scheduledAt, String status, String notes) {
        this.id = new SimpleIntegerProperty(id);
        this.babyName = new SimpleStringProperty(babyName);
        this.scheduledAt = new SimpleStringProperty(scheduledAt);
        this.status = new SimpleStringProperty(status);
        this.notes = new SimpleStringProperty(notes);
    }

    public IntegerProperty idProperty() { return id; }
    public StringProperty babyNameProperty() { return babyName; }
    public StringProperty scheduledAtProperty() { return scheduledAt; }
    public StringProperty statusProperty() { return status; }
    public StringProperty notesProperty() { return notes; }

    public int getId() {
        return id.get();
    }
}
