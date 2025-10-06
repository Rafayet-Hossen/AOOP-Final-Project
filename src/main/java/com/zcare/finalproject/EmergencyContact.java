package com.zcare.finalproject;

import javafx.beans.property.*;

public class EmergencyContact {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty contactNumber;
    private final StringProperty type;
    private final StringProperty location;
    private final StringProperty address;

    public EmergencyContact(int id, String name, String contactNumber, String type, String location, String address) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.contactNumber = new SimpleStringProperty(contactNumber);
        this.type = new SimpleStringProperty(type);
        this.location = new SimpleStringProperty(location);
        this.address = new SimpleStringProperty(address);
    }

    public IntegerProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty contactNumberProperty() { return contactNumber; }
    public StringProperty typeProperty() { return type; }
    public StringProperty locationProperty() { return location; }
    public StringProperty addressProperty() { return address; }

    public int getId() { return id.get(); }
    public String getName() { return name.get(); }
    public String getContactNumber() { return contactNumber.get(); }
    public String getType() { return type.get(); }
    public String getLocation() { return location.get(); }
    public String getAddress() { return address.get(); }
}
