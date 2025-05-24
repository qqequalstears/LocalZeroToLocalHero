package Client.Controller.GUIControllers.Notifications;

import Client.Controller.GUIControllers.FxController;

import Client.Controller.GUIControllers.GUIControllerRegistry;
import Client.Model.Notifications;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.event.ActionEvent;


public class NotificationController implements FxController {
    @FXML
    private ListView notificationsListView;
    private ObservableList<String> notifications = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        GUIControllerRegistry.getInstance().add(this.getClass().getName(), this);

        notificationsListView.setItems(notifications);
        notificationsListView.setCellFactory(listView -> new ListCell<String>() {
            @Override
            protected void updateItem(String notification, boolean empty) {
                super.updateItem(notification, empty);
                if (empty || notification == null) {
                    setText(null);
                } else {
                    setText(String.join("\n", notification));
                }
            }
        });
        updateNotifcations();
    }

    public void updateNotifcations() {
        notifications.clear();
        notifications.addAll(Notifications.notifications);
        System.out.println("[DEBUG] Notification list updated, size: " + notifications.size());
    }

    @FXML
    public void clearNotifications(ActionEvent event) {
        Notifications.notifications.clear();
        Notifications.resetUnreadCount();
        updateNotifcations();
    }

    @Override
    public void closeStage() {
        System.out.println("Det funka:)");
        GUIControllerRegistry.getInstance().remove(this.getClass().getName());
    }
}
