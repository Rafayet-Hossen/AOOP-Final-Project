package com.zcare.finalproject;

import javafx.beans.property.*;

public class Baby {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty dob;
    private final StringProperty gender;
    private final StringProperty bloodGroup;
    private final StringProperty parentName;
    private final StringProperty parentEmail;
    private final StringProperty parentPhone;

    public Baby(int id, String name, String dob, String gender, String bloodGroup,
                String parentName, String parentEmail, String parentPhone) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.dob = new SimpleStringProperty(dob);
        this.gender = new SimpleStringProperty(gender);
        this.bloodGroup = new SimpleStringProperty(bloodGroup);
        this.parentName = new SimpleStringProperty(parentName);
        this.parentEmail = new SimpleStringProperty(parentEmail);
        this.parentPhone = new SimpleStringProperty(parentPhone);
    }

    public IntegerProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty dobProperty() { return dob; }
    public StringProperty genderProperty() { return gender; }
    public StringProperty bloodGroupProperty() { return bloodGroup; }
    public StringProperty parentNameProperty() { return parentName; }
    public StringProperty parentEmailProperty() { return parentEmail; }
    public StringProperty parentPhoneProperty() { return parentPhone; }
}
