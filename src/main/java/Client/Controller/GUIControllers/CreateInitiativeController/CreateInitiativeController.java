package Client.Controller.GUIControllers.CreateInitiativeController;

import Client.Controller.GUIControllers.FxController;
import Client.Controller.GUIControllers.GUIControllerRegistry;
import Client.Controller.GUIControllers.GUIInController;
import Client.Controller.GUIControllers.GUIOutController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    private TextField startTimeTextfield;

    @FXML
    private TextField numberOfSeatsTextfield;

    @FXML
    private TextArea sellTextfield;

    @FXML
    private ComboBox categoryChoiceBox;

    @FXML
    private CheckBox isPublicCheckbox;

    @FXML
    private Button createButton;
    private GUIInController guiInController;
    private GUIOutController guiOutController;


    @FXML
    public void initialize() {
        GUIControllerRegistry.getInstance().add(this.getClass().getName(), this);
        guiInController = GUIInController.getInstance();
        guiOutController = GUIOutController.getInstance();

        List<String> categories = new ArrayList<>();
        categories.add("Carpool");
        categories.add("Gardening");
        categories.add("ToolSharing");
        categories.add("Garage Sale");
        categoryChoiceBox.getItems().addAll(categories);

        sellTextfield.setVisible(false);
        numberOfSeatsTextfield.setVisible(false);
    }

    @FXML
    public void createInitiative() {
        String name = nameTextfield.getText();
        String description = desciptionTextfield.getText();
        String location = locationTextfield.getText();
        String duration = timeTextfield.getText();
        String startTime = startTimeTextfield.getText();
        String numberOfSeats = numberOfSeatsTextfield.getText();
        String sellList = sellTextfield.getText();
        boolean ispublic = isPublicCheckbox.isSelected();

        if (categoryChoiceBox.getValue() != null) {
            String category = categoryChoiceBox.getValue().toString();
            guiOutController.createInitiative(name, description, location, duration, startTime, numberOfSeats, sellList, category, ispublic);
        } else {
            guiInController.notifyUser("Make sure to select a category");
        }
    }

    @FXML
    public void changeFields() {
        String category = categoryChoiceBox.getValue().toString();
        switch (category) {
            case "Carpool":
                numberOfSeatsTextfield.setVisible(true);
                sellTextfield.setVisible(false);
                break;
            case "Garage Sale":
                numberOfSeatsTextfield.setVisible(false);
                sellTextfield.setVisible(true);
                break;
            default:
                numberOfSeatsTextfield.setVisible(false);
                sellTextfield.setVisible(false);
        }
    }

    @Override
    public void closeStage() {
        Stage stage = (Stage) createButton.getScene().getWindow();
        stage.close();
    }
}

