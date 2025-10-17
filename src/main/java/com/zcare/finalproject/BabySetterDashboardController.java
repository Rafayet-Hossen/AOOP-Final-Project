package com.zcare.finalproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class BabySetterDashboardController implements Initializable {

    @FXML private Label dateTime;
    @FXML private Label loginSetterId;
    @FXML private Label loginSetterName;
    @FXML private Button setterLogoutBtn;
    @FXML private Button seeSetterRequestBtn;

    @FXML private AnchorPane setterRequestPane;
    @FXML private TableView<BabySetterRequest> setterRequestTable;
    @FXML private TableColumn<BabySetterRequest, Number> setterRequestId;
    @FXML private TableColumn<BabySetterRequest, String> setterRequestParents;
    @FXML private TableColumn<BabySetterRequest, String> setterParentsNo;
    @FXML private TableColumn<BabySetterRequest, String> parentsMessage;
    @FXML private TableColumn<BabySetterRequest, Void> setterAction;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loginSetterId.setText(String.valueOf(SessionManager.loggedInSetterId));
        loginSetterName.setText(SessionManager.loggedInSetterName);

        setterRequestId.setCellValueFactory(data -> data.getValue().idProperty());
        setterRequestParents.setCellValueFactory(data -> data.getValue().parentNameProperty());
        setterParentsNo.setCellValueFactory(data -> data.getValue().parentPhoneProperty());
        parentsMessage.setCellValueFactory(data -> data.getValue().messageProperty());

        setterAction.setCellFactory(col -> new TableCell<>() {
            private final Button viewBtn = new Button("View Details");

            {
                viewBtn.setOnAction(event -> {
                    BabySetterRequest selected = getTableView().getItems().get(getIndex());
                    openRequestDetails(selected.getId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : viewBtn);
            }
        });

        loadMySetterRequests();
    }

    private void loadMySetterRequests() {
        ObservableList<BabySetterRequest> list = FXCollections.observableArrayList();

        String sql = """
        SELECT r.id, p.name AS parent_name, p.phone AS parent_phone, r.message
        FROM baby_setter_requests r
        JOIN parents p ON r.parent_id = p.id
        WHERE r.status = 'REQUESTED'
          AND r.needed_to >= NOW()
          AND r.id NOT IN (
              SELECT request_id
              FROM baby_setter_accepts
              WHERE setter_id = ?
          )
        ORDER BY r.created_at DESC
    """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, SessionManager.loggedInSetterId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new BabySetterRequest(
                        rs.getInt("id"),
                        rs.getString("parent_name"),
                        rs.getString("parent_phone"),
                        rs.getString("message")
                ));
            }

            setterRequestTable.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Failed to load baby setter requests.");
        }
    }

    private void openRequestDetails(int requestId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("setterRequestDetails.fxml"));
            Parent root = loader.load();

            SetterRequestDetailsController controller = loader.getController();
            controller.setRequestId(requestId);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Request Details");
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Failed to open request details.");
        }
    }



    private void handleAcceptRequest(int requestId) {
        int setterId = SessionManager.loggedInSetterId;

        String sql = "INSERT INTO baby_setter_accepts (request_id, setter_id) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, requestId);
            ps.setInt(2, setterId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                AlertUtil.successAlert("You have accepted this request.");
                loadMySetterRequests();
            } else {
                AlertUtil.errorAlert("Could not accept the request.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("babySetters.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) setterLogoutBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Baby Setter Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Logout failed: " + e.getMessage());
        }
    }
}
