module com.zcare.finalproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens com.zcare.finalproject to javafx.fxml;
    exports com.zcare.finalproject;
}