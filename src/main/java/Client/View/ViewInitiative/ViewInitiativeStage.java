package Client.View.ViewInitiative;

import Client.View.StageCreator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewInitiativeStage implements StageCreator {

    private final String initiativeName;

    public ViewInitiativeStage(String initiativeName) {
        this.initiativeName = initiativeName;
    }

    @Override
    public void createStage() {
        Stage recipeStage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/Initiative/InitiativeView.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        recipeStage.setScene(new Scene(root, 600, 400));
        recipeStage.setResizable(false);
        recipeStage.show();
    }

}
