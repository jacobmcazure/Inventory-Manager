module mcewen.mainform {
    requires javafx.controls;
    requires javafx.fxml;


    opens mcewen.mainform to javafx.fxml;
    opens controller to javafx.fxml;
    opens model to javafx.fxml, javafx.base;

    exports mcewen.mainform;
    exports controller;
    exports model;
    //exports model to javafx.base;

}