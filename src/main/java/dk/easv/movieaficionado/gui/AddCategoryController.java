package dk.easv.movieaficionado.gui;

import dk.easv.movieaficionado.bll.Checker;
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

    private Runnable onCategoryAdded;

    public void setOnCategoryAdded(Runnable onCategoryAdded) {
        this.onCategoryAdded = onCategoryAdded;
    }

    @FXML
    private Button btnCancelId;
    Checker checker = new Checker();
    public void btnAddOnClick(ActionEvent actionEvent) {
        String category = txtCategory.getText();
        try {
            checker.addCategory(category);
            lblFeedback.setText("Added successfully");
            if (onCategoryAdded != null) {
                onCategoryAdded.run();
            }
        } catch (Exception e) {
            lblFeedback.setText("Category already exists");
        }
    }

    public void btnCancelOnClick(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCancelId.getScene().getWindow();

        stage.close();
    }
}
