package com.zcare.finalproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BabySetterController extends PageUtil implements Initializable {

    @FXML
    private TextField babySetterEmail;

    @FXML
    private Button babySetterLoginBtn;

    @FXML
    private PasswordField babySetterPassword;

    @FXML
    private Hyperlink createBabySetterAccount;

    @FXML
    private Pane mainpane;

    @FXML
    private StackPane parentsLoginPage;

    @FXML
    private ComboBox<String> selectUser;

    @FXML
    private Hyperlink switchToLogin;

    public void createBabySetterAccount() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("babySettersCreateAccount.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) createBabySetterAccount.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Create Parents Account");
        stage.setResizable(false);
        stage.show();
    }
    public void switchToLoginPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("babySetters.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) switchToLogin.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Parents Login");
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PageUtil.populateUsers(selectUser);
        selectUser.getSelectionModel().select("BabySetter");
        selectUser.setOnAction(event -> {
            String selected = selectUser.getSelectionModel().getSelectedItem();
            if(!"BabySetter".equals(selected)){
                try {
                    PageUtil.switchUser(selectUser);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
