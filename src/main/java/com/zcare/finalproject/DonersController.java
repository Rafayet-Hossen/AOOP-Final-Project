package com.zcare.finalproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DonersController {

    @FXML
    private Hyperlink createDonersAccount;

    @FXML
    private TextField donerEmail;

    @FXML
    private Button donerLoginBtn;

    @FXML
    private PasswordField donerPassword;

    @FXML
    private Pane mainpane;

    @FXML
    private StackPane parentsLoginPage;

    @FXML
    private ComboBox<?> selectUser;

    @FXML
    private Hyperlink switchToLogin;

    public void createDonersAccount() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("bloodDonerCreateAccount.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) createDonersAccount.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Create Parents Account");
        stage.setResizable(false);
        stage.show();
    }
    public void switchToLoginPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("donersLogin.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) switchToLogin.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Parents Login");
        stage.setResizable(false);
        stage.show();
    }


}
