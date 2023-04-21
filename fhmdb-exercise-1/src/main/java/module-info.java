module at.ac.fhcampuswien.fhmdb {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.jfoenix;
    requires okhttp3;
    requires com.google.gson;
    requires com.fasterxml.jackson.databind;

    opens at.ac.fhcampuswien.fhmdb to javafx.fxml;
    exports at.ac.fhcampuswien.fhmdb;
    exports at.ac.fhcampuswien.fhmdb.models;
    exports at.ac.fhcampuswien.fhmdb.controller;
    opens at.ac.fhcampuswien.fhmdb.controller to javafx.fxml;
    opens at.ac.fhcampuswien.fhmdb.models to com.fasterxml.jackson.databind;

}