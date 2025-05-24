package Client.View.Log;

import Client.View.StageCreator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LogStage implements StageCreator {
    public void createStage() {
        Stage recipeStage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/Log/LogStage.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        recipeStage.setScene(new Scene(root, 600, 400));
        recipeStage.setResizable(false);
        recipeStage.show();
    }
}
