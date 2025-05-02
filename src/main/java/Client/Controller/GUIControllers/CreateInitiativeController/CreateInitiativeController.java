package Client.Controller.GUIControllers.CreateInitiativeController;

import Client.Controller.GUIControllers.FxController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class CreateInitiativeController implements FxController {
    @FXML
    private TextField nameTextfield;

    @FXML
    private TextArea desciptionTextfield;

    @FXML
    private TextField locationTextfield;

    @FXML
    private TextField timeTextfield;

    @FXML
    private ComboBox<?> categoryChoiceBox;

    @FXML
    private CheckBox isPublicCheckbox;

    @FXML
    private Button createButton;

    @FXML
    public void initialize() {

    }

    @FXML
    public void createInitiative() {

    }

    @Override
    public void closeStage() {
        Stage stage = (Stage) createButton.getScene().getWindow();
        stage.close();
    }
}
