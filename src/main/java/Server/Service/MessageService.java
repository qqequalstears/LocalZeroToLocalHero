package Server.Service;

import Common.Controller.Utility.Packager;
import Server.Controller.ClientUpdater;
import org.json.JSONObject;

import java.time.LocalDateTime;

public class MessageService {
    private final ClientUpdater clientUpdater;
    private final FileStorageService fileStorageService;
    private final NotificationService notificationService;
    private static final String MESSAGES_FILE = "messages.txt";

    public MessageService(ClientUpdater clientUpdater, FileStorageService fileStorageService, NotificationService notificationService) {
        this.clientUpdater = clientUpdater;
        this.fileStorageService = fileStorageService;
        this.notificationService = notificationService;
    }

    public void handleNewMessage(JSONObject messageData) {
        String senderId = messageData.getString("senderId");
        String recipientId = messageData.getString("recipientId");
        String subject = messageData.getString("subject");
        String content = messageData.getString("content");
        
        JSONObject messagePackage = createMessagePackage(senderId, recipientId, subject, content);
        deliverMessageToRecipient(messagePackage, recipientId);
        persistMessage(senderId, recipientId, subject, content);
        notifyRecipient(senderId, recipientId);
    }

    private JSONObject createMessagePackage(String senderId, String recipientId, String subject, String content) {
        JSONObject messagePackage = new JSONObject();
        messagePackage.put("type", "newMessage");
        messagePackage.put("senderId", senderId);
        messagePackage.put("recipientId", recipientId);
        messagePackage.put("subject", subject);
        messagePackage.put("content", content);
        messagePackage.put("timestamp", LocalDateTime.now().toString());
        return messagePackage;
    }

    private void deliverMessageToRecipient(JSONObject messagePackage, String recipientId) {
        var receiver = clientUpdater.getClientConnection(recipientId);
        if (receiver != null) {
            receiver.sendObject(messagePackage.toString());
        }
    }

    private void persistMessage(String senderId, String recipientId, String subject, String content) {
        String messageData = String.join("|",
            senderId,
            recipientId,
            subject,
            content,
            LocalDateTime.now().toString(),
            "false"
        );
        fileStorageService.appendToFile(MESSAGES_FILE, messageData);
    }

    private void notifyRecipient(String senderId, String recipientId) {
        String notification = "You have received a new message from " + senderId;
        notificationService.sendNotification(notification, recipientId);
    }
} 