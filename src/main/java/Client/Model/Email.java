package Client.Model;

import java.time.LocalDateTime;

public class Email {
    private String senderId;
    private String recipientId;
    private String subject;
    private String content;
    private LocalDateTime timestamp;
    private boolean isRead;

    public Email(String senderId, String recipientId, String subject, String content) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.subject = subject;
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.isRead = false;
    }

    // Getters and Setters
    public String getSenderId() {
        return senderId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
} 