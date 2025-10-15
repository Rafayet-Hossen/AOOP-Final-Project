package com.zcare.finalproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
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

    @FXML private TableView<Parents> parentsInformationTable;
    @FXML private TableColumn<Parents, Integer> parentsID;
    @FXML private TableColumn<Parents, String> parentsName;
    @FXML private TableColumn<Parents, String> parentsEmail;
    @FXML private TableColumn<Parents, String> parentsPhone;
    @FXML private TableColumn<Parents, String> parentsDistricts;
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

    @FXML private TableView<Baby> babiesInformationTable;
    @FXML private TableColumn<Baby, Integer> babyId;
    @FXML private TableColumn<Baby, String> babyName;
    @FXML private TableColumn<Baby, String> babyParentsName;
    @FXML private TableColumn<Baby, String> babyParentsPhoneNo;
    @FXML private TableColumn<Baby, String> babyDOB;
    @FXML private Label activeBabiesCount;


    @FXML private Button logoutBtn;
    @FXML private Label loginAdminId;
    @FXML private Label loginAdminName;
    @FXML private Label adminUsername;
    @FXML private Label informationOf;

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

        loadAdminInfo();
        loadDoctors();
        loadParents();
        loadDoners();
        loadSetters();
        loadBabies();
    }

    private void loadAdminInfo() {
        loginAdminId.setText(String.valueOf(SessionManager.loggedInAdminId));
        loginAdminName.setText(SessionManager.loggedInAdminName);
        adminUsername.setText(SessionManager.loggedInAdminName);
    }

    @FXML
    private void logout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("parentsLogin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Parent Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Failed to load login page.");
        }
    }


    public void loadDoctors() {

        ObservableList<Doctor> list = FXCollections.observableArrayList();
        int count = 0;

        String query = "SELECT id, email, name, phone, specialization,clinic_address FROM doctors";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Doctor(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("specialization"),
                        rs.getString("clinic_address")
                ));
                count++;
            }

            doctorsInformationTable.setItems(list);
            doctorsInformationTable.setVisible(true);
            parentsInformationTable.setVisible(false);
            donersInformationTable.setVisible(false);
            settersInformationTable.setVisible(false);
            babiesInformationTable.setVisible(false);
            informationOf.setText("Information of Doctors");
            activeDoctorsCount.setText(String.valueOf(count));

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Error loading doctors: " + e.getMessage());
        }
    }

    public void loadParents() {
        int count = 0;
        ObservableList<Parents> list = FXCollections.observableArrayList();

        String query = "SELECT id, email, name, phone, district FROM parents";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Parents(
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
            babiesInformationTable.setVisible(false);
            informationOf.setText("Information of Parents");
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
            babiesInformationTable.setVisible(false);
            informationOf.setText("Information of Doners");
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
            babiesInformationTable.setVisible(false);
            informationOf.setText("Information of Baby Setters");
            activeSettersCount.setText(String.valueOf(count));

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Error loading parents: " + e.getMessage());
        }
    }

    public void loadBabies() {
        int count = 0;
        ObservableList<Baby> list = FXCollections.observableArrayList();
        String sql = """
        SELECT 
            b.id, b.name, b.dob,
            p.name AS parent_name,
            p.phone AS parent_phone
        FROM babies b
        JOIN parents p ON b.parent_id = p.id
    """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Baby(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("parent_name"),
                        rs.getString("parent_phone"),
                        rs.getString("dob")
                ));
                count++;
            }

            babyId.setCellValueFactory(cell -> cell.getValue().idProperty().asObject());
            babyName.setCellValueFactory(cell -> cell.getValue().nameProperty());
            babyParentsName.setCellValueFactory(cell -> cell.getValue().parentNameProperty());
            babyParentsPhoneNo.setCellValueFactory(cell -> cell.getValue().parentPhoneProperty());
            babyDOB.setCellValueFactory(cell -> cell.getValue().dobProperty());

            babiesInformationTable.setItems(list);

            babiesInformationTable.setVisible(true);
            doctorsInformationTable.setVisible(false);
            parentsInformationTable.setVisible(false);
            donersInformationTable.setVisible(false);
            settersInformationTable.setVisible(false);
            activeBabiesCount.setText(String.valueOf(count));
            informationOf.setText("Information of Babies");

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Failed to load babies data: " + e.getMessage());
        }
    }


}
