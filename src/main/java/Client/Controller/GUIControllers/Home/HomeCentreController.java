package Client.Controller.GUIControllers.Home;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

public class HomeCentreController {
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
}
