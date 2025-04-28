package Client.Controller.GUIControllers.Home;

import Client.Controller.GUIControllers.FxController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class HomeLeftController implements FxController {
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

        initiativesListview.setOnMouseClicked(event -> {
            String selectedInitiative = (String) initiativesListview.getSelectionModel().getSelectedItem();
            if (selectedInitiative != null) {
                //mediator.notify("NEWSTAGE","");
            }
        });
    }

    @FXML
    public void createInitiative() {

    }

    @Override
    public void closeStage() {
        Stage stage = (Stage) createButton.getScene().getWindow();
        stage.close();
    }
}
