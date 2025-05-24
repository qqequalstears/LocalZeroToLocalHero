package Client.Controller.GUIControllers.Home;

import Client.Controller.GUIControllers.FxController;
import Client.Controller.GUIControllers.GUIControllerRegistry;
import Client.Controller.GUIControllers.GUIOutController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.List;

public class HomeCentreController implements FxController {
    @FXML
    private ListView usersListview;

    @FXML
    private Label usersLabel;

    private ObservableList<String> usernames = FXCollections.observableArrayList();
    private GUIOutController guiOutController;

    @FXML
    public void initialize() {
        GUIControllerRegistry.getInstance().add(this.getClass().getName(), this);
        guiOutController = GUIOutController.getInstance();
        usersListview.setItems(usernames);
        usersListview.setCellFactory(listView -> new ListCell<String>() {
            @Override
            protected void updateItem(String username, boolean empty) {
                super.updateItem(username, empty);
                if (empty || username == null) {
                    setText(null);
                } else {
                    setText(String.join("\n", username));
                }
            }
        });

        usersListview.setOnMouseClicked(event -> {
            String selectedMail = (String) usersListview.getSelectionModel().getSelectedItem();
            if (selectedMail != null) {
                guiOutController.seeUserInfo(selectedMail);
            }
        });
    }

    public void setOnlineUsers(List<String> listOfUser) {
        usernames.setAll(listOfUser);
    }


    @Override
    public void closeStage() {
        Stage stage = (Stage) usersLabel.getScene().getWindow();
        stage.close();
    }
}
