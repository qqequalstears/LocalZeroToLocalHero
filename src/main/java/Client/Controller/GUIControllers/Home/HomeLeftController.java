package Client.Controller.GUIControllers.Home;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class HomeLeftController {
    @FXML
    private ListView initiativesListview;
    @FXML
    private Button createButton;
    private ObservableList<String> intitiatives = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        initiativesListview.setItems(intitiatives);
        initiativesListview.setCellFactory(listView -> new ListCell<String>() {
            @Override
            protected void updateItem(String initiative, boolean empty) {
                super.updateItem(initiative, empty);
                if (empty || initiative == null) {
                    setText(null);
                } else {
                    setText(String.join("\n", initiative));
                }
            }
        });
        intitiatives.add("hej");
        intitiatives.add("på");
        intitiatives.add("DIG");
    }
}
