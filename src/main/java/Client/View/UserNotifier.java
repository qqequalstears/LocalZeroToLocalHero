package Client.View;

import Client.Controller.Mediators.MediatorManager;
import javafx.scene.control.Alert;

public class UserNotifier {
    public UserNotifier() {
        MediatorManager.getInstance().getMediator("GUI").registerController(this.getClass().getName(),this);
    }

    public void informUser(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.show();
    }
}
