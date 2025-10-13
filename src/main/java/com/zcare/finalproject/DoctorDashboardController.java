package com.zcare.finalproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class DoctorDashboardController implements Initializable {
    @FXML private TableColumn<AppointmentRow, Number> appointmentId;
    @FXML private TableColumn<AppointmentRow, String> babyName;
    @FXML private TableColumn<AppointmentRow, String> scheduledAt;
    @FXML private TableColumn<AppointmentRow, String> status;
    @FXML private TableColumn<AppointmentRow, String> parentsNote;
    @FXML private TableColumn<AppointmentRow, Void> actionCol;
    @FXML private TableView<AppointmentRow> doctorsAppointmentsTable;
    @FXML private AnchorPane pendingAppointmentsPane;
    @FXML
    private ComboBox<String> filteredByStatus;
    @FXML private DatePicker filteredByDate;
    @FXML private Button applyFilterBtn;
    @FXML private Button pendingAppointmentsBtn;
    @FXML private Label loginDoctorId;
    @FXML private Label loginDoctorName;

    @FXML private AnchorPane shareTipsPane;
    @FXML private TextField postTitle;
    @FXML private TextArea postDescription;
    @FXML private Button postBtn;
    @FXML private Button viewAllPostButton;

    @FXML private TextField docName;
    @FXML private TextField docEmail;
    @FXML private TextField docPhone;
    @FXML private TextArea docClinicAddress;
    @FXML private ComboBox<String> docDistrict;
    @FXML private Button docChooseProfilePicBtn;
    @FXML private ImageView docProfilePic;
    @FXML private Button saveChangesBtn;

    @FXML private Label dName;
    @FXML private Label dEmail;
    @FXML private Label dPhone;
    @FXML private Label dAddress;
    @FXML private Label dDistrict;

    @FXML private AnchorPane updateProfilePane;

    @FXML private AnchorPane previousAppointmentPane;

    @FXML private Button logoutBtn;



    public void initialize(URL location, ResourceBundle resources) {
        appointmentId.setCellValueFactory(data -> data.getValue().idProperty());
        babyName.setCellValueFactory(data -> data.getValue().babyNameProperty());
        scheduledAt.setCellValueFactory(data -> data.getValue().scheduledAtProperty());
        status.setCellValueFactory(data -> data.getValue().statusProperty());
        parentsNote.setCellValueFactory(data -> data.getValue().notesProperty());

        filteredByStatus.setItems(FXCollections.observableArrayList("ALL", "REQUESTED", "CONFIRMED", "CANCELLED", "COMPLETED"));
        filteredByStatus.getSelectionModel().select("ALL");

        if (docDistrict != null) {
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
            docDistrict.getItems().addAll(districts);
            docDistrict.setVisibleRowCount(5);
            docDistrict.getSelectionModel().clearSelection();
        }

        loadDoctorAppointments();
    }

    @FXML
    private void handleShareTipsView() {
        pendingAppointmentsPane.setVisible(false);
        previousAppointmentPane.setVisible(false);
        shareTipsPane.setVisible(true);
        updateProfilePane.setVisible(false);
    }

    @FXML
    private void handlePendingAppointmentsView() {
        pendingAppointmentsPane.setVisible(true);
        shareTipsPane.setVisible(false);
        previousAppointmentPane.setVisible(false);
        updateProfilePane.setVisible(false);
        loadDoctorAppointments();
    }

    @FXML
    private void handlePreviousAppointmentsView(){
        previousAppointmentPane.setVisible(true);
        pendingAppointmentsPane.setVisible(false);
        shareTipsPane.setVisible(false);
        updateProfilePane.setVisible(false);
    }
    @FXML
    private void handleUpdateProfileView(){
        previousAppointmentPane.setVisible(false);
        pendingAppointmentsPane.setVisible(false);
        shareTipsPane.setVisible(false);
        updateProfilePane.setVisible(true);
        loadDoctorProfile();
    }

    private void loadDoctorAppointments() {
        ObservableList<AppointmentRow> appointments = FXCollections.observableArrayList();

        loginDoctorId.setText(String.valueOf(SessionManager.loggedInDoctorId));
        loginDoctorName.setText(SessionManager.loggedInDoctorName);

        String baseSql = """
        SELECT 
            a.id,
            b.name AS baby_name,
            b.dob AS baby_dob,
            p.name AS parent_name,
            p.phone AS parent_phone,
            a.scheduled_at,
            a.status,
            a.notes
        FROM appointments a
        JOIN babies b ON a.baby_id = b.id
        JOIN parents p ON a.parent_id = p.id
        WHERE a.doctor_id = ?
    """;

        StringBuilder sql = new StringBuilder(baseSql);

        String selectedStatus = filteredByStatus.getValue();
        LocalDate selectedDate = filteredByDate.getValue();

        if (selectedStatus != null && !selectedStatus.equals("ALL")) {
            sql.append(" AND a.status = '").append(selectedStatus).append("'");
        }

        if (selectedDate != null) {
            sql.append(" AND DATE(a.scheduled_at) = '").append(selectedDate).append("'");
        }

        sql.append(" ORDER BY a.scheduled_at DESC");

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            ps.setInt(1, SessionManager.loggedInDoctorId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                appointments.add(new AppointmentRow(
                        rs.getInt("id"),
                        rs.getString("baby_name"),
                        rs.getString("parent_name"),
                        rs.getString("parent_phone"),
                        rs.getString("baby_dob"),
                        rs.getString("scheduled_at"),
                        rs.getString("status"),
                        rs.getString("notes") != null ? rs.getString("notes") : ""
                ));
            }

            doctorsAppointmentsTable.setItems(appointments);
            addActionButtons();

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Failed to load appointments.");
        }
    }



    private void addActionButtons() {
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button acceptBtn = new Button("Accept");
            private final Button cancelBtn = new Button("Cancel");
            private final Button completeBtn = new Button("Complete");

            {
                acceptBtn.setOnAction(e -> updateAppointmentStatus(getTableView().getItems().get(getIndex()).getId(), "CONFIRMED"));
                cancelBtn.setOnAction(e -> updateAppointmentStatus(getTableView().getItems().get(getIndex()).getId(), "CANCELLED"));
                completeBtn.setOnAction(e -> openCompletionPopup(getTableView().getItems().get(getIndex()).getId()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    AppointmentRow appt = getTableView().getItems().get(getIndex());
                    switch (appt.statusProperty().get()) {
                        case "REQUESTED" -> setGraphic(new HBox(5, acceptBtn, cancelBtn));
                        case "CONFIRMED" -> setGraphic(completeBtn);
                        default -> setGraphic(null);
                    }
                }
            }
        });
    }

    private void updateAppointmentStatus(int id, String newStatus) {
        String sql = "UPDATE appointments SET status = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, id);

            if (ps.executeUpdate() > 0) {
                AlertUtil.successAlert("Status updated to " + newStatus);
                loadDoctorAppointments();
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Failed to update status.");
        }
    }

    private void openCompletionPopup(int appointmentId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("completeAppointment.fxml"));
            Parent root = loader.load();

            CompleteAppointmentController controller = loader.getController();
            controller.setAppointmentId(appointmentId);

            Stage stage = new Stage();
            stage.setTitle("Complete Appointment");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();

            loadDoctorAppointments();

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Failed to open completion window.");
        }
    }

    @FXML
    private void handleApplyFilter() {
        loadDoctorAppointments();
    }

    @FXML
    private void handlePostTip() {
        String title = postTitle.getText().trim();
        String content = postDescription.getText().trim();
        int doctorId = SessionManager.loggedInDoctorId;

        if (title.isEmpty() || content.isEmpty()) {
            AlertUtil.errorAlert("Please enter both title and content before posting.");
            return;
        }

        String sql = "INSERT INTO tips (doctor_id, title, content) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, doctorId);
            ps.setString(2, title);
            ps.setString(3, content);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                AlertUtil.successAlert("Your tip has been posted successfully!");
                postTitle.clear();
                postDescription.clear();
            } else {
                AlertUtil.errorAlert("Failed to post your tip. Please try again.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Database Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleViewAllPosts() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("viewAllTips.fxml"));
            Parent root = loader.load();

            ViewAllTipsController controller = loader.getController();
            controller.setDoctorId(SessionManager.loggedInDoctorId);

            Stage stage = new Stage();
            stage.setTitle("My Posts");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Unable to open posts window.");
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("doctorsLogin.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Doctor Login");
            stage.show();

            AlertUtil.successAlert("You have been logged out successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Logout failed: " + e.getMessage());
        }
    }

    private void loadDoctorProfile() {
        String sql = "SELECT * FROM doctors WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, SessionManager.loggedInDoctorId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                docName.setText(rs.getString("name"));
                docEmail.setText(rs.getString("email"));
                docPhone.setText(rs.getString("phone"));
                docClinicAddress.setText(rs.getString("clinic_address"));
                docDistrict.setValue(rs.getString("district"));

                dName.setText(rs.getString("name"));
                dEmail.setText(rs.getString("email"));
                dPhone.setText(rs.getString("phone"));
                dAddress.setText(rs.getString("clinic_address"));
                dDistrict.setText(rs.getString("district"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Failed to load doctor profile.");
        }
    }

}
