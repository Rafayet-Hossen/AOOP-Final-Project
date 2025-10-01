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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ParentsController extends PageUtil implements Initializable {

    @FXML
    private Hyperlink parentsCreateAccount;

    @FXML
    private TextField parentsEmail;

    @FXML
    private Button parentsLoginBtn;

    @FXML
    private PasswordField parentsPassword;

    @FXML
    private ComboBox<String> selectUser;

    @FXML
    private TextField parentAddress;

    @FXML
    private Button parentCreateAccountBtn;

    @FXML
    private ComboBox<String> parentDistrict;

    @FXML
    private TextField parentEmail;

    @FXML
    private TextField parentFullName;

    @FXML
    private PasswordField parentPassword;

    @FXML
    private TextField parentPhoneNo;


    @FXML
    private Hyperlink switchToLogin;

    private DBConnection connections;
    private PreparedStatement ps;
    private ResultSet rs;

    public void createParentsAccount() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("parentsCreateAccount.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) parentsCreateAccount.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Create Parents Account");
        stage.setResizable(false);
        stage.show();
    }
    public void switchToLoginPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("parentsLogin.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) switchToLogin.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Parents Login");
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (parentDistrict != null) {
            List<String> districts = Arrays.asList(
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
            parentDistrict.getItems().addAll(districts);
            parentDistrict.setVisibleRowCount(5);
            parentDistrict.getSelectionModel().clearSelection();
        }

        if (selectUser != null) {
            PageUtil.populateUsers(selectUser);
            selectUser.getSelectionModel().select("Parent");

            selectUser.setOnAction(event -> {
                String selected = selectUser.getSelectionModel().getSelectedItem();
                if (!"Parent".equals(selected)) {
                    try {
                        PageUtil.switchUser(selectUser);
                    } catch (Exception e) {
                        e.printStackTrace();
                        AlertUtil.errorAlert("Failed to switch user: " + e.getMessage());
                    }
                }
            });
        }
    }


    public void parentRegister() {
        String Email = parentEmail.getText().trim();
        String Password = parentPassword.getText();
        String Name = parentFullName.getText().trim();
        String Phone = parentPhoneNo.getText().trim();
        String Add = parentAddress.getText().trim();
        String Dist = parentDistrict.getValue();

        if (Email.isEmpty() || Password.isEmpty() || Name.isEmpty()
                || Phone.isEmpty() || Add.isEmpty() || Dist == null) {
            AlertUtil.errorAlert("Please fill in all fields.");
            return;
        }

        if (Password.length() < 8) {
            AlertUtil.errorAlert("Password must be at least 8 characters long.");
            return;
        }

        String checkEmail = "SELECT * FROM parents WHERE email = ?";
        String insertParent = "INSERT INTO parents (email, password, name, phone, address, district) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement psCheck = con.prepareStatement(checkEmail)) {

            psCheck.setString(1, Email);
            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                AlertUtil.errorAlert("Email already exists");
                return;
            }

            try (PreparedStatement psInsert = con.prepareStatement(insertParent)) {
                psInsert.setString(1, Email);
                psInsert.setString(2, Password);
                psInsert.setString(3, Name);
                psInsert.setString(4, Phone);
                psInsert.setString(5, Add);
                psInsert.setString(6, Dist);

                int rowsInserted = psInsert.executeUpdate();

                if (rowsInserted > 0) {
                    AlertUtil.successAlert("Account created successfully! Redirecting to login...");

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("parentsLogin.fxml"));
                    Parent root = fxmlLoader.load();
                    Stage stage = (Stage) parentCreateAccountBtn.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Parent Login");
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

    public void parentLogin() {
        String email = parentsEmail.getText().trim();
        String password = parentsPassword.getText();

        if (email.isEmpty() || password.isEmpty()) {
            AlertUtil.errorAlert("Please fill in all fields.");
            return;
        }

        String query = "SELECT * FROM parents WHERE email = ? AND password = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                AlertUtil.successAlert("Login successful!");

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("parentsDashboard.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = (Stage) parentsLoginBtn.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Parent Dashboard");
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
