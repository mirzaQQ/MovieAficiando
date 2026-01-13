package dk.easv.moviedemo.gui;

import dk.easv.moviedemo.bll.Checker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddCategoryController {
    @FXML
    private TextField txtCategory;

    @FXML
    private Label lblFeedback; //if exists "Added successfully" if not "Already exists"

    @FXML
    private Button btnCancelId;
    Checker checker = new Checker();
    public void btnAddOnClick(ActionEvent actionEvent) {
        String category = txtCategory.getText();
        try {
        checker.addCategory(category);
        lblFeedback.setText("Adding category: " + category);
        } catch (Exception e) {
        lblFeedback.setText("Category already exists");
        }
    }

    public void btnCancelOnClick(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCancelId.getScene().getWindow();

        stage.close();
    }
}
