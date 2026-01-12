package dk.easv.moviedemo.gui;

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

    public void btnAddOnClick(ActionEvent actionEvent) {
        String category = txtCategory.getText();
        lblFeedback.setText("Adding category: " + category);
    }

    public void btnCancelOnClick(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCancelId.getScene().getWindow();

        stage.close();
    }
}
