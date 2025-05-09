package Client.Controller;

import Client.Model.Message;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.json.JSONObject;
import Client.Controller.ConnectionController;
import Client.Controller.GUIControllers.GUIOutController;

public class MessageController {
    private static MessageController instance;
    private final ConcurrentHashMap<String, List<Message>> userInboxes;
    private final ConcurrentHashMap<String, List<Message>> userSentMessages;
    private static final String MESSAGES_FILE = "messages.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private MessageController() {
        userInboxes = new ConcurrentHashMap<>();
        userSentMessages = new ConcurrentHashMap<>();
        loadMessagesFromFile();
    }

    public static MessageController getInstance() {
        if (instance == null) {
            instance = new MessageController();
        }
        return instance;
    }

    private void loadMessagesFromFile() {
        File file = new File(MESSAGES_FILE);
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
                Message message = new Message(senderId, recipientId, subject, content);
                // Set timestamp and isRead
                try {
                    java.lang.reflect.Field tsField = Message.class.getDeclaredField("timestamp");
                    tsField.setAccessible(true);
                    tsField.set(message, timestamp);
                    java.lang.reflect.Field readField = Message.class.getDeclaredField("isRead");
                    readField.setAccessible(true);
                    readField.set(message, isRead);
                } catch (Exception ignored) {}
                userInboxes.computeIfAbsent(recipientId, k -> new ArrayList<>()).add(message);
                userSentMessages.computeIfAbsent(senderId, k -> new ArrayList<>()).add(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveMessagesToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MESSAGES_FILE))) {
            for (List<Message> messages : userInboxes.values()) {
                for (Message message : messages) {
                    writer.write(String.join("|",
                        message.getSenderId(),
                        message.getRecipientId(),
                        message.getSubject().replace("|", "/"),
                        message.getContent().replace("|", "/"),
                        message.getTimestamp().format(FORMATTER),
                        Boolean.toString(message.isRead())
                    ));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String senderId, String recipientId, String subject, String content) {
        // Send message to server for delivery and notification
        JSONObject json = new JSONObject();
        json.put("type", "sendMessage");
        json.put("senderId", senderId);
        json.put("recipientId", recipientId);
        json.put("subject", subject);
        json.put("content", content);
        ConnectionController connectionController = GUIOutController.getInstance().getConnectionController();
        connectionController.sendJsonObject(json);
    }

    public List<Message> getInbox(String userId) {
        return userInboxes.getOrDefault(userId, new ArrayList<>());
    }

    public List<Message> getSentMessages(String userId) {
        return userSentMessages.getOrDefault(userId, new ArrayList<>());
    }

    public void markMessageAsRead(String userId, Message message) {
        if (message != null && message.getRecipientId().equals(userId)) {
            message.setRead(true);
        }
    }

    public int getUnreadMessageCount(String userId) {
        return (int) getInbox(userId).stream()
            .filter(message -> !message.isRead())
            .count();
    }
} 