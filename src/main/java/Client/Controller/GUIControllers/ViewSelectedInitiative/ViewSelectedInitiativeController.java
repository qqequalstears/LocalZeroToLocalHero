package Client.Controller.GUIControllers.ViewSelectedInitiative;

import Client.Controller.GUIControllers.GUIInController;
import Client.Controller.GUIControllers.GUIOutController;
import Client.Model.Initiative.Children.CarPool;
import Client.Model.Initiative.Children.GarageSale;
import Client.Model.Initiative.Children.Gardening;
import Client.Model.Initiative.Children.ToolSharing;
import Client.Model.Initiative.Parent.Initiative;
import Client.Model.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class ViewSelectedInitiativeController implements Client.Controller.GUIControllers.FxController {

    private Initiative initiative;
    private GUIInController guiInController;
    private GUIOutController guiOutController;

    @FXML
    private Label initName;
    @FXML
    private TextField descriptionField;
    @FXML
    private VBox commentsBox;
    @FXML
    private TextField txtInputField;
    @FXML
    private ScrollPane commentsScrollPane;
    @FXML
    private Button joinButton;
    @FXML
    private Button leaveButton;
    @FXML
    private Button neighborhoodButton;
    @FXML
    private Button addCommentButton;
    @FXML
    private TextField participantsField;
    
    // CarPool fields
    @FXML
    private Label destinationLabel;
    @FXML
    private TextField destinationField;
    @FXML
    private Label passengersLabel;
    @FXML
    private TextField passengersField;
    
    // GarageSale fields
    @FXML
    private Label itemsToSellLabel;
    @FXML
    private TextField itemsToSellField;
    @FXML
    private Label sellerLabel;
    @FXML
    private TextField sellerField;
    
    // Gardening fields
    @FXML
    private Label needsHelpLabel;
    @FXML
    private TextField needsHelpField;
    @FXML
    private Label helpersLabel;
    @FXML
    private TextField helpersField;
    
    // ToolSharing fields
    @FXML
    private Label loanerLabel;
    @FXML
    private TextField loanerField;
    @FXML
    private Label lenderLabel;
    @FXML
    private TextField lenderField;

    @FXML
    public void initialize() {
        this.guiInController = GUIInController.getInstance();
        this.guiOutController = GUIOutController.getInstance();
        // Register this controller for refresh
        Client.Controller.GUIControllers.GUIControllerRegistry.getInstance().add(this.getClass().getName(), (Client.Controller.GUIControllers.FxController) this);
        //Start invisible so dont crash if bad type.
        txtInputField.setVisible(false);
        commentsBox.setVisible(false);
        commentsScrollPane.setVisible(false);
        populateFields();
    }

    @FXML
    private void handleJoinInitiative() {
        String initiativeTitle = GUIInController.getInstance().getCurrentlySelectedInitiative();
        if (initiativeTitle != null) {
            guiOutController.joinInitiative(initiativeTitle);
        }
    }

    @FXML
    private void handleLeaveInitiative() {
        String initiativeTitle = GUIInController.getInstance().getCurrentlySelectedInitiative();
        if (initiativeTitle != null) {
            guiOutController.leaveInitiative(initiativeTitle);
        }
    }

    @FXML
    private void handleShowNeighborhood() {
        guiOutController.getNeighborhoodInitiatives();
    }

    @FXML
    private void handleAddComment() {
        if (initiative == null || txtInputField == null || commentsBox == null || commentsScrollPane == null) return;

        String newComment = txtInputField.getText().trim();
        if (newComment.isEmpty()) return;

        String initiativeTitle = GUIInController.getInstance().getCurrentlySelectedInitiative();
        if (initiativeTitle != null) {
            guiOutController.addComment(initiativeTitle, newComment);
            txtInputField.clear();
        }
    }

    public void setInitiative(Initiative initiative) {
        this.initiative = initiative;
    }

    public void populateFields() {
        Initiative selectedInitiative = GUIInController.getInstance().getSelectedInitiativeObject();
        String title = GUIInController.getInstance().getCurrentlySelectedInitiative();
        String type = GUIOutController.getInstance().getConnectionController().getCurrentInitiativeType(title);

        if (selectedInitiative == null || type.equals("BadType")) return;

        populateCommonFields(selectedInitiative);
        updateJoinLeaveButtons(selectedInitiative);

        switch (type) {
            case "CarPool":
                populateCarPoolFields((CarPool) selectedInitiative);
                break;
            case "Garage Sale":
                populateGarageSaleFields((GarageSale) selectedInitiative);
                break;
            case "Gardening":
                populateGardeningFields((Gardening) selectedInitiative);
                break;
            case "ToolSharing":
                populateToolSharingFields((ToolSharing) selectedInitiative);
                break;
        }
    }

    private void populateCommonFields(Initiative initiative) {
        this.initiative = initiative;
        initName.setText(initiative.getTitle());
        descriptionField.setText(initiative.getDescription());

        // Show participants
        List<String> participants = initiative.getParticipants();
        if (participants != null && !participants.isEmpty()) {
            participantsField.setText(String.join(", ", participants));
        } else {
            participantsField.setText("No participants yet");
        }

        // Only show comment input for participants
        String userEmail = guiOutController.getConnectedUserEmail();
        boolean isParticipant = userEmail != null && participants != null && participants.contains(userEmail);
        
        txtInputField.setVisible(isParticipant);
        addCommentButton.setVisible(isParticipant);
        commentsBox.setVisible(true);
        commentsScrollPane.setVisible(true);

        // Clear existing comments
        commentsBox.getChildren().clear();

        // Display threaded comments
        System.out.println("[DEBUG] Initiative has " + initiative.getCommentList().size() + " comments");
        for (Initiative.Comment comment : initiative.getCommentList()) {
            System.out.println("[DEBUG] Adding comment to UI: " + comment.getContent());
            addCommentToUI(comment, 0);
        }
    }

    private void addCommentToUI(Initiative.Comment comment, int indentLevel) {
        VBox commentContainer = new VBox(5);
        commentContainer.setStyle("-fx-padding: " + (indentLevel * 20) + " 0 0 0; -fx-background-color: " + 
                                 (indentLevel % 2 == 0 ? "#FFFFFF" : "#F5F5F5") + "; -fx-border-radius: 5;");

        // Comment header with author and likes
        HBox headerBox = new HBox(10);
        Label authorLabel = new Label(comment.getAuthorEmail());
        authorLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #5b9e46;");
        
        Label likesLabel = new Label("❤ " + comment.getLikes());
        
        // Only show like and reply buttons for participants
        String userEmail = guiOutController.getConnectedUserEmail();
        Initiative selectedInitiative = GUIInController.getInstance().getSelectedInitiativeObject();
        boolean isParticipant = selectedInitiative != null && userEmail != null && 
                               selectedInitiative.getParticipants().contains(userEmail);
        
        headerBox.getChildren().addAll(authorLabel, likesLabel);
        
        if (isParticipant) {
            Button likeButton = new Button("Like");
            likeButton.setStyle("-fx-background-color: #FFE5E5; -fx-text-fill: #FF6B6B;");
            likeButton.setOnAction(e -> handleLikeComment(comment.getId()));
            
            Button replyButton = new Button("Reply");
            replyButton.setStyle("-fx-background-color: #E5F3FF; -fx-text-fill: #4A90E2;");
            replyButton.setOnAction(e -> handleReplyComment(comment.getId()));
            
            headerBox.getChildren().addAll(likeButton, replyButton);
        }

        // Comment content
        Label contentLabel = new Label(comment.getContent());
        contentLabel.setWrapText(true);
        contentLabel.setMaxWidth(300 - (indentLevel * 20));

        commentContainer.getChildren().addAll(headerBox, contentLabel);
        commentsBox.getChildren().add(commentContainer);

        // Add replies recursively
        for (Initiative.Comment reply : comment.getReplies()) {
            addCommentToUI(reply, indentLevel + 1);
        }
    }

    private void handleLikeComment(String commentId) {
        System.out.println("[DEBUG] handleLikeComment called for commentId: " + commentId);
        String initiativeTitle = GUIInController.getInstance().getCurrentlySelectedInitiative();
        System.out.println("[DEBUG] Initiative title: " + initiativeTitle);
        if (initiativeTitle != null) {
            System.out.println("[DEBUG] Sending like comment request to server");
            guiOutController.likeComment(initiativeTitle, commentId);
        } else {
            System.out.println("[DEBUG] ERROR: Initiative title is null");
        }
    }

    private void handleReplyComment(String parentCommentId) {
        System.out.println("[DEBUG] handleReplyComment called for parentCommentId: " + parentCommentId);
        String replyContent = txtInputField.getText().trim();
        System.out.println("[DEBUG] Reply content: " + replyContent);
        
        if (replyContent.isEmpty()) {
            System.out.println("[DEBUG] Reply content is empty, showing error");
            guiInController.notifyUser("Please enter a reply");
            return;
        }

        String initiativeTitle = GUIInController.getInstance().getCurrentlySelectedInitiative();
        System.out.println("[DEBUG] Initiative title: " + initiativeTitle);
        if (initiativeTitle != null) {
            System.out.println("[DEBUG] Sending reply comment request to server");
            guiOutController.replyComment(initiativeTitle, replyContent, parentCommentId);
            txtInputField.clear();
        } else {
            System.out.println("[DEBUG] ERROR: Initiative title is null");
        }
    }

    private void updateJoinLeaveButtons(Initiative initiative) {
        String userEmail = guiOutController.getConnectedUserEmail();
        if (userEmail == null) {
            joinButton.setDisable(true);
            leaveButton.setDisable(true);
            return;
        }

        boolean isParticipant = initiative.getParticipants().contains(userEmail);
        boolean isFull = initiative.isFull();

        joinButton.setDisable(isParticipant || isFull);
        leaveButton.setDisable(!isParticipant);

        if (isFull && !isParticipant) {
            joinButton.setText("Full");
        } else {
            joinButton.setText("Join Initiative");
        }
    }

    private void populateCarPoolFields(CarPool cp) {
        destinationLabel.setVisible(true);
        destinationField.setVisible(true);
        passengersLabel.setVisible(true);
        passengersField.setVisible(true);
        
        destinationField.setText(cp.getDestination() != null ? cp.getDestination() : "");

        List<String> passengerNames = new ArrayList<>();
        if (cp.getPassengers() != null) {
            for (User u : cp.getPassengers()) {
                passengerNames.add(u.getName());
            }
        }
        passengersField.setText(String.join(", ", passengerNames));
    }

    private void populateGarageSaleFields(GarageSale gs) {
        itemsToSellLabel.setVisible(true);
        itemsToSellField.setVisible(true);
        sellerLabel.setVisible(true);
        sellerField.setVisible(true);
        
        itemsToSellField.setText(gs.getItemsToSell() != null ? gs.getItemsToSell() : "");
        sellerField.setText(gs.getSeller() != null ? gs.getSeller().getName() : "");
    }

    private void populateGardeningFields(Gardening g) {
        needsHelpLabel.setVisible(true);
        needsHelpField.setVisible(true);
        helpersLabel.setVisible(true);
        helpersField.setVisible(true);
        
        needsHelpField.setText(g.getNeedsHelp() != null ? g.getNeedsHelp().getName() : "");

        List<String> helperNames = new ArrayList<>();
        if (g.getHelpers() != null) {
            for (User u : g.getHelpers()) {
                helperNames.add(u.getName());
            }
        }
        helpersField.setText(String.join(", ", helperNames));
    }

    private void populateToolSharingFields(ToolSharing ts) {
        loanerLabel.setVisible(true);
        loanerField.setVisible(true);
        lenderLabel.setVisible(true);
        lenderField.setVisible(true);
        
        loanerField.setText(ts.getLoaner() != null ? ts.getLoaner().getName() : "");
        lenderField.setText(ts.getLender() != null ? ts.getLender().getName() : "");
    }

    @Override
    public void closeStage() {
        // Find and close the stage for this controller if needed
        if (initName != null && initName.getScene() != null) {
            javafx.stage.Window window = initName.getScene().getWindow();
            if (window instanceof javafx.stage.Stage) {
                ((javafx.stage.Stage) window).close();
            }
        }
    }
}
