package Client.Controller.GUIControllers.Home;

import Client.Controller.GUIControllers.FxController;
import Client.Controller.GUIControllers.GUIControllerRegistry;
import Client.Controller.GUIControllers.GUIInController;
import Client.Controller.GUIControllers.GUIOutController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.List;


/**
 *
 *
 *
 * @author Martin Frick
 * Updated to add "editInitiativeButton" and functionality associated with it.
 * @date 2022-05-06
 * @author Anton Persson
 *
 *
 */

public class HomeLeftController implements FxController {

    private GUIInController guiInController;
    private GUIOutController guiOutController;

    @FXML
    private ListView<String> initiativesListview;

    @FXML
    private Button createButton;
    private ObservableList<String> intitiatives = FXCollections.observableArrayList();

    @FXML
    private Button openInitiativeButton;


    @FXML
    public void initialize() {
        guiInController = GUIInController.getInstance();
        guiOutController = GUIOutController.getInstance();
        GUIControllerRegistry.getInstance().add(this.getClass().getName(),this);


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

        GUIOutController.getInstance().getConnectionController().updateInitiativesFromCache();

//        updateInitiatives(intitiatives);

//        loadInitiativesFromCSV();

       /* initiativesListview.setOnMouseClicked(event -> {
            String selectedInitiative = (String) initiativesListview.getSelectionModel().getSelectedItem();
            if (selectedInitiative != null) {
//                System.out.println("Yoyoyoyoy");
                //mediator.notify("NEWSTAGE","");
            }
        });*/

    }

    private void loadInitiativesFromCSV() {
        String csvFile = "src/main/java/Server/fileStorage/activeIntiative.csv";
        try (java.util.stream.Stream<String> lines = java.nio.file.Files.lines(java.nio.file.Paths.get(csvFile))) {
            lines.skip(1) // skip header
                 .map(line -> line.split(","))
                 .filter(parts -> parts.length > 1)
                 .map(parts -> parts[1].trim()) // get the title column
                 .forEach(intitiatives::add);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        String selected = initiativesListview.getSelectionModel().getSelectedItem();
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
        System.out.println("DID IT REACH ???? ---------------------------------------");
        for (String title : titles) {
            System.out.println(title);
        }
        intitiatives.clear();
        intitiatives.addAll(titles);
    }

}
