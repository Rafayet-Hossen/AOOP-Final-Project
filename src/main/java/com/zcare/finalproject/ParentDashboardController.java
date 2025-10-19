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
import javafx.scene.layout.VBox;
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
    private Button submitFormBtn;

    @FXML
    private Button updateProfileBtn;

    @FXML
    private Label loginParentId;
    @FXML
    private AnchorPane availableDoctorsPane;
    @FXML
    private AnchorPane babyInformationPane;

    @FXML TextField requestTitle;
    @FXML TextField locationName;
    @FXML DatePicker fromDatePicker;
    @FXML DatePicker toDatePeaker;
    @FXML TextArea messageToDoner;
    @FXML Button submitForm;

    @FXML private AnchorPane doctorTipsPane;
    @FXML private VBox tipsContainer;
    @FXML private ScrollPane tipsScrollPane;

    @FXML TableView<BloodRequest>bloodRequestTable;
    @FXML TableColumn<BloodRequest,Integer>serialNo;
    @FXML TableColumn<BloodRequest,String>parentName;
    @FXML TableColumn<BloodRequest,String>acceptorName;
    @FXML TableColumn<BloodRequest,String>acceptorPhoneNo;
    @FXML TableColumn<BloodRequest,String>createdTime;
    @FXML TableColumn<BloodRequest,String>requestStatus;
    @FXML TableColumn<BloodRequest, Void> completeCol;
    @FXML
    private AnchorPane bloodRequestPane;

    @FXML private AnchorPane babySetterRequestPane;

    @FXML private TextField setterRequestTitle;
    @FXML private TextField setterLocationName;
    @FXML private DatePicker fromDatePickerSetter;
    @FXML private DatePicker toDatePeakerSetter;
    @FXML private TextArea messageToSetter;
    @FXML private Button submitSetterRequestForm;


    @FXML private TableView<BabySetterRequest> setterRequestTable;
    @FXML private TableColumn<BabySetterRequest, Integer> setterSerialNo;
    @FXML private TableColumn<BabySetterRequest, String> requestParentName;
    @FXML private TableColumn<BabySetterRequest, String> setterAcceptedBy;
    @FXML private TableColumn<BabySetterRequest, String> setterAcceptedByNo;
    @FXML private TableColumn<BabySetterRequest, String> setterRequestCreatedAt;
    @FXML private TableColumn<BabySetterRequest, String> setterRequestStatus;
    @FXML private TableColumn<BabySetterRequest, Void> setterAction;

    @FXML private AnchorPane emergencyContactPane;
    @FXML private TableView<EmergencyContact> emergencyContactTable;
    @FXML private TableColumn<EmergencyContact, Integer> serviceId;
    @FXML private TableColumn<EmergencyContact, String> serviceName;
    @FXML private TableColumn<EmergencyContact, String> serviceContactNo;
    @FXML private TableColumn<EmergencyContact, String> serviceType;
    @FXML private TableColumn<EmergencyContact, String> serviceLocation;
    @FXML private TableColumn<EmergencyContact, String> serviceAddress;

    @FXML private ComboBox<String> filterByLocation;
    @FXML private ComboBox<String> filterByType;

    @FXML private AnchorPane appointmentStatusPane;
    @FXML private TableView<AppointmentStatusRow> appointmentStatusTable;
    @FXML private TableColumn<AppointmentStatusRow, Integer> appointmentId;
    @FXML private TableColumn<AppointmentStatusRow, String> appointmentBabyName;
    @FXML private TableColumn<AppointmentStatusRow, String> doctorName;
    @FXML private TableColumn<AppointmentStatusRow, String> scheduledAt;
    @FXML private TableColumn<AppointmentStatusRow, String> status;
    @FXML private TableColumn<AppointmentStatusRow, String> doctorNote;


    private ObservableList<EmergencyContact> emergencyList = FXCollections.observableArrayList();



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        babyGender.setItems(FXCollections.observableArrayList("Male", "Female", "Other"));
        babyBloodGroup.setItems(FXCollections.observableArrayList("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"));
        serialNo.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        parentName.setCellValueFactory(data -> data.getValue().parentNameProperty());
        acceptorName.setCellValueFactory(data -> data.getValue().acceptedByNameProperty());
        acceptorPhoneNo.setCellValueFactory(data -> data.getValue().acceptedByPhoneProperty());
        createdTime.setCellValueFactory(data -> data.getValue().createdAtProperty());
        requestStatus.setCellValueFactory(data -> data.getValue().statusProperty());
        setterSerialNo.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        requestParentName.setCellValueFactory(data -> data.getValue().parentNameProperty());
        setterAcceptedBy.setCellValueFactory(data -> data.getValue().acceptedByNameProperty());
        setterAcceptedByNo.setCellValueFactory(data -> data.getValue().acceptedByPhoneProperty());
        setterRequestCreatedAt.setCellValueFactory(data -> data.getValue().createdAtProperty());
        setterRequestStatus.setCellValueFactory(data -> data.getValue().statusProperty());

        setterAction.setCellFactory(col -> new TableCell<>() {
            private final Button completeBtn = new Button("Completed");

            {
                completeBtn.setOnAction(event -> {
                    BabySetterRequest request = getTableView().getItems().get(getIndex());
                    markRequestCompleted(request.getId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : completeBtn);
            }
        });


        completeCol.setCellFactory(tc -> new TableCell<>() {
            private final Button btn = new Button("Complete");

            {
                btn.setOnAction(event -> {
                    BloodRequest req = getTableView().getItems().get(getIndex());
                    completeBloodRequest(req.getId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty || !getTableView().getItems().get(getIndex()).getStatus().equals("ACCEPTED") ? null : btn);
            }
        });

        bloodRequestTable.setRowFactory(tv -> {
            TableRow<BloodRequest> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    BloodRequest selectedRequest = row.getItem();
                    openBloodRequestDetailsPopup(selectedRequest.getId());
                }
            });
            return row;
        });

        setterRequestTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !setterRequestTable.getSelectionModel().isEmpty()) {
                BabySetterRequest selected = setterRequestTable.getSelectionModel().getSelectedItem();
                openSetterRequestDetails(selected.getId());
            }
        });


        babyGender.getSelectionModel().clearSelection();
        babyBloodGroup.getSelectionModel().clearSelection();
        loadParentsInfo();
        loadBabyTable();
    }

    private void showOnlyPane(AnchorPane paneToShow) {
        babyInformationPane.setVisible(false);
        availableDoctorsPane.setVisible(false);
        bloodRequestPane.setVisible(false);
        babySetterRequestPane.setVisible(false);
        emergencyContactPane.setVisible(false);
        appointmentStatusPane.setVisible(false);
        doctorTipsPane.setVisible(false);

        if (paneToShow != null) {
            paneToShow.setVisible(true);
        }
    }

    @FXML
    private void handleBabyDetailsView() {
        showOnlyPane(babyInformationPane);
        loadBabyTable();
    }

    @FXML
    private void handleAvailableDoctorsView() {
        showOnlyPane(availableDoctorsPane);
        loadDoctorTable();
    }

    @FXML
    private void handleAvailableBloodDonersView() {
        showOnlyPane(bloodRequestPane);
        loadMyBloodRequests();
    }

    @FXML
    private void handleAvailableBabySetterView() {
        showOnlyPane(babySetterRequestPane);
        loadMySetterRequests();
    }

    @FXML
    private void handleEmergencyContactView() {
        showOnlyPane(emergencyContactPane);
        loadEmergencyContacts();
    }

    @FXML
    private void handleAppointmentsStatusView() {
        showOnlyPane(appointmentStatusPane);
        loadParentAppointments();
    }

    @FXML
    private void handleDoctorsTipsView() {
        showOnlyPane(doctorTipsPane);
        loadDoctorsTips();
    }

    private void loadParentsInfo() {
        loginParentId.setText(String.valueOf(SessionManager.loggedInParentId));
        loginParentsName.setText(SessionManager.loggedInParentsName);
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("parentsLogin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Parent Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Logout failed.");
        }
    }

    private void openBloodRequestDetailsPopup(int requestId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("parentBloodRequestDetails.fxml"));
            Parent root = loader.load();

            ParentBloodRequestDetailsController controller = loader.getController();
            controller.setRequestId(requestId);

            Stage stage = new Stage();
            stage.setTitle("Blood Request Details");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Could not open details window.");
        }
    }

    private void openSetterRequestDetails(int requestId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("babySetterRequestDetails.fxml"));
            Parent root = loader.load();

            BabySetterRequestDetailsController controller = loader.getController();
            controller.setRequestId(requestId);

            Stage stage = new Stage();
            stage.setTitle("Baby Setter Request Details");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Failed to open request details.");
        }
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
        availableDoctorsPane.setVisible(false);
        babyInformationPane.setVisible(true);
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
        babyInformationPane.setVisible(false);
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
            controller.setDoctor(selected.getId());

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

    @FXML
    private void handleBloodRequestSubmit() {
        String title = requestTitle.getText().trim();
        String location = locationName.getText().trim();
        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePeaker.getValue();
        String message = messageToDoner.getText().trim();
        int parentId = SessionManager.loggedInParentId;

        if (title.isEmpty() || location.isEmpty() || fromDate == null || toDate == null || message.isEmpty()) {
            AlertUtil.errorAlert("Please fill in all the fields before submitting the request.");
            return;
        }

        if (toDate.isBefore(fromDate)) {
            AlertUtil.errorAlert("'To' date must be after 'From' date.");
            return;
        }

        String sql = "INSERT INTO blood_donor_requests (parent_id, title, message, location_text, needed_from, needed_to, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, 'REQUESTED')";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, parentId);
            ps.setString(2, title);
            ps.setString(3, message);
            ps.setString(4, location);
            ps.setObject(5, fromDate.atStartOfDay());
            ps.setObject(6, toDate.atTime(23, 59));

            int rows = ps.executeUpdate();
            if (rows > 0) {
                AlertUtil.successAlert("Blood request submitted successfully.");
                loadMyBloodRequests();
                clearBloodRequestForm();
            } else {
                AlertUtil.errorAlert("Failed to submit blood request. Please try again.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Database error: " + e.getMessage());
        }
    }

    private void loadMyBloodRequests() {
        ObservableList<BloodRequest> list = FXCollections.observableArrayList();

        String sql = """
        SELECT
            r.id,
            p.name AS parent_name,
            COALESCE(GROUP_CONCAT(DISTINCT d.name SEPARATOR ', '), 'Pending') AS donor_names,
            COALESCE(GROUP_CONCAT(DISTINCT d.phone SEPARATOR ', '), '-') AS donor_phones,
            r.created_at,
            r.status
        FROM blood_donor_requests r
        JOIN parents p ON r.parent_id = p.id
        LEFT JOIN blood_donor_accepts a ON r.id = a.request_id
        LEFT JOIN blood_donors d ON a.donor_id = d.id
        WHERE r.parent_id = ?
        GROUP BY r.id, p.name, r.created_at, r.status
        ORDER BY r.created_at DESC
    """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, SessionManager.loggedInParentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new BloodRequest(
                        rs.getInt("id"),
                        rs.getString("parent_name"),
                        rs.getString("donor_names"),
                        rs.getString("donor_phones"),
                        rs.getString("created_at"),
                        rs.getString("status")
                ));
            }

            bloodRequestTable.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Failed to load blood requests.");
        }
    }

    private void completeBloodRequest(int requestId) {
        String sql = "UPDATE blood_donor_requests SET status = 'COMPLETED' WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            ps.executeUpdate();
            AlertUtil.successAlert("Request marked as completed.");
            loadMyBloodRequests();
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Failed to complete request.");
        }
    }


    @FXML
    private void handleSetterRequestSubmit() {
        String title = setterRequestTitle.getText().trim();
        String location = setterLocationName.getText().trim();
        LocalDate from = fromDatePickerSetter.getValue();
        LocalDate to = toDatePeakerSetter.getValue();
        String message = messageToSetter.getText().trim();

        if (title.isEmpty() || location.isEmpty() || from == null || to == null || message.isEmpty()) {
            AlertUtil.errorAlert("Please complete all fields.");
            return;
        }

        if (to.isBefore(from)) {
            AlertUtil.errorAlert("'To' date must be after 'From' date.");
            return;
        }

        String sql = "INSERT INTO baby_setter_requests (parent_id, title, message, location_text, needed_from, needed_to, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, 'REQUESTED')";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, SessionManager.loggedInParentId);
            ps.setString(2, title);
            ps.setString(3, message);
            ps.setString(4, location);
            ps.setObject(5, from.atStartOfDay());
            ps.setObject(6, to.atTime(23, 59));

            int result = ps.executeUpdate();
            if (result > 0) {
                AlertUtil.successAlert("Setter request submitted!");
                clearSetterRequestForm();
                loadMySetterRequests();
            } else {
                AlertUtil.errorAlert("Submission failed.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Error: " + e.getMessage());
        }
    }


    private void loadMySetterRequests() {
        ObservableList<BabySetterRequest> list = FXCollections.observableArrayList();

        String sql = """
        SELECT
          r.id,
          p.name AS parent_name,
          COALESCE(GROUP_CONCAT(bs.name SEPARATOR ', '), 'Pending') AS setter_names,
          COALESCE(GROUP_CONCAT(bs.phone SEPARATOR ', '), '-') AS setter_phones,
          r.created_at,
          r.status
        FROM baby_setter_requests r
        JOIN parents p ON r.parent_id = p.id
        LEFT JOIN baby_setter_accepts a ON r.id = a.request_id
        LEFT JOIN baby_setters bs ON a.setter_id = bs.id
        WHERE r.parent_id = ?
          AND r.needed_to >= NOW()
        GROUP BY r.id, p.name, r.created_at, r.status
        ORDER BY r.created_at DESC
    """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, SessionManager.loggedInParentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new BabySetterRequest(
                        rs.getInt("id"),
                        rs.getString("parent_name"),
                        rs.getString("setter_names") != null ? rs.getString("setter_names") : "Pending",
                        rs.getString("setter_phones") != null ? rs.getString("setter_phones") : "-",
                        rs.getString("created_at"),
                        rs.getString("status")
                ));
            }

            setterRequestTable.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Failed to load requests.");
        }
    }


    private void markRequestCompleted(int requestId) {
        String sql = "UPDATE baby_setter_requests SET status = 'COMPLETED' WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, requestId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                AlertUtil.successAlert("Request marked as completed.");
                loadMySetterRequests(); // Refresh table
            } else {
                AlertUtil.errorAlert("Update failed.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Error: " + e.getMessage());
        }
    }


    private void loadEmergencyContacts() {
        emergencyList.clear();
        filterByType.getItems().clear();
        filterByLocation.getItems().clear();

        String sql = "SELECT id, name, contact_number, type, location, address FROM emergency_contacts";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                emergencyList.add(new EmergencyContact(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("contact_number"),
                        rs.getString("type"),
                        rs.getString("location"),
                        rs.getString("address")
                ));
            }

            serviceId.setCellValueFactory(data -> data.getValue().idProperty().asObject());
            serviceName.setCellValueFactory(data -> data.getValue().nameProperty());
            serviceContactNo.setCellValueFactory(data -> data.getValue().contactNumberProperty());
            serviceType.setCellValueFactory(data -> data.getValue().typeProperty());
            serviceLocation.setCellValueFactory(data -> data.getValue().locationProperty());
            serviceAddress.setCellValueFactory(data -> data.getValue().addressProperty());

            emergencyContactTable.setItems(emergencyList);

            filterByType.getItems().add("All");
            filterByLocation.getItems().add("All");
            emergencyList.stream().map(EmergencyContact::getType).distinct().forEach(filterByType.getItems()::add);
            emergencyList.stream().map(EmergencyContact::getLocation).distinct().forEach(filterByLocation.getItems()::add);

            filterByType.getSelectionModel().select("All");
            filterByLocation.getSelectionModel().select("All");

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Failed to load emergency contacts.");
        }
    }

    @FXML
    private void applyEmergencyFilter() {
        String selectedType = filterByType.getValue();
        String selectedLocation = filterByLocation.getValue();

        ObservableList<EmergencyContact> filteredList = emergencyList.filtered(item ->
                ("All".equals(selectedType) || item.getType().equalsIgnoreCase(selectedType)) &&
                        ("All".equals(selectedLocation) || item.getLocation().equalsIgnoreCase(selectedLocation))
        );

        emergencyContactTable.setItems(filteredList);
    }

    private void loadParentAppointments() {
        ObservableList<AppointmentStatusRow> list = FXCollections.observableArrayList();

        String sql = """
        SELECT 
            a.id,
            b.name AS baby_name,
            d.name AS doctor_name,
            a.scheduled_at,
            a.status,
            a.doctor_note
        FROM appointments a
        JOIN babies b ON a.baby_id = b.id
        JOIN doctors d ON a.doctor_id = d.id
        WHERE a.parent_id = ?
        ORDER BY a.scheduled_at DESC
    """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, SessionManager.loggedInParentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new AppointmentStatusRow(
                        rs.getInt("id"),
                        rs.getString("baby_name"),
                        rs.getString("doctor_name"),
                        rs.getString("scheduled_at"),
                        rs.getString("status"),
                        rs.getString("doctor_note")
                ));
            }

            appointmentId.setCellValueFactory(data -> data.getValue().idProperty().asObject());
            appointmentBabyName.setCellValueFactory(data -> data.getValue().babyNameProperty());
            doctorName.setCellValueFactory(data -> data.getValue().doctorNameProperty());
            scheduledAt.setCellValueFactory(data -> data.getValue().scheduledAtProperty());
            status.setCellValueFactory(data -> data.getValue().statusProperty());
            doctorNote.setCellValueFactory(data -> data.getValue().doctorNoteProperty());

            appointmentStatusTable.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Failed to load appointment status: " + e.getMessage());
        }
    }

    private void loadDoctorsTips() {
        tipsContainer.getChildren().clear();

        String sql = """
        SELECT t.id, d.name AS doctor_name, d.specialization, t.title, t.content, t.published_at
        FROM tips t
        JOIN doctors d ON t.doctor_id = d.id
        ORDER BY t.published_at DESC
    """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                VBox card = createTipCard(
                        rs.getString("doctor_name"),
                        rs.getString("specialization"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("published_at")
                );
                tipsContainer.getChildren().add(card);
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.errorAlert("Failed to load doctor tips: " + e.getMessage());
        }
    }

    private VBox createTipCard(String doctorName, String specialization, String title, String content, String publishedAt) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label doctorInfo = new Label("Dr. " + doctorName + " (" + specialization + ")");
        doctorInfo.setStyle("-fx-font-size: 13px; -fx-text-fill: #555;");

        Label dateLabel = new Label("Published: " + publishedAt);
        dateLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");

        TextArea contentLabel = new TextArea(content);
        contentLabel.setWrapText(true);
        contentLabel.setEditable(false);
        contentLabel.setPrefHeight(100);
        contentLabel.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #ccc; -fx-border-radius: 5;");

        VBox box = new VBox(5, titleLabel, doctorInfo, contentLabel, dateLabel);
        box.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 8; "
                + "-fx-padding: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5,0,0,2);");
        return box;
    }



    private void clearBloodRequestForm() {
        requestTitle.clear();
        locationName.clear();
        fromDatePicker.setValue(null);
        toDatePeaker.setValue(null);
        messageToDoner.clear();
    }

    private void clearSetterRequestForm() {
        setterRequestTitle.clear();
        setterLocationName.clear();
        fromDatePickerSetter.setValue(null);
        toDatePeakerSetter.setValue(null);
        messageToSetter.clear();
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
