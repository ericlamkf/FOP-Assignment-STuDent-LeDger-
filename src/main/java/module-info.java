module com.example.studentledger {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires jdk.jfr;



    opens com.example.studentledger to javafx.fxml;
    exports com.example.studentledger;
}