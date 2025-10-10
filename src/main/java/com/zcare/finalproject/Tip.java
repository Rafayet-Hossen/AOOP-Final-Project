package com.zcare.finalproject;

import javafx.beans.property.*;

public class Tip {
    private final IntegerProperty id;
    private final StringProperty title;
    private final StringProperty content;
    private final StringProperty publishedAt;
    private final StringProperty doctorName;
    private final StringProperty specialization;

    public Tip(int id, String title, String content, String publishedAt,
               String doctorName, String specialization) {
        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.content = new SimpleStringProperty(content);
        this.publishedAt = new SimpleStringProperty(publishedAt);
        this.doctorName = new SimpleStringProperty(doctorName);
        this.specialization = new SimpleStringProperty(specialization);
    }

    public Tip(int id, String title, String content, String publishedAt) {
        this(id, title, content, publishedAt, "", "");
    }

    public IntegerProperty idProperty() { return id; }
    public StringProperty titleProperty() { return title; }
    public StringProperty contentProperty() { return content; }
    public StringProperty publishedAtProperty() { return publishedAt; }

    public int getId() { return id.get(); }
    public String getTitle() { return title.get(); }
    public String getContent() { return content.get(); }
    public String getPublishedAt() { return publishedAt.get(); }

    public StringProperty doctorNameProperty() { return doctorName; }
    public String getDoctorName() { return doctorName.get(); }

    public StringProperty specializationProperty() { return specialization; }
    public String getSpecialization() { return specialization.get(); }
}
