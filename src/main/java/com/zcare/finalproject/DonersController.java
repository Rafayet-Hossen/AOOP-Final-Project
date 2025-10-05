package com.zcare.finalproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class DonersController extends PageUtil implements Initializable {

    @FXML
    private Button createDonersAccountBtn;

    @FXML
    private TextField donerAddress;

    @FXML
    private ComboBox<String> donerBloodGroup;

    @FXML
    private ComboBox<String> donerDistrict;

    @FXML
    private TextField donerEmail;

    @FXML
    private TextField donerFullName;

    @FXML
    private PasswordField donerPassword;

    @FXML
    private TextField donerPhoneNo;

    @FXML
    private DatePicker lastDonationDate;

    @FXML
    private Hyperlink createDonersAccount;

    @FXML
    private Button donerLoginAccountBtn;

    @FXML
    private TextField donerLoginEmail;

    @FXML
    private PasswordField donerLoginPassword;


    @FXML
    private ComboBox<String> selectUser;

    @FXML
    private Hyperlink switchToLogin;


    public void createDonersAccount() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("bloodDonerCreateAccount.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) createDonersAccount.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Create Doner Account");
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (donerBloodGroup != null) {
            donerBloodGroup.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
            donerBloodGroup.setVisibleRowCount(5);
            donerBloodGroup.getSelectionModel().clearSelection();
        }

        if (donerDistrict != null) {
            donerDistrict.getItems().addAll(
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
            donerDistrict.setVisibleRowCount(5);
            donerDistrict.getSelectionModel().clearSelection();
        }
        if(selectUser != null) {
            PageUtil.populateUsers(selectUser);
            selectUser.getSelectionModel().select("Doner");

            selectUser.setOnAction(event -> {
                String selected = selectUser.getSelectionModel().getSelectedItem();
                if (!"Doner".equals(selected)) {
                    try {
                        PageUtil.switchUser(selectUser);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    public void donerRegister() {
        String email = donerEmail.getText().trim();
        String password = donerPassword.getText();
        String name = donerFullName.getText().trim();
        String phone = donerPhoneNo.getText().trim();
        String address = donerAddress.getText().trim();
        String bloodGroup = donerBloodGroup.getValue();
        String district = donerDistrict.getValue();
        java.sql.Date donationDate = lastDonationDate.getValue() != null
                ? java.sql.Date.valueOf(lastDonationDate.getValue())
                : null;

        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty() ||
                address.isEmpty() || bloodGroup == null || district == null || donationDate == null) {
            AlertUtil.errorAlert("Please fill in all fields.");
            return;
        }

        if (password.length() < 8) {
            AlertUtil.errorAlert("Password must be at least 8 characters.");
            return;
        }

        String checkEmail = "SELECT * FROM blood_donors WHERE email = ?";
        String insert = "INSERT INTO blood_donors (email, password, name, phone, address, blood_group, district, last_donation_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

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
                psInsert.setString(6, bloodGroup);
                psInsert.setString(7, district);
                psInsert.setDate(8, donationDate);

                int inserted = psInsert.executeUpdate();
                if (inserted > 0) {
                    AlertUtil.successAlert("Account created successfully!");

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("donersLogin.fxml"));
                    Parent root = fxmlLoader.load();
                    Stage stage = (Stage) createDonersAccountBtn.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Doner Login");
                    stage.setResizable(false);
                    stage.show();
                } else {
                    AlertUtil.errorAlert("Account creation failed.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Database error: " + e.getMessage());
        }
    }

    public void donerLogin() {
        String email = donerLoginEmail.getText().trim();
        String password = donerLoginPassword.getText();

        if (email.isEmpty() || password.isEmpty()) {
            AlertUtil.errorAlert("Please enter both email and password.");
            return;
        }

        String query = "SELECT * FROM blood_donors WHERE email = ? AND password = ?";

        try (var con = DBConnection.getConnection();
             var ps = con.prepareStatement(query)) {

            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                AlertUtil.successAlert("Login successful!");
                SessionManager.loggedInDonorId = rs.getInt("id");
                SessionManager.loggedInDonorName = rs.getString("name");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("donerDashboard.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = (Stage) donerLoginAccountBtn.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Doner Dashboard");
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
