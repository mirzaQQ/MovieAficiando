package dk.easv.movieaficionado.gui;

import dk.easv.movieaficionado.be.Category;
import dk.easv.movieaficionado.be.Movie;
import dk.easv.movieaficionado.bll.Checker;
import dk.easv.movieaficionado.bll.FileOps;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class MovieInfoController {

    @FXML private TextField txtFile;
    @FXML private TextField txtPersonal;
    @FXML private TextField txtImbd;
    @FXML private TextField txtTitle;
    @FXML private Button btnExit;


    // NEW: multi-category selection
    @FXML private ListView<Category> categoryListView;

    private String currentFile;
    private Runnable onMovieAdded;
    private Movie movieToEdit = null;

    private final FileOps fileOps = new FileOps();
    private final Checker checker = new Checker();






    @FXML
    public void initialize() {
        try {
            categoryListView.setItems(
                    FXCollections.observableArrayList(checker.getAllCategories())
            );
            categoryListView.getSelectionModel()
                    .setSelectionMode(SelectionMode.MULTIPLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setOnMovieAdded(Runnable onMovieAdded) {
        this.onMovieAdded = onMovieAdded;
    }

    //This method fills out the information about a movie that the user wants to edit
    public void setMovieToEdit(Movie movie) {
        this.movieToEdit = movie;

        txtTitle.setText(movie.getName());
        txtImbd.setText(String.valueOf(movie.getRating()));
        txtPersonal.setText(String.valueOf(movie.getPrating()));
        currentFile = movie.getFilelink();
        txtFile.setText(currentFile);

        categoryListView.getSelectionModel().clearSelection();
        for (Category c : categoryListView.getItems()) {
            if (movie.getCategories().stream().anyMatch(mc -> mc.getName().equals(c.getName()))) {
                categoryListView.getSelectionModel().select(c);
            }
        }
    }

    @FXML
    public void btnSaveOnClick(ActionEvent actionEvent) {
        try {
            if (currentFile == null || currentFile.isBlank()) return;

            double imdb = Double.parseDouble(txtImbd.getText().trim().replace(",", "."));
            double personal = Double.parseDouble(txtPersonal.getText().trim().replace(",", "."));

            if (imdb < 0 || imdb > 10 || personal < 0 || personal > 10) {
                System.out.println("Rating must be between 0 and 10");
                return;
            }

            Set<Category> selectedCategories =
                    new HashSet<>(categoryListView.getSelectionModel().getSelectedItems());

            if (selectedCategories.isEmpty()) {
                System.out.println("A movie must have at least one category");
                return;
            }

            String title = txtTitle.getText().trim();

            if (movieToEdit == null) {
                //Add
                Movie movie = new Movie(title, currentFile, selectedCategories);
                movie.setRating(imdb);
                movie.setPrating(personal);
                checker.addMovie(movie);
            } else {
                //Edit
                Movie movie = new Movie(movieToEdit.getId(), title, currentFile, selectedCategories);
                movie.setRating(imdb);
                movie.setPrating(personal);
                checker.updateMovie(movie);
            }

            if (onMovieAdded != null) onMovieAdded.run();

            Stage stage = (Stage) btnExit.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            System.out.println("Ratings must be numbers!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    @FXML
    public void btnExitOnClick(ActionEvent actionEvent) {
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void btnSearchOnClick(ActionEvent actionEvent) {
        currentFile = fileOps.openFile();
        txtFile.setText(currentFile);
    }
}
