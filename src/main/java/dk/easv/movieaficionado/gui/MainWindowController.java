package dk.easv.movieaficionado.gui;

import dk.easv.movieaficionado.MainApplication;
import dk.easv.movieaficionado.be.Movie;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.io.IOException;
import dk.easv.movieaficionado.be.Category;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import dk.easv.movieaficionado.bll.Checker;


public class MainWindowController {

    private final Checker checker = new Checker();

    //Table view fields for viewing movies
    @FXML private TableView<Movie> movieTable;
    @FXML private TableColumn<Movie, String> colTitle;
    @FXML private TableColumn<Movie, Number> colImdb;
    @FXML private TableColumn<Movie, Number> colPersonal;
    @FXML private TableColumn<Movie, String> colCategories;


    //List view for showing categories
    @FXML
    private ListView<Category> lstCategories;


    private String SelectedItem;
    Movies movieOps = new Movies();

    public void btnAddCategory(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("gui/AddCategory.fxml"));
        Scene scene = new Scene(loader.load()); // load musí být před getController()

        AddCategoryController controller = loader.getController();
        controller.setOnCategoryAdded(this::refreshCategories);

        Stage stage = new Stage();
        stage.setTitle("Add Category");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void initialize() {
        refreshCategories();
        colTitle.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getName())
        );
        colImdb.setCellValueFactory(data ->
                new SimpleDoubleProperty(data.getValue().getRating())
        );
        colPersonal.setCellValueFactory(data ->
                new SimpleDoubleProperty(data.getValue().getPrating())
        );
        colCategories.setCellValueFactory(data -> {
            Movie m = data.getValue();
            String cats = m.getCategories().stream()
                    .map(Category::getName)
                    .sorted()
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
            return new SimpleStringProperty(cats);
        });

        refreshMovies();
    }

    public void refreshCategories() {
        try {
            lstCategories.getItems().setAll(checker.getAllCategories());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void btnRemoveCategory(ActionEvent actionEvent) {
        Category selected = lstCategories.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        try {
            checker.deleteCategory(selected.getId());
            refreshCategories();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void refreshMovies() {
        try {
            movieTable.getItems().setAll(checker.getAllMovies());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnAddMovie(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("gui/MovieInfo.fxml"));
        Scene scene = new Scene(loader.load());

        MovieInfoController controller = loader.getController();
        controller.setOnMovieAdded(this::refreshMovies);

        Stage stage = new Stage();
        stage.setTitle("Add Movie");
        stage.setScene(scene);
        stage.show();
    }


    public void btnEditMovie(ActionEvent actionEvent) throws IOException {
        movieOps.EditMovie();
    }

    public void btnRemoveMovie(ActionEvent actionEvent) {
       Movie selected = movieTable.getSelectionModel().getSelectedItem();

        if (selected == null) return;

        try{
            checker.removeMovie(selected.getId());
            refreshMovies();
        }catch (Exception e){
            e.printStackTrace();
        }

        //movieOps.RemovedMovie(item);
    }

    public void btnPlayMovie(ActionEvent actionEvent) throws IOException {

        movieOps.startCinema();
    }

    public String getSelectedItem() {
        SelectedItem = "video.mp4";
        return SelectedItem;
    }
}
