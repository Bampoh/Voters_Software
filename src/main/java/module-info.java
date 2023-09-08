module com.example.voters_software {
    requires javafx.controls;
    requires javafx.fxml;
   requires  java.sql;


    opens com.example.voters_software to javafx.fxml;
    exports com.example.voters_software;
}