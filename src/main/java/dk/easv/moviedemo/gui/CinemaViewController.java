package dk.easv.moviedemo.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;

public class CinemaViewController {
    @FXML
    private Pane paneId;
    @FXML
    private BorderPane borderpaneId;

    @FXML
    private HBox controlsId;
    
    
    private MediaPlayer mediaPlayer;
    private MediaView mediaView;
    public CinemaViewController()
    {
        MainWindowController mainWindowController = new MainWindowController();
        Media media = new Media(new File(mainWindowController.getSelectedItem()).toURI().toString());

        mediaPlayer = new MediaPlayer(media);
        this.mediaView = new MediaView(mediaPlayer);
        this.mediaPlayer = mediaPlayer;
    }
    public void initialize()
    {
        borderpaneId.setCenter(mediaView);
        borderpaneId.autosize();
    }
    public void OnMouseMoved(MouseEvent mouseEvent) {
        controlsId.setOpacity(1);
    }

    public void OnMouseExited(MouseEvent mouseEvent) {
        controlsId.setOpacity(0);
    }

    public void btnPlayOnClick(ActionEvent actionEvent) {
        mediaPlayer.play();

    }

    public void btnPauseOnClick(ActionEvent actionEvent) {
        mediaPlayer.pause();
    }
}
