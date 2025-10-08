package com.zcare.finalproject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class UpdateTipController {

    @FXML private TextField updateTitle;
    @FXML private TextArea updateContent;
    @FXML private Button updateBtn;

    private Tip tip;
    private ViewAllTipsController parentController;

    public void setTipData(Tip tip, ViewAllTipsController parentController) {
        this.tip = tip;
        this.parentController = parentController;
        updateTitle.setText(tip.getTitle());
        updateContent.setText(tip.getContent());
    }

    @FXML
    private void handleUpdateTip() {
        String title = updateTitle.getText().trim();
        String content = updateContent.getText().trim();

        if (title.isEmpty() || content.isEmpty()) {
            AlertUtil.errorAlert("Please fill in all fields.");
            return;
        }

        String sql = "UPDATE tips SET title = ?, content = ? WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setString(2, content);
            ps.setInt(3, tip.getId());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                AlertUtil.successAlert("Tip updated successfully!");
                parentController.loadDoctorTips();
                ((Stage) updateBtn.getScene().getWindow()).close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Failed to update tip: " + e.getMessage());
        }
    }
}
