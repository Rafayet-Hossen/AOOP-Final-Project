package com.zcare.finalproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class BabySetterController extends PageUtil implements Initializable {

    @FXML
    private TextField babySetterAddress;

    @FXML
    private ComboBox<String> babySetterDistrict;

    @FXML
    private TextField babySetterEmail;

    @FXML
    private TextField babySetterFullName;

    @FXML
    private TextField babySetterHourlyRate;

    @FXML
    private PasswordField babySetterPassword;

    @FXML
    private TextField babySetterPhoneNo;

    @FXML
    private Button createBabySetterAccountBtn;

    @FXML
    private Button babySetterLoginBtn;

    @FXML
    private TextField babySetterLoginEmail;

    @FXML
    private PasswordField babySetterLoginPassword;

    @FXML
    private Hyperlink createBabySetterAccount;

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
        if (babySetterDistrict != null) {
            babySetterDistrict.getItems().addAll(
                    "Bagerhat", "Bandarban", "Barguna", "Barisal", "Bhola", "Bogura", "Brahmanbaria",
                    "Chandpur", "Chapai Nawabganj", "Chattogram", "Chuadanga", "Comilla", "Cox's Bazar",
                    "Dhaka", "Dinajpur", "Faridpur", "Feni", "Gaibandha", "Gazipur", "Gopalganj",
                    "Habiganj", "Jamalpur", "Jashore", "Jhalokathi", "Jhenaidah", "Joypurhat", "Khagrachari",
                    "Khulna", "Kishoreganj", "Kurigram", "Kushtia", "Lakshmipur", "Lalmonirhat", "Madaripur",
                    "Magura", "Manikganj", "Meherpur", "Moulvibazar", "Munshiganj", "Mymensingh", "Naogaon",
                    "Narail", "Narayanganj", "Narsingdi", "Natore", "Netrokona", "Nilphamari", "Noakhali",
                    "Pabna", "Panchagarh", "Patuakhali", "Pirojpur", "Rajbari", "Rajshahi", "Rangamati",
                    "Rangpur", "Satkhira", "Shariatpur", "Sherpur", "Sirajganj", "Sunamganj", "Sylhet",
                    "Tangail", "Thakurgaon"
            );
            babySetterDistrict.setVisibleRowCount(5);
            babySetterDistrict.getSelectionModel().clearSelection();
        }
        if(selectUser != null){
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

    public void babySetterRegister() {
        String email = babySetterEmail.getText().trim();
        String password = babySetterPassword.getText();
        String name = babySetterFullName.getText().trim();
        String phone = babySetterPhoneNo.getText().trim();
        String address = babySetterAddress.getText().trim();
        String district = babySetterDistrict.getValue();
        String hourlyRateStr = babySetterHourlyRate.getText().trim();

        if (email.isEmpty() || password.isEmpty() || name.isEmpty()
                || phone.isEmpty() || address.isEmpty() || district == null || hourlyRateStr.isEmpty()) {
            AlertUtil.errorAlert("Please fill in all fields.");
            return;
        }

        if (password.length() < 8) {
            AlertUtil.errorAlert("Password must be at least 8 characters long.");
            return;
        }

        double hourlyRate;
        try {
            hourlyRate = Double.parseDouble(hourlyRateStr);
        } catch (NumberFormatException e) {
            AlertUtil.errorAlert("Hourly rate must be a number.");
            return;
        }

        String checkEmail = "SELECT * FROM baby_setters WHERE email = ?";
        String insert = "INSERT INTO baby_setters (email, password, name, phone, address, district, hourly_rate) VALUES (?, ?, ?, ?, ?, ?, ?)";

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
                psInsert.setString(5, address);
                psInsert.setString(6, district);
                psInsert.setDouble(7, hourlyRate);

                int rows = psInsert.executeUpdate();
                if (rows > 0) {
                    AlertUtil.successAlert("Account created successfully!");

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("babySetters.fxml"));
                    Parent root = fxmlLoader.load();
                    Stage stage = (Stage) createBabySetterAccountBtn.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Baby Setter Login");
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

    public void babySetterLogin() {
        String email = babySetterLoginEmail.getText().trim();
        String password = babySetterLoginPassword.getText();

        if (email.isEmpty() || password.isEmpty()) {
            AlertUtil.errorAlert("Please enter both email and password.");
            return;
        }

        String query = "SELECT * FROM baby_setters WHERE email = ? AND password = ?";

        try (var con = DBConnection.getConnection();
             var ps = con.prepareStatement(query)) {

            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                AlertUtil.successAlert("Login successful!");

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("babySetterDashboard.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = (Stage) babySetterLoginBtn.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Baby Setter Dashboard");
                stage.setResizable(false);
                stage.show();
            } else {
                AlertUtil.errorAlert("Invalid email or password.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Database error: " + e.getMessage());
        }
    }

}
