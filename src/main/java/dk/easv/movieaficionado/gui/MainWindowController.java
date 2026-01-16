package dk.easv.movieaficionado.gui;

import dk.easv.movieaficionado.MainApplication;
import dk.easv.movieaficionado.be.Category;
import dk.easv.movieaficionado.be.Movie;
import dk.easv.movieaficionado.bll.Checker;
import dk.easv.movieaficionado.bll.FileOps;
import dk.easv.movieaficionado.bll.exceptions.MovieCleanupWarningException;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class MainWindowController {

    private final Checker checker = new Checker();

    // Table view fields for viewing movies
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

    public void btnAddCategory(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("gui/AddCategory.fxml"));
        Scene scene = new Scene(loader.load());

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
        sortedMovies.comparatorProperty().bind(movieTable.comparatorProperty());

        refreshMovies();

        // Cleanup popup
        try {
            checker.checkForCleanupWarning();
        } catch (MovieCleanupWarningException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cleanup reminder");
            alert.setHeaderText("Movie cleanup recommended");

            TextArea area = new TextArea(e.getMessage());
            area.setEditable(false);
            area.setWrapText(true);
            area.setMaxWidth(Double.MAX_VALUE);
            area.setMaxHeight(Double.MAX_VALUE);

            alert.getDialogPane().setContent(area);
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Filters listeners
        txtTitleFilter.textProperty().addListener((obs, oldText, newText) -> applyFilters());
        sliderImdb.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        lstCategories.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> applyFilters());

        // Combo sorting
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
        if (selected == null) return;

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
        alert.setContentText("This will reset all filters and sorting.");

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

        try {
            checker.removeMovie(selected.getId());
            refreshMovies();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnPlayMovie(ActionEvent actionEvent) throws IOException {
        Movie selected = movieTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        FileOps fileOps = new FileOps();
        fileOps.playMovie(selected.getFilelink());

        try {
            checker.markMovieAsViewed(selected.getFilelink());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void applySorting() {
        String selected = cmbSortBy.getSelectionModel().getSelectedItem();

        movieTable.getSortOrder().clear();
        if (selected == null) return;

        TableColumn<Movie, ?> col = null;
        boolean desc = false;

        switch (selected) {
            case "Title (A–Z)":
                col = colTitle;
                desc = false;
                break;
            case "IMDb Rating (High → Low)":
                col = colImdb;
                desc = true;
                break;
            case "IMDb Rating (Low → High)":
                col = colImdb;
                desc = false;
                break;
            case "Personal Rating (High → Low)":
                col = colPersonal;
                desc = true;
                break;
            case "Personal Rating (Low → High)":
                col = colPersonal;
                desc = false;
                break;
            case "Category (A–Z)":
                col = colCategories;
                desc = false;
                break;
        }

        if (col != null) {
            col.setSortType(desc ? TableColumn.SortType.DESCENDING : TableColumn.SortType.ASCENDING);
            movieTable.getSortOrder().add(col);
            movieTable.sort();
        }
    }

    private void applyFilters() {
        String title = txtTitleFilter.getText() == null ? "" : txtTitleFilter.getText().trim().toLowerCase();
        double minImdb = sliderImdb.getValue();

        Category selectedCategory = lstCategories.getSelectionModel().getSelectedItem();

        filteredMovies.setPredicate(movie -> {
            boolean matchesTitle = title.isEmpty() || movie.getName().toLowerCase().contains(title);
            boolean matchesImdb = movie.getRating() >= minImdb;

            boolean matchesCategory = true;
            if (selectedCategory != null) {
                matchesCategory = movie.getCategories().stream()
                        .anyMatch(c -> c.getName().equals(selectedCategory.getName()));
            }

            return matchesTitle && matchesImdb && matchesCategory;
        });
    }
}
