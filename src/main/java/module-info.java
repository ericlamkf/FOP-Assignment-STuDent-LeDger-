module com.example.studentledger {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.example.studentledger to javafx.fxml;
    exports com.example.studentledger;
}