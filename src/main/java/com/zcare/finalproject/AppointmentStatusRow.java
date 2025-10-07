package com.zcare.finalproject;

import javafx.beans.property.*;

public class AppointmentStatusRow {
    private final IntegerProperty id;
    private final StringProperty babyName;
    private final StringProperty doctorName;
    private final StringProperty scheduledAt;
    private final StringProperty status;
    private final StringProperty doctorNote;

    public AppointmentStatusRow(int id, String babyName, String doctorName,
                                String scheduledAt, String status, String doctorNote) {
        this.id = new SimpleIntegerProperty(id);
        this.babyName = new SimpleStringProperty(babyName);
        this.doctorName = new SimpleStringProperty(doctorName);
        this.scheduledAt = new SimpleStringProperty(scheduledAt);
        this.status = new SimpleStringProperty(status);
        this.doctorNote = new SimpleStringProperty(doctorNote != null ? doctorNote : "");
    }

    public IntegerProperty idProperty() { return id; }
    public StringProperty babyNameProperty() { return babyName; }
    public StringProperty doctorNameProperty() { return doctorName; }
    public StringProperty scheduledAtProperty() { return scheduledAt; }
    public StringProperty statusProperty() { return status; }
    public StringProperty doctorNoteProperty() { return doctorNote; }
}
