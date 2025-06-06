package Client.Controller.GUIControllers.Home;

import Client.Controller.GUIControllers.FxController;
import Client.Controller.GUIControllers.GUIInController;
import Client.Controller.GUIControllers.GUIOutController;
import Client.Controller.GUIControllers.GUIControllerRegistry;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.List;


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
    private Button neighborhoodButton;
    @FXML
    private Button allInitiativesButton;
    private GUIOutController guiOutController;

    @FXML
    public void initialize() {
        this.guiInController = GUIInController.getInstance();
        this.guiOutController = GUIOutController.getInstance();
        GUIControllerRegistry.getInstance().add(this.getClass().getName(), this);
        
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

       /* initiativesListview.setOnMouseClicked(event -> {
            String selectedInitiative = (String) initiativesListview.getSelectionModel().getSelectedItem();
            if (selectedInitiative != null) {
//                System.out.println("Yoyoyoyoy");
                //mediator.notify("NEWSTAGE","");
            }
        });*/

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
        guiInController.populateViewSelectedInitiativeSceen();
    }

    /**
     *
     * Adds initiatives to the list
     * @autor Martin Frick
     *
     */
    public void setInitiatives (List<String> initiatives) {

        intitiatives.clear();
        intitiatives.addAll(initiatives);

    }

    public void updateInitiatives(List<String> titles) {
        System.out.println("[DEBUG] HomeLeftController.updateInitiatives called with " + titles.size() + " titles");
        System.out.println("[DEBUG] Titles: " + titles);
        System.out.println("[DEBUG] Current list view items: " + initiativesListview.getItems().size());
        
        Platform.runLater(() -> {
            intitiatives.clear();
            intitiatives.addAll(titles);
            initiativesListview.setItems(intitiatives);
            System.out.println("[DEBUG] List view updated with " + intitiatives.size() + " items");
            System.out.println("[DEBUG] Final list view items: " + initiativesListview.getItems().size());
        });
    }

    @FXML
    public void showNeighborhood() {
        try {
            if (guiOutController == null) {
                guiOutController = GUIOutController.getInstance();
            }
            
            // Check if we have a valid connection
            if (guiOutController.getConnectionController() == null) {
                guiInController.notifyUser("No connection to server. Please restart the application.");
                return;
            }
            
            String userEmail = guiOutController.getConnectedUserEmail();
            if (userEmail == null) {
                guiInController.notifyUser("Please log in first to view neighborhood initiatives.");
                return;
            }
            
            guiOutController.getNeighborhoodInitiatives();
        } catch (Exception e) {
            System.err.println("Error showing neighborhood initiatives: " + e.getMessage());
            guiInController.notifyUser("Error connecting to server. Please try again.");
        }
    }

    @FXML
    public void showAllInitiatives() {
        try {
            if (guiOutController == null) {
                guiOutController = GUIOutController.getInstance();
            }
            
            // Check if we have a valid connection
            if (guiOutController.getConnectionController() == null) {
                guiInController.notifyUser("No connection to server. Please restart the application.");
                return;
            }
            
            guiOutController.getAllInitiativesFromServer();
        } catch (Exception e) {
            System.err.println("Error showing all initiatives: " + e.getMessage());
            guiInController.notifyUser("Error connecting to server. Please try again.");
        }
    }

}
