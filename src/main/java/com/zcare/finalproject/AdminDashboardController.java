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
import java.sql.SQLException;
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

    @FXML private TableView<Doner> donersInformationTable;
    @FXML private TableColumn<Doner, Integer> donerId;
    @FXML private TableColumn<Doner, String> donerName;
    @FXML private TableColumn<Doner, String> donerEmail;
    @FXML private TableColumn<Doner, String> donerPhone;
    @FXML private TableColumn<Doner, String> donerBloodGroup;
    @FXML private Label activeDonersCount;

    @FXML private TableView<BabySetter> settersInformationTable;
    @FXML private TableColumn<BabySetter, Integer> setterId;
    @FXML private TableColumn<BabySetter, String> setterName;
    @FXML private TableColumn<BabySetter, String> setterEmail;
    @FXML private TableColumn<BabySetter, String> setterPhone;
    @FXML private TableColumn<BabySetter, String> setterDistrict;
    @FXML private Label activeSettersCount;


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

        donerId.setCellValueFactory(cell -> cell.getValue().idProperty().asObject());
        donerName.setCellValueFactory(cell -> cell.getValue().nameProperty());
        donerEmail.setCellValueFactory(cell -> cell.getValue().emailProperty());
        donerPhone.setCellValueFactory(cell -> cell.getValue().phoneProperty());
        donerBloodGroup.setCellValueFactory(cell -> cell.getValue().bloodGroupProperty());

        setterId.setCellValueFactory(cell -> cell.getValue().idProperty().asObject());
        setterEmail.setCellValueFactory(cell -> cell.getValue().emailProperty());
        setterName.setCellValueFactory(cell -> cell.getValue().nameProperty());
        setterPhone.setCellValueFactory(cell -> cell.getValue().phoneProperty());
        setterDistrict.setCellValueFactory(cell -> cell.getValue().districtProperty());

        loadDoctors();
        loadParents();
        loadDoners();
        loadSetters();
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
            donersInformationTable.setVisible(false);
            settersInformationTable.setVisible(false);
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
            donersInformationTable.setVisible(false);
            settersInformationTable.setVisible(false);
            activeParentsCount.setText(String.valueOf(count));

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Error loading parents: " + e.getMessage());
        }
    }

    public void loadDoners(){
        int count = 0;
        ObservableList<Doner> list = FXCollections.observableArrayList();

        String query = "SELECT id, email, name, phone, blood_group FROM blood_donors";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery()){

            while (rs.next()) {
                list.add(new Doner(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("blood_group")
                ));
                count++;
            }
            donersInformationTable.setItems(list);
            donersInformationTable.setVisible(true);
            doctorsInformationTable.setVisible(false);
            parentsInformationTable.setVisible(false);
            settersInformationTable.setVisible(false);
            activeDonersCount.setText(String.valueOf(count));

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadSetters() {
        int count = 0;
        ObservableList<BabySetter> list = FXCollections.observableArrayList();

        String query = "SELECT id, email, name, phone, district FROM baby_setters";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new BabySetter(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("district")
                ));
                count++;
            }

            settersInformationTable.setItems(list);
            settersInformationTable.setVisible(true);
            doctorsInformationTable.setVisible(false);
            donersInformationTable.setVisible(false);
            parentsInformationTable.setVisible(false);
            activeSettersCount.setText(String.valueOf(count));

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Error loading parents: " + e.getMessage());
        }
    }

}
