package Client.Controller.GUIControllers.Log;

import Client.Controller.GUIControllers.FxController;
import Client.Controller.GUIControllers.GUIControllerRegistry;
import Client.Controller.GUIControllers.GUIInController;
import Client.Controller.GUIControllers.GUIOutController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.Collections;
import java.util.List;

public class LogCentreController implements FxController {

    @FXML
    private TextField newLogTextArea;
    @FXML
    private ListView<String> previousLogTextArea;
    @FXML
    private Button registerNewLogButton;

    @FXML
    private void newEntry(){
        String newEntry = newLogTextArea.getText();
        if(newEntry.isEmpty()){
            return;
        }
        GUIOutController.getInstance().sendNewLogEntry(newEntry);
        newLogTextArea.clear();
    }
    public void    addLogsToUI(List<String> log) {
        previousLogTextArea.getItems().clear();
        Collections.reverse(log);
        for (String oneLog : log) {
            String[] parts = oneLog.split(",", 2);
            if (parts.length == 2) {
                String entry = parts[0].trim();
                String dateTimeRaw = parts[1].trim();

                String dateTimeShort = dateTimeRaw.replace('T', ' ');
                if (dateTimeShort.length() > 16) {
                    dateTimeShort = dateTimeShort.substring(0, 16);
                }

                String formatted = dateTimeShort + " | " + entry;
                previousLogTextArea.getItems().add(formatted);
            }
        }
    }
    @FXML
    public void initialize() {
        GUIControllerRegistry.getInstance().add(this.getClass().getName(), this);
        List<String> logs = GUIInController.getInstance().getLogs();

        addLogsToUI(logs);
    }

    @Override
    public void closeStage() {

    }
}
