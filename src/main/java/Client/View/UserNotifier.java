package Client.View;

import Client.Controller.GUIControllers.GUIControllerRegistry;
import javafx.application.Platform;
import javafx.scene.control.Alert;

public class UserNotifier {
    public UserNotifier() {
    }

    public void informUser(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
            alert.setTitle("Information");
            alert.show();
        });
    }
}
