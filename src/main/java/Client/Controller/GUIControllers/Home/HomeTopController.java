package Client.Controller.GUIControllers.Home;

import Client.Controller.GUIControllers.FxController;
import Client.Controller.GUIControllers.GUIControllerRegistry;
import Client.Controller.GUIControllers.GUIInController;
import Client.Controller.GUIControllers.GUIOutController;
import Client.View.Message.MessageView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import Client.Model.Notifications;

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
    private GUIOutController guiOutController;

    @FXML
    public void initialize() {
        guiInController = GUIInController.getInstance();
        guiOutController = GUIOutController.getInstance();
        GUIControllerRegistry.getInstance().add(this.getClass().getName(), this);
        updateNotificationsButton();
    }

    @FXML
    public void openNotifications() {
        Notifications.resetUnreadCount();
        updateNotificationsButton();
        guiInController.createStage("NOTIFICATIONS");
    }

    @FXML
    public void openAchievements() {
        guiOutController.requestAchievements();
    }
    @FXML
    public void openLog(){
        guiOutController.requestLog();
        guiInController.createStage("LOGSTAGE");
    }

    @FXML
    public void logout() {
        guiOutController.logout();
    }

    public void updateNotificationsButton() {
        int unread = Notifications.getUnreadCount();
        System.out.println("[DEBUG] HomeTopController.updateNotificationsButton called, unread: " + unread);
        
        if (notificationsButton != null) {
            if (unread > 0) {
                notificationsButton.setText("Notifications (" + unread + ")");
            } else {
                notificationsButton.setText("Notifications");
            }
            System.out.println("[DEBUG] Notifications button text updated to: " + notificationsButton.getText());
        } else {
            System.out.println("[DEBUG] WARNING: notificationsButton is null!");
        }
    }

    public void notifyUser() {
        System.out.println("[DEBUG] notifyUser called");
        updateNotificationsButton();
    }

    @Override
    public void closeStage() {
        Stage stage = (Stage) logButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void openInbox() {
        String currentUserId = guiOutController.getConnectedUserEmail();
        MessageView messageView = new MessageView(currentUserId);
    }
}