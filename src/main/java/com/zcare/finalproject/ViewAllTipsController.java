package com.zcare.finalproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewAllTipsController {

    @FXML private TableView<Tip> tipsTable;
    @FXML private TableColumn<Tip, Integer> tipId;
    @FXML private TableColumn<Tip, String> tipTitle;
    @FXML private TableColumn<Tip, String> tipContent;
    @FXML private TableColumn<Tip, String> publishedAt;
    @FXML private TableColumn<Tip, Void> actionCol;

    private int doctorId;

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
        loadDoctorTips();
    }

    void loadDoctorTips() {
        ObservableList<Tip> list = FXCollections.observableArrayList();

        String sql = "SELECT id, title, content, published_at FROM tips WHERE doctor_id = ? ORDER BY published_at DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, doctorId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Tip(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("published_at")
                ));
            }

            tipId.setCellValueFactory(new PropertyValueFactory<>("id"));
            tipTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            tipContent.setCellValueFactory(new PropertyValueFactory<>("content"));
            publishedAt.setCellValueFactory(new PropertyValueFactory<>("publishedAt"));

            tipsTable.setItems(list);
            addActionButtons();

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Failed to load tips.");
        }
    }

    private void addActionButtons() {
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button updateBtn = new Button("Update");
            private final Button deleteBtn = new Button("Delete");
            private final HBox box = new HBox(5, updateBtn, deleteBtn);

            {
                updateBtn.setOnAction(event -> {
                    Tip selected = getTableView().getItems().get(getIndex());
                    openUpdatePopup(selected);
                });

                deleteBtn.setOnAction(event -> {
                    Tip selected = getTableView().getItems().get(getIndex());
                    deleteTip(selected.getId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else setGraphic(box);
            }
        });
    }

    private void deleteTip(int id) {
        String sql = "DELETE FROM tips WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                AlertUtil.successAlert("Tip deleted successfully!");
                loadDoctorTips();
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Failed to delete tip.");
        }
    }

    private void openUpdatePopup(Tip tip) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("updateTipPopup.fxml"));
            Parent root = loader.load();

            UpdateTipController controller = loader.getController();
            controller.setTipData(tip, this); // Pass tip and parent controller for refresh

            Stage stage = new Stage();
            stage.setTitle("Update Tip");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Failed to open update form.");
        }
    }
}
