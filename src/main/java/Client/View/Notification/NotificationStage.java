package Client.View.Notification;

import Client.Controller.GUIControllers.GUIControllerRegistry;
import Client.Controller.GUIControllers.Notifications.NotificationController;
import Client.View.StageCreator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NotificationStage implements StageCreator {
    @Override
    public void createStage() {
        Stage notificationStage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/Notifications/NotificationStage.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        notificationStage.setScene(new Scene(root, 442, 400));
        notificationStage.setResizable(false);
        notificationStage.show();
        notificationStage.setOnHidden(e -> GUIControllerRegistry.getInstance().get(NotificationController.class.getName()).closeStage());
    }
}
