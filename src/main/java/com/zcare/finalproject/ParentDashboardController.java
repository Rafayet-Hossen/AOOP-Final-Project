package com.zcare.finalproject;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ParentDashboardController implements Initializable {

    @FXML
    private TextArea alergiesNote;

    @FXML
    private Button appointmentsStatusBtn;

    @FXML
    private Button availableBabySetterBtn;

    @FXML
    private Button availableBloodDonersBtn;

    @FXML
    private Button availableDoctorsBtn;

    @FXML
    private ComboBox<String> babyBloodGroup;

    @FXML
    private DatePicker babyDOB;

    @FXML
    private Button babyDetailsBtn;

    @FXML
    private ComboBox<String> babyGender;

    @FXML
    private TextField babyHeight;

    @FXML
    private TextField babyName;

    @FXML
    private TextField babyWeight;

    @FXML
    private TableView<Baby> babyInformationTable;

    @FXML
    private TableView<Doctor> availableDoctorsTable;

    @FXML private TableColumn<Doctor, String> doctorsName;
    @FXML private TableColumn<Doctor, String> doctorsEmail;
    @FXML private TableColumn<Doctor, String> doctorsPhoneNo;
    @FXML private TableColumn<Doctor, String> clinicAddress;
    @FXML private TableColumn<Doctor, String> doctorSpecialization;
    @FXML private Button bookAppointmentBtn;

    @FXML
    private TableColumn<Baby,String> babygender;

    @FXML
    private TableColumn<Baby,Integer> babyid;

    @FXML
    private TableColumn<Baby,String> babyname;

    @FXML
    private TableColumn<Baby,String> bloodgroup;

    @FXML
    private TableColumn<Baby,String> parentemail;

    @FXML
    private TableColumn<Baby,String> parentname;

    @FXML
    private TableColumn<Baby,String> parentphone;

    @FXML
    private TableColumn<Baby,String> dateofbirth;

    @FXML
    private Button chatsBtn;

    @FXML
    private Label currentTimeLabel;

    @FXML
    private Button doctorsTipsBtn;

    @FXML
    private TextArea informationNote;

    @FXML
    private Label loginParentsName;

    @FXML
    private Button logoutBtn;

    @FXML
    private TextArea medicalConditionNote;

    @FXML
    private Button notificationsBtn;

    @FXML
    private Button submitFormBtn;

    @FXML
    private Button updateProfileBtn;

    @FXML
    private Label loginParentId;
    @FXML
    private AnchorPane availableDoctorsPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        babyGender.setItems(FXCollections.observableArrayList("Male", "Female", "Other"));
        babyBloodGroup.setItems(FXCollections.observableArrayList("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"));

        babyGender.getSelectionModel().clearSelection();
        babyBloodGroup.getSelectionModel().clearSelection();
        loadParentsInfo();
        loadBabyTable();
    }
    private void loadParentsInfo() {
        loginParentId.setText(String.valueOf(SessionManager.loggedInParentId));
        loginParentsName.setText(SessionManager.loggedInParentsName);
    }

    @FXML
    private void handleSubmitForm() {
        String name = babyName.getText().trim();
        LocalDate dob = babyDOB.getValue();
        String gender = babyGender.getValue();
        String bloodGroup = babyBloodGroup.getValue();
        String heightStr = babyHeight.getText().trim();
        String weightStr = babyWeight.getText().trim();
        String allergies = alergiesNote.getText().trim();
        String medical = medicalConditionNote.getText().trim();
        String notes = informationNote.getText().trim();

        if (name.isEmpty() || dob == null || gender == null || bloodGroup == null) {
            AlertUtil.errorAlert("Please fill all required fields: name, DOB, gender, blood group.");
            return;
        }

        try {
            double height = Double.parseDouble(heightStr);
            double weight = Double.parseDouble(weightStr);

            int parentId = SessionManager.loggedInParentId;

            String sql = "INSERT INTO babies (parent_id, name, dob, gender, blood_group, weight_kg, height_cm, allergies, medical_conditions, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, parentId);
                ps.setString(2, name);
                ps.setDate(3, java.sql.Date.valueOf(dob));
                ps.setString(4, gender);
                ps.setString(5, bloodGroup);
                ps.setDouble(6, weight);
                ps.setDouble(7, height);
                ps.setString(8, allergies);
                ps.setString(9, medical);
                ps.setString(10, notes);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    AlertUtil.successAlert("Baby added successfully!");
                    loadBabyTable();
                    clearForm();
                } else {
                    AlertUtil.errorAlert("Failed to add baby.");
                }
            }

        } catch (NumberFormatException e) {
            AlertUtil.errorAlert("Invalid height or weight. Please enter valid numbers.");
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Error: " + e.getMessage());
        }
    }

    private void loadBabyTable() {
        ObservableList<Baby> list = FXCollections.observableArrayList();

        String sql = "SELECT babies.id, babies.name, babies.dob, babies.gender, babies.blood_group, " +
                "parents.name AS parent_name, parents.email AS parent_email, parents.phone AS parent_phone " +
                "FROM babies JOIN parents ON babies.parent_id = parents.id WHERE parents.id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, SessionManager.loggedInParentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Baby(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("dob"),
                        rs.getString("gender"),
                        rs.getString("blood_group"),
                        rs.getString("parent_name"),
                        rs.getString("parent_email"),
                        rs.getString("parent_phone")
                ));

            }

            babyid.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().idProperty().get()).asObject());
            babyname.setCellValueFactory(cell -> cell.getValue().nameProperty());
            dateofbirth.setCellValueFactory(cell -> cell.getValue().dobProperty());
            babygender.setCellValueFactory(cell -> cell.getValue().genderProperty());
            bloodgroup.setCellValueFactory(cell -> cell.getValue().bloodGroupProperty());
            parentname.setCellValueFactory(cell -> cell.getValue().parentNameProperty());
            parentemail.setCellValueFactory(cell -> cell.getValue().parentEmailProperty());
            parentphone.setCellValueFactory(cell -> cell.getValue().parentPhoneProperty());

            babyInformationTable.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Failed to load baby data.");
        }
    }

    @FXML
    private void loadDoctorTable() {
        ObservableList<Doctor> doctorList = FXCollections.observableArrayList();
        availableDoctorsPane.setVisible(true);
        String sql = "SELECT id, name, email, phone, clinic_address, specialization FROM doctors";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                doctorList.add(new Doctor(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("specialization"),
                        rs.getString("clinic_address")
                ));
            }

            doctorsName.setCellValueFactory(cell -> cell.getValue().nameProperty());
            doctorsEmail.setCellValueFactory(cell -> cell.getValue().emailProperty());
            doctorsPhoneNo.setCellValueFactory(cell -> cell.getValue().phoneProperty());
            clinicAddress.setCellValueFactory(cell -> cell.getValue().clinicAddressProperty());
            doctorSpecialization.setCellValueFactory(cell -> cell.getValue().specializationProperty());

            availableDoctorsTable.setItems(doctorList);
            availableDoctorsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

            bookAppointmentBtn.setOnAction(e -> openAppointmentForm());

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Failed to load doctor data.");
        }
    }

    private void openAppointmentForm() {
        Doctor selected = availableDoctorsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.errorAlert("Please select a doctor from the table.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("bookAppointment.fxml"));
            Parent root = loader.load();

            BookAppointmentController controller = loader.getController();
            controller.setDoctor(selected.getId()); // pass selected doctor ID

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Book Appointment");
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Failed to open appointment form.");
        }
    }


    private void clearForm() {
        babyName.clear();
        babyDOB.setValue(null);
        babyGender.getSelectionModel().clearSelection();
        babyBloodGroup.getSelectionModel().clearSelection();
        babyHeight.clear();
        babyWeight.clear();
        alergiesNote.clear();
        medicalConditionNote.clear();
        informationNote.clear();
    }

}
