package Client.View;

import javafx.scene.control.Alert;

public class UserNotifier {
    public UserNotifier() {}

    public void informUser(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.show();
    }
}
