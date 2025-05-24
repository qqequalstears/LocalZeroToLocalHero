package Client.Model;

import java.time.LocalDateTime;

public class Message {
    private final String senderId;
    private final String recipientId;
    private final String subject;
    private final String content;
    private final LocalDateTime timestamp;
    private boolean read;

    public Message(String senderId, String recipientId, String subject, String content) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.subject = subject;
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.read = false;
    }

    // Getters
    public String getSenderId() { return senderId; }
    public String getRecipientId() { return recipientId; }
    public String getSubject() { return subject; }
    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public boolean isRead() { return read; }

    // Setters
    public void setRead(boolean read) { this.read = read; }
} 