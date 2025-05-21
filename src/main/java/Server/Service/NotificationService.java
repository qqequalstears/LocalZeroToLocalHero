package Server.Service;

import Server.Controller.ClientUpdater;
import Server.Controller.ClientConnection;
import org.json.JSONObject;

public class NotificationService {
    private final ClientUpdater clientUpdater;
    private final FileStorageService fileStorageService;
    private static final String NOTIFICATION_FILE_PREFIX = "notifications_";

    public NotificationService(ClientUpdater clientUpdater, FileStorageService fileStorageService) {
        this.clientUpdater = clientUpdater;
        this.fileStorageService = fileStorageService;
    }

    public void sendNotification(String notification, String recipientId) {
        ClientConnection receiver = clientUpdater.getClientConnection(recipientId);
        if (receiver != null) {
            sendOnlineNotification(receiver, notification);
        } else {
            storeOfflineNotification(recipientId, notification);
        }
    }

    private void sendOnlineNotification(ClientConnection receiver, String notification) {
        JSONObject notificationPackage = new JSONObject();
        notificationPackage.put("type", "notification");
        notificationPackage.put("notification", notification);
        receiver.sendObject(notificationPackage.toString());
    }

    private void storeOfflineNotification(String recipientId, String notification) {
        String filename = NOTIFICATION_FILE_PREFIX + recipientId + ".txt";
        fileStorageService.appendToFile(filename, notification);
    }
} 