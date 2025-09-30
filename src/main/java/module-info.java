module com.zcare.finalproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.zcare.finalproject to javafx.fxml;
    exports com.zcare.finalproject;
}