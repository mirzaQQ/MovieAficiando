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
    public void btnSaveOnClick(ActionEvent actionEvent) throws SQLException {
        String category = txtCategory.getText();
        String personal = txtPersonal.getText();
        String imbd = txtImbd.getText();
        String title = txtTitle.getText();
        String file = txtFile.getText();
        System.out.println("Category: " + category);
        System.out.println("Personal: " + personal);
        System.out.println("Imbd: " + imbd);
        System.out.println("Title: " + title);

        System.out.println("File: " + file);
        checker.addMovie(txtTitle.getText(), txtImbd.getText(), currentFile, txtPersonal.getText(), txtCategory.getText());

        System.out.println(fileOps.checkFile(currentFile));

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
