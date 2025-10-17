package com.zcare.finalproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class DonerDashboardController implements Initializable {

    @FXML private TableView<BloodRequestRow> bloodRequestTable;
    @FXML private TableColumn<BloodRequestRow, Number> requestId;
    @FXML private TableColumn<BloodRequestRow, String> parentsName;
    @FXML private TableColumn<BloodRequestRow, String> parentsPhoneNo;
    @FXML private TableColumn<BloodRequestRow, String> messages;
    @FXML private TableColumn<BloodRequestRow, Void> action;
    @FXML private Label loginDonerId;
    @FXML private Label loginDonerUsername;
    @FXML private Button logoutBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginDonerId.setText(String.valueOf(SessionManager.loggedInDonorId));
        loginDonerUsername.setText(SessionManager.loggedInDonorName);
        loadBloodRequests();
    }

    @FXML
    private void loadBloodRequests() {
        ObservableList<BloodRequestRow> list = FXCollections.observableArrayList();

        String sql = """
        SELECT r.id, p.name, p.phone, r.message
        FROM blood_donor_requests r
        JOIN parents p ON r.parent_id = p.id
        WHERE r.status = 'REQUESTED'
          AND r.needed_to >= NOW()
          AND r.id NOT IN (
              SELECT request_id FROM blood_donor_accepts WHERE donor_id = ?
          )
        ORDER BY r.created_at DESC
    """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, SessionManager.loggedInDonorId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new BloodRequestRow(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("message")
                ));
            }

            bloodRequestTable.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Failed to load blood requests.");
        }

        requestId.setCellValueFactory(data -> data.getValue().idProperty());
        parentsName.setCellValueFactory(data -> data.getValue().parentNameProperty());
        parentsPhoneNo.setCellValueFactory(data -> data.getValue().parentPhoneProperty());
        messages.setCellValueFactory(data -> data.getValue().messageProperty());

        action.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Accept");

            {
                btn.setOnAction(event -> {
                    BloodRequestRow selected = getTableView().getItems().get(getIndex());
                    handleAcceptRequest(selected.getId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void handleAcceptRequest(int requestId) {
        int donorId = SessionManager.loggedInDonorId;

        String insertSql = "INSERT INTO blood_donor_accepts (request_id, donor_id) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(insertSql)) {

            ps.setInt(1, requestId);
            ps.setInt(2, donorId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                AlertUtil.successAlert("You have accepted this request successfully!");
                loadBloodRequests();
            }

        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            AlertUtil.errorAlert("You’ve already accepted this request.");
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Failed to accept request: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("donersLogin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Donor Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Logout failed: " + e.getMessage());
        }
    }


}
