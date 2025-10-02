package com.zcare.finalproject;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Doner {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty email;
    private final StringProperty phone;
    private final StringProperty bloodGroup;

    public Doner(int id, String email,String name,String phone,String bloodGroup){
        this.id = new SimpleIntegerProperty(id);
        this.email = new SimpleStringProperty(email);
        this.name = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
        this.bloodGroup = new SimpleStringProperty(bloodGroup);
    }
    public int getId() {return id.get();}
    public IntegerProperty idProperty() {return id;}
    public String getName() {return name.get();}
    public StringProperty nameProperty() {return name;}
    public String getEmail() {return email.get();}
    public StringProperty emailProperty() {return email;}
    public String getPhone() {return phone.get();}
    public StringProperty phoneProperty(){return phone;}
    public String getBloodGroup() {return bloodGroup.get();}
    public StringProperty bloodGroupProperty(){return bloodGroup;}
}
