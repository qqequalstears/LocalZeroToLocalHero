package Client.View.Email;

import Client.Controller.EmailController;
import Client.Model.Email;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class EmailView {
    private final EmailController emailController;
    private final String currentUserId;
    private final Stage stage;
    private final ListView<Email> inboxListView;
    private final TextArea emailContentArea;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");

    public EmailView(String userId) {
        this.emailController = EmailController.getInstance();
        this.currentUserId = userId;
        this.stage = new Stage();
        this.inboxListView = new ListView<>();
        this.emailContentArea = new TextArea();
        
        setupUI();
        loadInbox();
    }

    private void setupUI() {
        BorderPane root = new BorderPane();
        
        // Top menu
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem composeItem = new MenuItem("Compose");
        composeItem.setOnAction(e -> showComposeDialog());
        fileMenu.getItems().add(composeItem);
        menuBar.getMenus().add(fileMenu);
        
        // Left side - Inbox
        VBox inboxBox = new VBox(10);
        inboxBox.setPadding(new Insets(10));
        Label inboxLabel = new Label("Inbox");
        inboxListView.setPrefWidth(300);
        
        // Add Compose button above the inbox list
        Button composeButton = new Button("Compose");
        composeButton.setMaxWidth(Double.MAX_VALUE);
        composeButton.setOnAction(e -> showComposeDialog());
        
        // Set up the cell factory for the ListView
        inboxListView.setCellFactory(new Callback<ListView<Email>, ListCell<Email>>() {
            @Override
            public ListCell<Email> call(ListView<Email> param) {
                return new ListCell<Email>() {
                    @Override
                    protected void updateItem(Email email, boolean empty) {
                        super.updateItem(email, empty);
                        if (empty || email == null) {
                            setText(null);
                            setStyle("");
                        } else {
                            String displayText = String.format("%s\n%s\n%s",
                                email.getSubject(),
                                email.getSenderId(),
                                email.getTimestamp().format(DATE_FORMATTER));
                            setText(displayText);
                            
                            // Style unread emails
                            if (!email.isRead()) {
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
            (obs, oldVal, newVal) -> displayEmail(newVal));
        // Add Compose button and label to the VBox
        inboxBox.getChildren().addAll(composeButton, inboxLabel, inboxListView);
        
        // Center - Email content
        emailContentArea.setEditable(false);
        emailContentArea.setWrapText(true);
        
        root.setTop(menuBar);
        root.setLeft(inboxBox);
        root.setCenter(emailContentArea);
        
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Email Client");
        stage.setScene(scene);
    }

    private void loadInbox() {
        List<Email> emails = emailController.getInbox(currentUserId);
        inboxListView.getItems().clear();
        inboxListView.getItems().addAll(emails);
    }

    private void displayEmail(Email email) {
        if (email != null) {
            emailController.markEmailAsRead(currentUserId, email);
            emailContentArea.setText(
                "From: " + email.getSenderId() + "\n" +
                "Subject: " + email.getSubject() + "\n" +
                "Date: " + email.getTimestamp().format(DATE_FORMATTER) + "\n\n" +
                email.getContent()
            );
            // Refresh the inbox to update the unread status
            loadInbox();
        }
    }

    private void showComposeDialog() {
        Stage composeStage = new Stage();
        BorderPane composeRoot = new BorderPane();
        
        VBox formBox = new VBox(10);
        formBox.setPadding(new Insets(10));
        
        TextField toField = new TextField();
        toField.setPromptText("To:");
        
        TextField subjectField = new TextField();
        subjectField.setPromptText("Subject:");
        
        TextArea contentArea = new TextArea();
        contentArea.setPromptText("Write your message here...");
        contentArea.setPrefRowCount(10);
        
        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> {
            String recipientId = toField.getText();
            String subject = subjectField.getText();
            String content = contentArea.getText();
            
            if (!recipientId.isEmpty() && !subject.isEmpty() && !content.isEmpty()) {
                emailController.sendEmail(currentUserId, recipientId, subject, content);
                composeStage.close();
                loadInbox();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Missing Information");
                alert.setContentText("Please fill in all fields.");
                alert.showAndWait();
            }
        });
        
        formBox.getChildren().addAll(
            new Label("To:"), toField,
            new Label("Subject:"), subjectField,
            new Label("Message:"), contentArea,
            sendButton
        );
        
        composeRoot.setCenter(formBox);
        
        Scene composeScene = new Scene(composeRoot, 500, 400);
        composeStage.setTitle("Compose Email");
        composeStage.setScene(composeScene);
        composeStage.show();
    }

    public void show() {
        stage.show();
    }
} 