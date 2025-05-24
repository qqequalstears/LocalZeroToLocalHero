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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class ViewSelectedInitiativeController {

    private Initiative initiative;
    private GUIInController guiInController;
    private GUIOutController guiOutController;


    @FXML
    private Label initName;
    @FXML
    private VBox commentsBox;
    @FXML
    private TextField txtInputField;
    @FXML
    private ScrollPane commentsScrollPane;
    @FXML
    private TextField destinationField;
    @FXML
    private TextField passengersField;
    @FXML
    private TextField itemsToSellField;
    @FXML
    private TextField sellerField;
    @FXML
    private TextField needsHelpField;
    @FXML
    private TextField helpersField;
    @FXML
    private TextField loanerField;
    @FXML
    private TextField lenderField;




    @FXML
    public void initialize() {

        this.guiInController = GUIInController.getInstance();
        this.guiOutController = GUIOutController.getInstance();

        //Start invisible so dont crash if bad type.
        txtInputField.setVisible(false);
        commentsBox.setVisible(false);
        commentsScrollPane.setVisible(false);

        populateFields();

    }


  /*  @FXML
    private void handleBack() {
        GUIInController.getInstance().createStage("HOME");
    }*/

    @FXML
    private void handleAddComment() {
        if (initiative == null || txtInputField == null || commentsBox == null || commentsScrollPane == null) return;

        String newComment = txtInputField.getText().trim();
        if (newComment.isEmpty()) return;

        initiative.getComments().add(newComment);

        Label commentLabel = new Label(newComment);
        commentLabel.setWrapText(true);
        commentLabel.setMaxWidth(300);
        commentsBox.getChildren().add(commentLabel);

        Platform.runLater(() -> commentsScrollPane.setVvalue(1.0));

        txtInputField.clear();
    }

    public void setInitiative(Initiative initiative) {
        this.initiative = initiative;
    }

    public void populateFields() {
        Initiative selectedInitiative = GUIInController.getInstance().getSelectedInitiativeObject();
        String title = GUIInController.getInstance().getCurrentlySelectedInitiative();
        String type = GUIOutController.getInstance().getConnectionController().getCurrentInitiativeType(title);

        if (selectedInitiative == null || type == "BadType"){
            System.out.println("_____________________________________________________________________________");
            System.out.println("BAD TYPE");
            System.out.println("_____________________________________________________________________________");
            return;
        }

        populateCommonFields(selectedInitiative);

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
        initName.setText(initiative.getTitle());

        txtInputField.setVisible(true);
        commentsBox.setVisible(true);
        commentsScrollPane.setVisible(true);

        for (String comment : initiative.getComments()) {
            Label commentLabel = new Label(comment);
            commentLabel.setWrapText(true);
            commentLabel.setMaxWidth(300);
            commentsBox.getChildren().add(commentLabel);
        }
    }

    private void populateCarPoolFields(CarPool cp) {
        destinationField.setVisible(true);
        passengersField.setVisible(true);
        destinationField.setText(cp.getDestination());

        List<String> passengerNames = new ArrayList<>();
        for (User u : cp.getPassengers()) {
            passengerNames.add(u.getName());
        }
        passengersField.setText(String.join(", ", passengerNames));
    }

    private void populateGarageSaleFields(GarageSale gs) {
        itemsToSellField.setVisible(true);
        sellerField.setVisible(true);
        itemsToSellField.setText(gs.getItemsToSell());
        sellerField.setText(gs.getSeller() != null ? gs.getSeller().getName() : "");
    }

    private void populateGardeningFields(Gardening g) {
        needsHelpField.setVisible(true);
        helpersField.setVisible(true);
        needsHelpField.setText(g.getNeedsHelp() != null ? g.getNeedsHelp().getName() : "");

        List<String> helperNames = new ArrayList<>();
        for (User u : g.getHelpers()) {
            helperNames.add(u.getName());
        }
        helpersField.setText(String.join(", ", helperNames));
    }

    private void populateToolSharingFields(ToolSharing ts) {
        loanerField.setVisible(true);
        lenderField.setVisible(true);
        loanerField.setText(ts.getLoaner() != null ? ts.getLoaner().getName() : "");
        lenderField.setText(ts.getLender() != null ? ts.getLender().getName() : "");
    }







}
