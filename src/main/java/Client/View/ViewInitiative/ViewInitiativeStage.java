package Client.View.ViewInitiative;

import Client.View.StageCreator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewInitiativeStage implements StageCreator {

    private Stage initiativeStage;

    public ViewInitiativeStage( ) {
        
    }

    @Override
    public void createStage() {
        initiativeStage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/Initiative/InitiativeView.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        initiativeStage.setScene(new Scene(root, 800, 700));
        initiativeStage.setResizable(false);
        initiativeStage.show();
    }

    public void closeStage() {
        if (initiativeStage != null) {
            initiativeStage.close();
        }
    }

    public Stage getStage() {
        return initiativeStage;
    }

}
