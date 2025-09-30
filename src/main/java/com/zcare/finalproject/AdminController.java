package com.zcare.finalproject;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.net.URL;
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

    @FXML
    private Label sidepane;

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
}
