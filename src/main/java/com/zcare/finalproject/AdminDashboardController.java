package com.zcare.finalproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {
    @FXML
    private Label activeDoctorsCount;
    @FXML
    private TableView<Doctor> doctorsInformationTable;
    @FXML private TableColumn<Doctor, Integer> id;
    @FXML private TableColumn<Doctor, String> name;
    @FXML private TableColumn<Doctor, String> email;
    @FXML private TableColumn<Doctor, String> phone;
    @FXML private TableColumn<Doctor, String> specialization;

    @FXML private TableView<Parent> parentsInformationTable;
    @FXML private TableColumn<Parent, Integer> parentsID;
    @FXML private TableColumn<Parent, String> parentsName;
    @FXML private TableColumn<Parent, String> parentsEmail;
    @FXML private TableColumn<Parent, String> parentsPhone;
    @FXML private TableColumn<Parent, String> parentsDistricts;
    @FXML
    private Label activeParentsCount;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        id.setCellValueFactory(cell -> cell.getValue().idProperty().asObject());
        name.setCellValueFactory(cell -> cell.getValue().nameProperty());
        email.setCellValueFactory(cell -> cell.getValue().emailProperty());
        phone.setCellValueFactory(cell -> cell.getValue().phoneProperty());
        specialization.setCellValueFactory(cell -> cell.getValue().specializationProperty());
        parentsID.setCellValueFactory(cell -> cell.getValue().idProperty().asObject());
        parentsName.setCellValueFactory(cell -> cell.getValue().nameProperty());
        parentsEmail.setCellValueFactory(cell -> cell.getValue().emailProperty());
        parentsPhone.setCellValueFactory(cell -> cell.getValue().phoneProperty());
        parentsDistricts.setCellValueFactory(cell -> cell.getValue().districtProperty());
    }

    public void loadDoctors() {

        ObservableList<Doctor> list = FXCollections.observableArrayList();
        int count = 0;

        String query = "SELECT id, email, name, phone, specialization FROM doctors";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Doctor(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("specialization")
                ));
                count++;
            }

            doctorsInformationTable.setItems(list);
            doctorsInformationTable.setVisible(true);
            parentsInformationTable.setVisible(false);
            activeDoctorsCount.setText(String.valueOf(count));

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Error loading doctors: " + e.getMessage());
        }
    }

    public void loadParents() {
        int count = 0;
        ObservableList<Parent> list = FXCollections.observableArrayList();

        String query = "SELECT id, email, name, phone, district FROM parents";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Parent(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("district")
                ));
                count++;
            }

            parentsInformationTable.setItems(list);
            parentsInformationTable.setVisible(true);
            doctorsInformationTable.setVisible(false);
            activeParentsCount.setText(String.valueOf(count));

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Error loading parents: " + e.getMessage());
        }
    }

}
