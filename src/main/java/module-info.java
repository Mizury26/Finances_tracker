module com.example.desktopproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires log4j;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;

    opens com.example.desktopproject to javafx.fxml;
    exports com.example.desktopproject;
    exports com.example.desktopproject.controller;
    opens com.example.desktopproject.controller to javafx.fxml;
    exports com.example.desktopproject.model;
    opens com.example.desktopproject.model to javafx.fxml;
    exports com.example.desktopproject.charts;
    opens com.example.desktopproject.charts to javafx.fxml;
    exports com.example.desktopproject.component;
    opens com.example.desktopproject.component to javafx.fxml;
    exports com.example.desktopproject.db;
    opens com.example.desktopproject.db to javafx.fxml;
    exports com.example.desktopproject.API;
    opens com.example.desktopproject.API to javafx.fxml;

}