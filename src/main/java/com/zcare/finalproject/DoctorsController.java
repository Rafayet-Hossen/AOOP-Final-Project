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
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class DoctorsController extends PageUtil implements Initializable {

    @FXML
    private Button createDoctorAccountBtn;

    @FXML
    private Hyperlink createDoctorAccount;

    @FXML
    private TextField doctorClinicAddress;

    @FXML
    private TextField doctorEmail;

    @FXML
    private TextField doctorFullName;

    @FXML
    private PasswordField doctorPassword;

    @FXML
    private TextField doctorPhoneNo;

    @FXML
    private ComboBox<String> doctorSpecialization;

    @FXML
    private Button doctorLoginAccountBtn;

    @FXML
    private TextField doctorLoginEmail;

    @FXML
    private PasswordField doctorLoginPassword;

    @FXML
    private ComboBox<String> selectUser;

    @FXML
    private Hyperlink switchToLogin;

    public void createDoctorAccount() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("doctorsCreateAccount.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) createDoctorAccount.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Create Blood Doner Account");
        stage.setResizable(false);
        stage.show();
    }
    public void switchToLoginPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("doctorsLogin.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) switchToLogin.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Parents Login");
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (doctorSpecialization != null) {
            doctorSpecialization.getItems().addAll(
                    "General Physician", "Cardiologist", "Dermatologist", "Pediatrician",
                    "Gynecologist", "Dentist", "ENT Specialist", "Orthopedic", "Neurologist",
                    "Psychiatrist", "Surgeon", "Urologist", "Ophthalmologist"
            );
            doctorSpecialization.setVisibleRowCount(5);
            doctorSpecialization.getSelectionModel().clearSelection();
        }

        if (selectUser != null) {
            PageUtil.populateUsers(selectUser);
            selectUser.getSelectionModel().select("Doctor");
            selectUser.setOnAction(event -> {
                String selected = selectUser.getSelectionModel().getSelectedItem();
                if (!"Doctor".equals(selected)) {
                    try {
                        PageUtil.switchUser(selectUser);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    public void doctorRegister() {
        String email = doctorEmail.getText().trim();
        String password = doctorPassword.getText();
        String name = doctorFullName.getText().trim();
        String phone = doctorPhoneNo.getText().trim();
        String clinic = doctorClinicAddress.getText().trim();
        String specialization = doctorSpecialization.getValue();

        if (email.isEmpty() || password.isEmpty() || name.isEmpty()
                || phone.isEmpty() || clinic.isEmpty() || specialization == null) {
            AlertUtil.errorAlert("Please fill in all fields.");
            return;
        }

        if (password.length() < 8) {
            AlertUtil.errorAlert("Password must be at least 8 characters.");
            return;
        }

        String checkEmail = "SELECT * FROM doctors WHERE email = ?";
        String insert = "INSERT INTO doctors (email, password, name, phone, specialization, clinic_address) VALUES (?, ?, ?, ?, ?, ?)";

        try (var con = DBConnection.getConnection();
             var psCheck = con.prepareStatement(checkEmail)) {

            psCheck.setString(1, email);
            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                AlertUtil.errorAlert("Email already exists.");
                return;
            }

            try (var psInsert = con.prepareStatement(insert)) {
                psInsert.setString(1, email);
                psInsert.setString(2, password);
                psInsert.setString(3, name);
                psInsert.setString(4, phone);
                psInsert.setString(5, specialization);
                psInsert.setString(6, clinic);

                int rows = psInsert.executeUpdate();
                if (rows > 0) {
                    AlertUtil.successAlert("Account created successfully!");

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("doctorsLogin.fxml"));
                    Parent root = fxmlLoader.load();
                    Stage stage = (Stage) createDoctorAccountBtn.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Doctor Login");
                    stage.setResizable(false);
                    stage.show();
                } else {
                    AlertUtil.errorAlert("Failed to create account.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Database error: " + e.getMessage());
        }
    }

    public void doctorLogin() {
        String email = doctorLoginEmail.getText().trim();
        String password = doctorLoginPassword.getText();

        if (email.isEmpty() || password.isEmpty()) {
            AlertUtil.errorAlert("Please enter both email and password.");
            return;
        }

        String query = "SELECT * FROM doctors WHERE email = ? AND password = ?";

        try (var con = DBConnection.getConnection();
             var ps = con.prepareStatement(query)) {

            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                AlertUtil.successAlert("Login successful!");

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("doctorDashboard.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = (Stage) doctorLoginAccountBtn.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Doctor Dashboard");
                stage.setResizable(false);
                stage.show();
            } else {
                AlertUtil.errorAlert("Invalid credentials.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Database error: " + e.getMessage());
        }
    }

}
