package com.zcare.finalproject;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class CompleteAppointmentController {

    @FXML
    private TextArea doctorNoteArea;
    @FXML
    private Button submitCompletionBtn;

    private int appointmentId;
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    @FXML
    private void handleSubmitCompletion() {
        String doctorNote = doctorNoteArea.getText().trim();
        if (doctorNote.isEmpty()) {
            AlertUtil.errorAlert("Please write a note before completing.");
            return;
        }

        String sql = "UPDATE appointments SET doctor_note = ?, status = 'COMPLETED' WHERE id = ?";
        String logSql = "INSERT INTO appointment_logs (appointment_id, status_before, status_after, changed_by) VALUES (?, 'CONFIRMED', 'COMPLETED', 'DOCTOR')";

        try (Connection con = DBConnection.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, doctorNote);
                ps.setInt(2, appointmentId);
                ps.executeUpdate();
            }

            try (PreparedStatement ps2 = con.prepareStatement(logSql)) {
                ps2.setInt(1, appointmentId);
                ps2.executeUpdate();
            }

            AlertUtil.successAlert("Appointment marked as completed.");
            Stage stage = (Stage) submitCompletionBtn.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Failed to complete appointment: " + e.getMessage());
        }
    }
}
