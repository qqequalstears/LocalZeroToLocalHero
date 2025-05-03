package Client.Controller.GUIControllers.Home;

import Client.Controller.GUIControllers.FxController;
import Client.Controller.GUIControllers.GUIControllerRegistry;
import Client.Controller.GUIControllers.GUIInController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HomeTopController implements FxController {
    @FXML
    private Button logoutButton;
    @FXML
    private Button achievemntsButton;
    @FXML
    private Button logButton;
    @FXML
    private Button inbocButton;
    @FXML
    private Button notificationsButton;
    private GUIInController guiInController;

    @FXML
    public void initialize() {
        guiInController = GUIInController.getInstance();
        GUIControllerRegistry.getInstance().add(this.getClass().getName(), this);
    }

    @FXML
    public void openNotifications() {
        notificationsButton.setText("Notifications");
        guiInController.createStage("NOTIFICATIONS");
    }

    @FXML
    public void logout() {

    }

    public void notifyUser() {
        notificationsButton.setText("Notifications*");
    }

    @Override
    public void closeStage() {
        Stage stage = (Stage) logButton.getScene().getWindow();
        stage.close();
    }
}
