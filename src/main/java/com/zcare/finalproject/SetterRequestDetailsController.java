package com.zcare.finalproject;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SetterRequestDetailsController {

    @FXML private Label titleLabel;
    @FXML private TextArea messageArea;
    @FXML private Label locationLabel;
    @FXML private Label fromDateLabel;
    @FXML private Label toDateLabel;
    @FXML private Button acceptBtn;

    private int requestId;

    public void setRequestId(int requestId) {
        this.requestId = requestId;
        loadRequestDetails();
    }

    private void loadRequestDetails() {
        String sql = "SELECT title, message, location_text, needed_from, needed_to FROM baby_setter_requests WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                titleLabel.setText(rs.getString("title"));
                messageArea.setText(rs.getString("message"));
                locationLabel.setText(rs.getString("location_text"));
                fromDateLabel.setText(rs.getString("needed_from"));
                toDateLabel.setText(rs.getString("needed_to"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Failed to load request details.");
        }
    }

    @FXML
    private void handleAccept() {
        int setterId = SessionManager.loggedInSetterId;
        String sql = "INSERT INTO baby_setter_accepts (request_id, setter_id) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, requestId);
            ps.setInt(2, setterId);

            ps.executeUpdate();
            AlertUtil.successAlert("Request accepted!");
            ((Stage) acceptBtn.getScene().getWindow()).close();

        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            AlertUtil.errorAlert("You have already accepted this request.");
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Error: " + e.getMessage());
        }
    }

}
