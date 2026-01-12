package dk.easv.moviedemo.gui;

import dk.easv.moviedemo.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainWindowController {
    private String SelectedItem;
    Movies movieOps = new Movies();
    public void btnAddCategory(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("gui/AddCategory.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Add Category");
        stage.setScene(scene);
        stage.show();
    }

    public void btnAddMovie(ActionEvent actionEvent) throws IOException {
        movieOps.AddMovie();
    }

    public void btnEditMovie(ActionEvent actionEvent) throws IOException {
        movieOps.EditMovie();
    }

    public void btnRemoveMovie(ActionEvent actionEvent) {
        movieOps.RemovedMovie();
    }

    public void btnPlayMovie(ActionEvent actionEvent) throws IOException {
       /** MediaPlayer mediaPlayer;
        String path = "video.mp4";
        Media media = new Media(new File(path).toURI().toString());

        mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);*/

        movieOps.startCinema();
    }

    public String getSelectedItem() {
        SelectedItem = "video.mp4";
        return SelectedItem;
    }
}
