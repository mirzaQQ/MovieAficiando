package dk.easv.moviedemo.gui;

import dk.easv.moviedemo.MainApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

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
        stage.setTitle("Hello!");
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
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }


    public void PlayMovie() throws IOException {
        /**
        startCinema();
        Media media = new Media(new File().toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);




        /
        String path = "video.mp4";
        Media media = new Media(new File(path).toURI().toString());

        mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);


        Button playBtn = new Button("Play");
        Button pauseBtn = new Button("Pause");
        Button stopBtn = new Button("Stop");

        playBtn.setOnAction(e -> mediaPlayer.play());
        pauseBtn.setOnAction(e -> mediaPlayer.pause());
        stopBtn.setOnAction(e -> mediaPlayer.stop());

        HBox controls = new HBox(10, playBtn, pauseBtn, stopBtn);

        BorderPane root = new BorderPane();
        root.setCenter(mediaView);
        root.setBottom(controls);

        Scene scene = new Scene(root, 800, 500);
        Stage stage = new Stage();
        stage.setTitle("JavaFX Media Player");
        stage.setScene(scene);
        stage.show();*/

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
