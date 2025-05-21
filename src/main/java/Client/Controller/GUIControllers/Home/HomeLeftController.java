package Client.Controller.GUIControllers.Home;

import Client.Controller.GUIControllers.FxController;
import Client.Controller.GUIControllers.GUIInController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;


/**
 *
 *
 *
 * @author Martin Frick
 * Updated to add "editInitiativeButton" and functionality associated with it.
 * @date 2022-05-06
 * @author Anton Persson
 */

public class HomeLeftController implements FxController {
    @FXML
    private ListView initiativesListview;
    @FXML
    private Button createButton;
    private ObservableList<String> intitiatives = FXCollections.observableArrayList();
    private GUIInController guiInController;
    @FXML
    private Button openInitiativeButton;


    @FXML
    public void initialize() {
        guiInController = GUIInController.getInstance();
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
//                System.out.println("Yoyoyoyoy");
                //mediator.notify("NEWSTAGE","");
            }
        });

    }

    @FXML
    public void createInitiative() {
        guiInController.createStage("CREATEINITIATIVE");
    }

    @Override
    public void closeStage() {
        Stage stage = (Stage) createButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Opens the selected initiative
     * @autor Martin Frick
     */
    @FXML
    private void openSelectedInitiative() {
        String selected = (String) initiativesListview.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        guiInController.setCurrentlySelectedInitiative(selected);
        guiInController.createStage("OPENINITIATIVESTAGE");
    }

}
