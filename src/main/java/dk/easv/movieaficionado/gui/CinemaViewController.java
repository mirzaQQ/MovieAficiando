package dk.easv.movieaficionado.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;

public class CinemaViewController {

    @FXML
    private Pane paneId;

    @FXML
    private BorderPane borderpaneId;

    @FXML
    private HBox controlsId;

    @FXML
    private Slider sliderVolume;

    private MediaPlayer mediaPlayer;
    private MediaView mediaView;

    public CinemaViewController() {
    }

    @FXML
    public void initialize() {

        controlsId.setOpacity(0);

        mediaView = new MediaView();
        borderpaneId.setCenter(mediaView);

        mediaView.setPreserveRatio(true);
        mediaView.fitWidthProperty().bind(borderpaneId.widthProperty());
        mediaView.fitHeightProperty().bind(borderpaneId.heightProperty());

        String url = new File("video.mp4").toURI().toString();
        Media media = new Media(url);
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);

        mediaPlayer.setVolume(sliderVolume.getValue() / 100.0);
        sliderVolume.valueProperty().addListener((obs, oldV, newV) ->
                mediaPlayer.setVolume(newV.doubleValue() / 100.0)
        );

        mediaPlayer.setOnReady(mediaPlayer::play);
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
