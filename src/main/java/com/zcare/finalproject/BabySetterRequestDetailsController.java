package com.zcare.finalproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BabySetterRequestDetailsController {

    @FXML private Label titleLabel;
    @FXML private Label messageLabel;
    @FXML private Label locationLabel;
    @FXML private Label neededFromLabel;
    @FXML private Label neededToLabel;
    @FXML private Label statusLabel;
    @FXML private TableView<BabySetterAcceptRow> setterTable;
    @FXML private TableColumn<BabySetterAcceptRow, String> setterNameCol;
    @FXML private TableColumn<BabySetterAcceptRow, String> setterPhoneCol;
    @FXML private Button closeButton;

    private int requestId;

    public void setRequestId(int requestId) {
        this.requestId = requestId;
        loadRequestDetails();
        loadAcceptedSetters();
    }

    @FXML
    private void initialize() {
        setterNameCol.setCellValueFactory(data -> data.getValue().setterNameProperty());
        setterPhoneCol.setCellValueFactory(data -> data.getValue().setterPhoneProperty());

        closeButton.setOnAction(e -> ((Stage) closeButton.getScene().getWindow()).close());
    }

    private void loadRequestDetails() {
        String sql = """
            SELECT title, message, location_text, needed_from, needed_to, status
            FROM baby_setter_requests
            WHERE id = ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                titleLabel.setText("Title: " + rs.getString("title"));
                messageLabel.setText("Message: " + rs.getString("message"));
                locationLabel.setText("Location: " + rs.getString("location_text"));
                neededFromLabel.setText("Needed From: " + rs.getString("needed_from"));
                neededToLabel.setText("Needed To: " + rs.getString("needed_to"));
                statusLabel.setText("Status: " + rs.getString("status"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Failed to load request details.");
        }
    }

    private void loadAcceptedSetters() {
        ObservableList<BabySetterAcceptRow> list = FXCollections.observableArrayList();

        String sql = """
            SELECT s.name, s.phone
            FROM baby_setter_accepts a
            JOIN baby_setters s ON a.setter_id = s.id
            WHERE a.request_id = ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new BabySetterAcceptRow(
                        rs.getString("name"),
                        rs.getString("phone")
                ));
            }

            setterTable.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Failed to load accepted setters.");
        }
    }
}
