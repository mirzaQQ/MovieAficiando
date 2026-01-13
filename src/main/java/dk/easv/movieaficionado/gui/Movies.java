package dk.easv.movieaficionado.gui;

import dk.easv.movieaficionado.MainApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;


import javafx.scene.media.MediaPlayer;

public class Movies {
    private MediaPlayer mediaPlayer;


    public void RemovedMovie() {
        System.out.println("Removing movie from list");

    }


    public void AddMovie() throws IOException {
        System.out.println("Adding movie to list");
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("gui/MovieInfo.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Add movie");
        stage.setScene(scene);
        stage.show();

    }


    public void EditMovie() throws IOException {
        /**
         * This FMXL is the same as the addMovie, but I believe that we can use the same file for this operation
         * by creating a method which will just send the data to the other window(controller), and that data can
         * be used to fill the textfields in the controller.
         */

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("gui/MovieInfo.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Edit movie");
        stage.setScene(scene);
        stage.show();
    }

    public void startCinema() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("gui/CinemaView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Cinema");
        stage.setScene(scene);
        stage.show();

    }


}
