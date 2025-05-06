package Client.Controller;

import Client.Model.Email;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class EmailController {
    private static EmailController instance;
    private final ConcurrentHashMap<String, List<Email>> userInboxes;
    private final ConcurrentHashMap<String, List<Email>> userSentEmails;

    private EmailController() {
        userInboxes = new ConcurrentHashMap<>();
        userSentEmails = new ConcurrentHashMap<>();
    }

    public static EmailController getInstance() {
        if (instance == null) {
            instance = new EmailController();
        }
        return instance;
    }

    public void sendEmail(String senderId, String recipientId, String subject, String content) {
        Email email = new Email(senderId, recipientId, subject, content);
        
        // Add to recipient's inbox
        userInboxes.computeIfAbsent(recipientId, k -> new ArrayList<>()).add(email);
        
        // Add to sender's sent emails
        userSentEmails.computeIfAbsent(senderId, k -> new ArrayList<>()).add(email);
        
        // Trigger notification
        NotificationController.getInstance().createNotification(
            recipientId,
            "New Email",
            "You have received a new email from " + senderId
        );
    }

    public List<Email> getInbox(String userId) {
        return userInboxes.getOrDefault(userId, new ArrayList<>());
    }

    public List<Email> getSentEmails(String userId) {
        return userSentEmails.getOrDefault(userId, new ArrayList<>());
    }

    public void markEmailAsRead(String userId, Email email) {
        email.setRead(true);
    }

    public int getUnreadEmailCount(String userId) {
        return (int) getInbox(userId).stream()
            .filter(email -> !email.isRead())
            .count();
    }
} 