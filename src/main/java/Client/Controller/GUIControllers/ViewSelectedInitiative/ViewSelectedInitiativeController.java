package Client.Controller.GUIControllers.ViewSelectedInitiative;

import Client.Controller.GUIControllers.GUIInController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.List;

public class ViewSelectedInitiativeController {

    @FXML
    private Label initName;
    @FXML
    private VBox commentsBox;
    @FXML
    private TextField txtInputField;
    @FXML
    private ScrollPane commentsScrollPane;


    @FXML
    public void initialize() {

        String initiative = GUIInController.getInstance().getCurrentlySelectedInitiative();

        // TEMP: Simulate a few comments — replace with real data later
        List<String> comments = List.of("Welcome to the initiative!", "Looks good", "Needs improvement");

        for (String comment : comments) {
            Label commentLabel = new Label(comment);
            commentLabel.setWrapText(true); // allows multiline display
            commentLabel.setMaxWidth(300); // optional: control width
            commentsBox.getChildren().add(commentLabel);
        }
    }



    @FXML
    private void handleBack() {
        GUIInController.getInstance().createStage("HOME");
    }

    @FXML
    private void handleAddComment() {
        String newComment = txtInputField.getText().trim();
        if (newComment.isEmpty()) return;

        Label commentLabel = new Label(newComment);
        commentLabel.setWrapText(true);
        commentLabel.setMaxWidth(300);
        commentsBox.getChildren().add(commentLabel);
        // force scroll to bottom
        Platform.runLater(() ->
                commentsScrollPane.setVvalue(1.0)
        );


        txtInputField.clear();

        // Optionally add to model too
        // GUIInController.getInstance().getSelectedInitiative().addComment(newComment);
    }

}
