package dk.easv.movieaficionado.gui;

import dk.easv.movieaficionado.MainApplication;
import dk.easv.movieaficionado.be.Movie;
import dk.easv.movieaficionado.bll.FileOps;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import java.io.IOException;
import java.util.Comparator;

import dk.easv.movieaficionado.be.Category;
import javafx.fxml.FXML;
import dk.easv.movieaficionado.bll.Checker;
import dk.easv.movieaficionado.bll.DBOps;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import java.util.Comparator;



public class MainWindowController {

    private final Checker checker = new Checker();
    private final DBOps ops = new DBOps();

    //Table view fields for viewing movies
    @FXML private TableView<Movie> movieTable;
    @FXML private TableColumn<Movie, String> colTitle;
    @FXML private TableColumn<Movie, Number> colImdb;
    @FXML private TableColumn<Movie, Number> colPersonal;
    @FXML private TableColumn<Movie, String> colCategories;

    // UI controls
    @FXML private TextField txtTitleFilter;
    @FXML private ComboBox<String> cmbSortBy;
    @FXML private ListView<Category> lstCategories;
    @FXML private Slider sliderImdb;

    // Class fields
    private ObservableList<Movie> masterMovieList;
    private FilteredList<Movie> filteredMovies;
    private SortedList<Movie> sortedMovies;



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

        colTitle.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        colImdb.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getRating()));
        colPersonal.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrating()));
        colCategories.setCellValueFactory(data -> {
            Movie m = data.getValue();
            String cats = m.getCategories().stream()
                    .map(Category::getName)
                    .sorted()
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
            return new SimpleStringProperty(cats);
        });

        masterMovieList = FXCollections.observableArrayList();
        filteredMovies = new FilteredList<>(masterMovieList, m -> true);
        sortedMovies = new SortedList<>(filteredMovies);
        movieTable.setItems(sortedMovies);

        refreshMovies();

        txtTitleFilter.textProperty().addListener((obs, oldText, newText) -> applyFilters());
        sliderImdb.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        lstCategories.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> applyFilters());

        cmbSortBy.getItems().setAll(
                "Title (A–Z)",
                "IMDb Rating (High → Low)",
                "IMDb Rating (Low → High)",
                "Personal Rating (High → Low)",
                "Personal Rating (Low → High)",
                "Category (A–Z)"
        );
        cmbSortBy.setOnAction(e -> applySorting());

        applyFilters();
        applySorting();
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
    @FXML
    public void btnClearFilters(ActionEvent actionEvent) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Clear Filters");
        alert.setHeaderText("Clear all filters?");
        alert.setContentText("This will reset all filter settings and sorting.");

        ButtonType btnClear = new ButtonType("Clear");
        ButtonType btnCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnClear, btnCancel);

        alert.showAndWait().ifPresent(response -> {
            if (response == btnClear) {
                txtTitleFilter.clear();
                sliderImdb.setValue(0);
                lstCategories.getSelectionModel().clearSelection();
                cmbSortBy.getSelectionModel().clearSelection();

                applyFilters();
                applySorting();
            }
        });
    }



    public void refreshMovies() {
        try {
            masterMovieList.setAll(checker.getAllMovies());
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
        Movie selected = movieTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("gui/MovieInfo.fxml"));
        Scene scene = new Scene(loader.load());

        MovieInfoController controller = loader.getController();
        controller.setOnMovieAdded(this::refreshMovies);
        controller.setMovieToEdit(selected);

        Stage stage = new Stage();
        stage.setTitle("Edit Movie");
        stage.setScene(scene);
        stage.show();
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
        FileOps fileOps = new FileOps();
        fileOps.playMovie(movieTable.getSelectionModel().getSelectedItem().getFilelink());
    }

    private void applySorting() {
        String selected = cmbSortBy.getSelectionModel().getSelectedItem();

        if (selected == null) {
            sortedMovies.setComparator(null);
            return;
        }

        Comparator<Movie> comparator = null;

        switch (selected) {
            case "Title (A–Z)":
                comparator = Comparator.comparing(Movie::getName, String.CASE_INSENSITIVE_ORDER);
                break;
            case "IMDb Rating (High → Low)":
                comparator = Comparator.comparingDouble(Movie::getRating).reversed();
                break;
            case "IMDb Rating (Low → High)":
                comparator = Comparator.comparingDouble(Movie::getRating);
                break;
            case "Personal Rating (High → Low)":
                comparator = Comparator.comparingDouble(Movie::getPrating).reversed();
                break;
            case "Personal Rating (Low → High)":
                comparator = Comparator.comparingDouble(Movie::getPrating);
                break;
            case "Category (A–Z)":
                comparator = Comparator.comparing(
                        movie -> movie.getCategories().stream()
                                .map(Category::getName)
                                .sorted()
                                .reduce((a, b) -> a + ", " + b)
                                .orElse(""),
                        String.CASE_INSENSITIVE_ORDER
                );
                break;
        }

        sortedMovies.setComparator(comparator);
    }

    //Something like a master filter
    private void applyFilters() {
        String title = txtTitleFilter.getText() == null ? "" : txtTitleFilter.getText().trim().toLowerCase();
        double minImdb = sliderImdb.getValue();

        Category selectedCategory = lstCategories.getSelectionModel().getSelectedItem();

        filteredMovies.setPredicate(movie -> {
            // Title filter
            boolean matchesTitle = title.isEmpty()
                    || movie.getName().toLowerCase().contains(title);

            // Min IMDB filter
            boolean matchesImdb = movie.getRating() >= minImdb;

            // Category filter
            boolean matchesCategory = true;
            if (selectedCategory != null) {
                matchesCategory = movie.getCategories().stream()
                        .anyMatch(c -> c.getName().equals(selectedCategory.getName()));
            }

            return matchesTitle && matchesImdb && matchesCategory;
        });
        applySorting();
    }

}
