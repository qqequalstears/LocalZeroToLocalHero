package Client.Controller.GUIControllers.Home;

import Client.Controller.GUIControllers.FxController;
import Client.Controller.GUIControllers.GUIControllerRegistry;
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

    @FXML
    private Button onlineUsersButton;

    @FXML
    private Button allUsersButton;
    private ObservableList<String> usernames = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        GUIControllerRegistry.getInstance().add(this.getClass().getName(), this);
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
        usernames.add("hej");
        usernames.add("på");
        usernames.add("DIG");
    }

    public void setOnlineUsers(List<String> listOfUser) {
        usernames.setAll(listOfUser);
    }


    @Override
    public void closeStage() {
        Stage stage = (Stage) onlineUsersButton.getScene().getWindow();
        stage.close();
    }
}
