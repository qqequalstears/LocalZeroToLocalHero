package Client.Controller.GUIControllers.CreateInitiativeController;

import Client.Controller.Mediators.Mediator;
import Client.Controller.Mediators.MediatorManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;

public class CreateInitiativeController {
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
    private Mediator mediator;


    @FXML
    public void initialize() {
        mediator = MediatorManager.getInstance().getMediator("INITIATIVE");
    }

    @FXML
    public void createInitiative() {

    }
}
