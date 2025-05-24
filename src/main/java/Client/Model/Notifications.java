package Client.Model;

import java.util.ArrayList;
import java.util.List;

public class Notifications {
    public static List<String> notifications = new ArrayList<>();
    public static int unreadCount = 0;

    public static void addNotification(String notification) {
        notifications.add(notification);
        unreadCount++;
        System.out.println("[DEBUG] Notification added: " + notification + ", unreadCount: " + unreadCount);
    }

    public static void resetUnreadCount() {
        unreadCount = 0;
        System.out.println("[DEBUG] Unread count reset to 0");
    }

    public static int getUnreadCount() {
        System.out.println("[DEBUG] getUnreadCount called, value: " + unreadCount);
        return unreadCount;
    }
}
