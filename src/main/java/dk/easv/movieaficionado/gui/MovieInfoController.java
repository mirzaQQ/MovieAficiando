package dk.easv.movieaficionado.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import dk.easv.movieaficionado.bll.*;

import java.sql.SQLException;

public class MovieInfoController {
    @FXML
    public TextField txtFile;
    @FXML
    private Button btnExit;
    @FXML
    private TextField txtCategory;
    @FXML
    private TextField txtPersonal;
    @FXML
    private TextField txtImbd;
    @FXML
    private TextField txtTitle;
    private String currentFile;

    FileOps fileOps = new FileOps();
    Checker checker = new Checker();
    public void btnSaveOnClick(ActionEvent actionEvent) {
        try {
            if (currentFile == null || currentFile.isBlank()) return;

            double imdb = Double.parseDouble(txtImbd.getText().trim().replace(",", "."));
            double personal = Double.parseDouble(txtPersonal.getText().trim().replace(",", "."));

            if (imdb < 0 || imdb > 10 || personal < 0 || personal > 10) {
                System.out.println("Rating must be between 0 and 10");
                return;
            }
            checker.addMovie(
                    txtTitle.getText().trim(),
                    imdb,
                    currentFile,
                    personal,
                    txtCategory.getText().trim()
            );

        } catch (NumberFormatException e) {
            System.out.println("Ratings must be numbers!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnExitOnClick(ActionEvent actionEvent) {
        Stage stage = (Stage) btnExit.getScene().getWindow();

        stage.close();
    }

    public void btnSearchOnClick(ActionEvent actionEvent) {

        currentFile = fileOps.openFile();
        txtFile.setText(currentFile);

    }
}
