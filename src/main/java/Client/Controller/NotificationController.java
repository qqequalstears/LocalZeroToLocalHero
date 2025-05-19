package Client.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class NotificationController {
    private static NotificationController instance;
    private final ConcurrentHashMap<String, List<Notification>> userNotifications;

    private NotificationController() {
        userNotifications = new ConcurrentHashMap<>();
    }

    public static NotificationController getInstance() {
        if (instance == null) {
            instance = new NotificationController();
        }
        return instance;
    }

    public void createNotification(String userId, String title, String message) {
        Notification notification = new Notification(title, message);
        userNotifications.computeIfAbsent(userId, k -> new ArrayList<>()).add(notification);
    }

    public List<Notification> getNotifications(String userId) {
        return userNotifications.getOrDefault(userId, new ArrayList<>());
    }

    public void clearNotifications(String userId) {
        userNotifications.remove(userId);
    }

    public static class Notification {
        private final String title;
        private final String message;
        private final long timestamp;

        public Notification(String title, String message) {
            this.title = title;
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }

        public String getTitle() {
            return title;
        }

        public String getMessage() {
            return message;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
} 