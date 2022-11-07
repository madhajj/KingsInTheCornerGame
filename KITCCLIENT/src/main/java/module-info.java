module com.sep.kitc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires java.logging;


    opens com.sep.kitc to javafx.fxml;
    exports com.sep.kitc;
    exports com.sep.kitc.controller;
    opens com.sep.kitc.controller to javafx.fxml;
}