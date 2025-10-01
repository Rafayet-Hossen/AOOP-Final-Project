package com.zcare.finalproject;

import javafx.beans.property.*;

public class Doctor {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty email;
    private final StringProperty phone;
    private final StringProperty specialization;

    public Doctor(int id, String email, String name, String phone, String specialization) {
        this.id = new SimpleIntegerProperty(id);
        this.email = new SimpleStringProperty(email);
        this.name = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
        this.specialization = new SimpleStringProperty(specialization);
    }

    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }

    public String getName() { return name.get(); }
    public StringProperty nameProperty() { return name; }

    public String getEmail() { return email.get(); }
    public StringProperty emailProperty() { return email; }

    public String getPhone() { return phone.get(); }
    public StringProperty phoneProperty() { return phone; }

    public String getSpecialization() { return specialization.get(); }
    public StringProperty specializationProperty() { return specialization; }
}
