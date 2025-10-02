package com.zcare.finalproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class AdminController extends PageUtil implements Initializable {

    @FXML
    private TextField adminEmail;

    @FXML
    private Button adminLoginBtn;

    @FXML
    private PasswordField adminPassword;

    @FXML
    private Pane mainpane;

    @FXML
    private StackPane parentsLoginPage;

    @FXML
    private ComboBox<String> selectUser;

    private Connection connection;
    private PreparedStatement ps;
    private ResultSet rs;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PageUtil.populateUsers(selectUser);
        selectUser.getSelectionModel().select("Admin");
        selectUser.setOnAction(event -> {
            String selected = selectUser.getSelectionModel().getSelectedItem();
            if(!"Parent".equals(selected)){
                try {
                    PageUtil.switchUser(selectUser);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void adminLogin(){
        String email = adminEmail.getText();
        String password = adminPassword.getText();

        if(email.isEmpty() || password.isEmpty()){
            AlertUtil.errorAlert("Please fill in all fields.");
        }

        String query = "SELECT * FROM admins WHERE email = ? AND password = ?";

        try{
            connection = DBConnection.getConnection();
            if(connection == null){
                AlertUtil.errorAlert("Database connection failed.");
            }
            ps = connection.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) {
                SessionManager.loggedInAdminId = rs.getInt("id");
                SessionManager.loggedInAdminName = rs.getString("name");
                SessionManager.loggedInAdminEmail = rs.getString("email");

                AlertUtil.successAlert("Login successful!");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zcare/finalproject/adminDashboard.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) adminLoginBtn.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                AlertUtil.errorAlert("Invalid email or password.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
