package com.zcare.finalproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class BookAppointmentController {
    @FXML
    private ComboBox<Baby> babyCombo;
    @FXML private DatePicker appointmentDate;
    @FXML private TextArea notesField;
    @FXML private Label doctorIdLabel;
    private int doctorId;

    public void setDoctor(int doctorId) {
        this.doctorId = doctorId;
        doctorIdLabel.setText("Doctor ID: " + doctorId);
    }

    @FXML
    public void initialize() {

        ObservableList<Baby> babies = FXCollections.observableArrayList();

        String sql = "SELECT id, name FROM babies WHERE parent_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, SessionManager.loggedInParentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                babies.add(new Baby(rs.getInt("id"), rs.getString("name")));
            }

            babyCombo.setItems(babies);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void submitAppointment() {
        LocalDate date = appointmentDate.getValue();
        Baby selectedBaby = babyCombo.getValue();
        String notes = notesField.getText();

        if (date == null || selectedBaby == null) {
            AlertUtil.errorAlert("Please select baby and date.");
            return;
        }

        String sql = "INSERT INTO appointments (doctor_id, parent_id, baby_id, scheduled_at, notes, status) VALUES (?, ?, ?, ?, ?, 'REQUESTED')";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, doctorId);
            ps.setInt(2, SessionManager.loggedInParentId);
            ps.setInt(3, selectedBaby.getId());
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(date.atStartOfDay()));
            ps.setString(5, notes);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                AlertUtil.successAlert("Appointment request sent.");
                ((Stage) babyCombo.getScene().getWindow()).close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Failed to submit appointment.");
        }
    }
}
