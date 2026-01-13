package dk.easv.movieaficionado.gui;

import dk.easv.movieaficionado.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

        movieOps.startCinema();
    }

    public String getSelectedItem() {
        SelectedItem = "video.mp4";
        return SelectedItem;
    }
}
