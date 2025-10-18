package com.zcare.finalproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;

public class ParentBloodRequestDetailsController {

    @FXML private Label titleLabel, messageLabel, locationLabel, dateRangeLabel;
    @FXML private TableView<DonorRow> donorTable;
    @FXML private TableColumn<DonorRow, String> donorNameCol;
    @FXML private TableColumn<DonorRow, String> donorPhoneCol;
    @FXML private Button completeRequestBtn;

    private int requestId;

    public void setRequestId(int requestId) {
        this.requestId = requestId;

        donorNameCol.setCellValueFactory(cell -> cell.getValue().nameProperty());
        donorPhoneCol.setCellValueFactory(cell -> cell.getValue().phoneProperty());

        loadDetails();
    }

    private void loadDetails() {
        ObservableList<DonorRow> donors = FXCollections.observableArrayList();

        String requestSql = """
            SELECT title, message, location_text, needed_from, needed_to, status
            FROM blood_donor_requests
            WHERE id = ?
        """;

        String donorSql = """
            SELECT d.name, d.phone
            FROM blood_donor_accepts a
            JOIN blood_donors d ON a.donor_id = d.id
            WHERE a.request_id = ?
        """;

        try (Connection con = DBConnection.getConnection()) {

            try (PreparedStatement ps = con.prepareStatement(requestSql)) {
                ps.setInt(1, requestId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    titleLabel.setText("Title: " + rs.getString("title"));
                    messageLabel.setText("Message: " + rs.getString("message"));
                    locationLabel.setText("Location: " + rs.getString("location_text"));
                    dateRangeLabel.setText("Needed: " + rs.getString("needed_from") + " to " + rs.getString("needed_to"));

                    if ("COMPLETED".equals(rs.getString("status"))) {
                        completeRequestBtn.setDisable(true);
                        completeRequestBtn.setText("Already Completed");
                    }
                }
            }

            try (PreparedStatement ps = con.prepareStatement(donorSql)) {
                ps.setInt(1, requestId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    donors.add(new DonorRow(rs.getString("name"), rs.getString("phone")));
                }
            }

            donorTable.setItems(donors);

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Failed to load request details.");
        }
    }

    @FXML
    private void markRequestAsCompleted() {
        String sql = "UPDATE blood_donor_requests SET status = 'COMPLETED' WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, requestId);

            int updated = ps.executeUpdate();
            if (updated > 0) {
                AlertUtil.successAlert("Request marked as completed.");
                completeRequestBtn.setDisable(true);
                completeRequestBtn.setText("Already Completed");
            } else {
                AlertUtil.errorAlert("Could not complete request.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Database error.");
        }
    }
}
