package Client.View.Message;

import Client.Controller.MessageController;
import Client.Model.Message;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class MessageView {
    private final MessageController messageController;
    private final String currentUserId;
    private final ListView<Message> inboxListView;
    private final TextArea messageContentArea;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");

    public MessageView(String userId) {
        this.messageController = MessageController.getInstance();
        this.currentUserId = userId;
        this.inboxListView = new ListView<>();
        this.messageContentArea = new TextArea();
        initializeUI();
    }

    private void initializeUI() {
        Stage stage = new Stage();
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Left - Message list
        VBox leftBox = new VBox(10);
        leftBox.setPadding(new Insets(10));
        leftBox.setPrefWidth(300);

        Label inboxLabel = new Label("Inbox");
        inboxLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        inboxListView.setCellFactory(new Callback<ListView<Message>, ListCell<Message>>() {
            @Override
            public ListCell<Message> call(ListView<Message> param) {
                return new ListCell<Message>() {
                    @Override
                    protected void updateItem(Message message, boolean empty) {
                        super.updateItem(message, empty);
                        if (empty || message == null) {
                            setText(null);
                            setStyle("");
                        } else {
                            setText(String.format("%s\nFrom: %s\n%s",
                                message.getSubject(),
                                message.getSenderId(),
                                message.getTimestamp().format(DATE_FORMATTER)));
                            
                            // Style unread messages
                            if (!message.isRead()) {
                                setStyle("-fx-font-weight: bold;");
                            } else {
                                setStyle("");
                            }
                        }
                    }
                };
            }
        });

        inboxListView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> displayMessage(newVal));

        leftBox.getChildren().addAll(inboxLabel, inboxListView);
        root.setLeft(leftBox);

        // Center - Message content
        messageContentArea.setEditable(false);
        messageContentArea.setWrapText(true);
        messageContentArea.setStyle("-fx-font-size: 14px;");
        root.setCenter(messageContentArea);

        // Top - Compose button
        Button composeButton = new Button("Compose Message");
        composeButton.setOnAction(e -> showComposeDialog());
        root.setTop(composeButton);

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Message Client");
        stage.setScene(scene);
        stage.show();

        // Load messages
        List<Message> messages = messageController.getInbox(currentUserId);
        inboxListView.getItems().addAll(messages);
    }

    private void displayMessage(Message message) {
        if (message != null) {
            messageController.markMessageAsRead(currentUserId, message);
            messageContentArea.setText(
                "From: " + message.getSenderId() + "\n" +
                "Subject: " + message.getSubject() + "\n" +
                "Date: " + message.getTimestamp().format(DATE_FORMATTER) + "\n\n" +
                message.getContent()
            );
        }
    }

    private void showComposeDialog() {
        Stage composeStage = new Stage();
        BorderPane composeRoot = new BorderPane();
        composeRoot.setPadding(new Insets(10));

        VBox formBox = new VBox(10);
        formBox.setPadding(new Insets(10));

        TextField recipientField = new TextField();
        recipientField.setPromptText("Recipient ID");
        TextField subjectField = new TextField();
        subjectField.setPromptText("Subject");
        TextArea contentArea = new TextArea();
        contentArea.setPromptText("Message content");
        contentArea.setPrefRowCount(10);

        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> {
            String recipientId = recipientField.getText().trim();
            String subject = subjectField.getText().trim();
            String content = contentArea.getText().trim();

            if (!recipientId.isEmpty() && !subject.isEmpty() && !content.isEmpty()) {
                messageController.sendMessage(currentUserId, recipientId, subject, content);
                composeStage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Missing Information");
                alert.setContentText("Please fill in all fields.");
                alert.showAndWait();
            }
        });

        formBox.getChildren().addAll(
            new Label("To:"), recipientField,
            new Label("Subject:"), subjectField,
            new Label("Message:"), contentArea,
            sendButton
        );

        composeRoot.setCenter(formBox);
        Scene composeScene = new Scene(composeRoot, 400, 500);
        composeStage.setTitle("Compose Message");
        composeStage.setScene(composeScene);
        composeStage.show();
    }
} 