package Client.Model;

import java.util.ArrayList;
import java.util.List;

public class Notifications {
    public static List<String> notifications = new ArrayList<>();
    public static int unreadCount = 0;

    public static void addNotification(String notification) {
        notifications.add(notification);
        unreadCount++;
    }

    public static void resetUnreadCount() {
        unreadCount = 0;
    }

    public static int getUnreadCount() {
        return unreadCount;
    }
}
