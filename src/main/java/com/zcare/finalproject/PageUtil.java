package com.zcare.finalproject;

import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;

public class PageUtil {
    public static void populateUsers(ComboBox<String> comboBox){
        comboBox.setItems(FXCollections.observableArrayList( "Parent","Doctor","Doner","BabySetter","Admin"));
        comboBox.getSelectionModel().selectFirst();
    }
    public static ComboBox<String> switchUser(ComboBox<String> comboBox){
        String selectedUser = comboBox.getSelectionModel().getSelectedItem();
        Users.selectedUser = selectedUser;
        String fxmlFile = "";
        String Title = "";

        switch(selectedUser){
            case "Parent":
                fxmlFile = "parentsLogin.fxml";
                Title = "Parents Login";
                break;
            case "Doctor":
                fxmlFile = "doctorsLogin.fxml";
                Title = "Doctor Login";
                break;
            case "Doner":
                fxmlFile = "donersLogin.fxml";
                Title = "Doners Login";
                break;
            case "BabySetter":
                fxmlFile = "babySetters.fxml";
                Title = "Baby Setter Login";
                break;
            case "Admin":
                fxmlFile = "adminLogin.fxml";
                Title = "Admin Login";
                break;
            default:
                AlertUtil.errorAlert("Please select a valid user type.");
                return comboBox;
        }
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(PageUtil.class.getResource(fxmlFile));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) comboBox.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(Title);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return comboBox;
    }
}
