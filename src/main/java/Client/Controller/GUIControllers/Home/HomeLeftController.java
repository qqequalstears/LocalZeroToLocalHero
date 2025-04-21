package Client.Controller.GUIControllers.Home;

import Client.Controller.Mediators.Mediator;
import Client.Controller.Mediators.MediatorManager;
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
    private Mediator mediator;


    @FXML
    public void initialize() {
        mediator = MediatorManager.getInstance().getMediator("GUI");

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

        initiativesListview.setOnMouseClicked(event -> {
            String selectedInitiative = (String) initiativesListview.getSelectionModel().getSelectedItem();
            if (selectedInitiative != null) {
                //mediator.notify("NEWSTAGE","");
            }
        });
    }

    @FXML
    public void createInitiative() {
        mediator.notify("NEWSTAGE", "CREATEINITIATIVE");
    }
}
