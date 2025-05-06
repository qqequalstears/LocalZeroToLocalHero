package Client.Controller;

import Client.Model.Email;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EmailController {
    private static EmailController instance;
    private final ConcurrentHashMap<String, List<Email>> userInboxes;
    private final ConcurrentHashMap<String, List<Email>> userSentEmails;
    private static final String EMAILS_FILE = "emails.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private EmailController() {
        userInboxes = new ConcurrentHashMap<>();
        userSentEmails = new ConcurrentHashMap<>();
        loadEmailsFromFile();
    }

    public static EmailController getInstance() {
        if (instance == null) {
            instance = new EmailController();
        }
        return instance;
    }

    private void loadEmailsFromFile() {
        File file = new File(EMAILS_FILE);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", -1);
                if (parts.length < 6) continue;
                String senderId = parts[0];
                String recipientId = parts[1];
                String subject = parts[2];
                String content = parts[3];
                LocalDateTime timestamp = LocalDateTime.parse(parts[4], FORMATTER);
                boolean isRead = Boolean.parseBoolean(parts[5]);
                Email email = new Email(senderId, recipientId, subject, content);
                // Set timestamp and isRead
                try {
                    java.lang.reflect.Field tsField = Email.class.getDeclaredField("timestamp");
                    tsField.setAccessible(true);
                    tsField.set(email, timestamp);
                    java.lang.reflect.Field readField = Email.class.getDeclaredField("isRead");
                    readField.setAccessible(true);
                    readField.set(email, isRead);
                } catch (Exception ignored) {}
                userInboxes.computeIfAbsent(recipientId, k -> new ArrayList<>()).add(email);
                userSentEmails.computeIfAbsent(senderId, k -> new ArrayList<>()).add(email);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveEmailsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(EMAILS_FILE))) {
            for (List<Email> emails : userInboxes.values()) {
                for (Email email : emails) {
                    writer.write(String.join("|",
                        email.getSenderId(),
                        email.getRecipientId(),
                        email.getSubject().replace("|", "/"),
                        email.getContent().replace("|", "/"),
                        email.getTimestamp().format(FORMATTER),
                        Boolean.toString(email.isRead())
                    ));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        saveEmailsToFile();
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