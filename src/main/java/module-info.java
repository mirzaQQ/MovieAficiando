module dk.easv.moviedemo {
    requires javafx.controls;
    requires javafx.fxml;
    //requires dk.easv.moviedemo;
    requires javafx.graphics;
    requires java.desktop;
    requires javafx.media;
    //requires dk.easv.moviedemo;


    opens dk.easv.moviedemo to javafx.fxml;
    exports dk.easv.moviedemo;
    exports dk.easv.moviedemo.gui;
    opens dk.easv.moviedemo.gui to javafx.fxml;
}