package Client.View.Login;

import Client.View.StageCreator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LogInStage extends Application implements StageCreator {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login/LoginStage.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Welcome to LocalZeroToHero");

        stage.show();
    }

    @Override
    public void createStage() {
        Stage recipeStage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/Login/LoginStage.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        recipeStage.setScene(new Scene(root, 600, 400));
        recipeStage.setResizable(false);
        recipeStage.show();
    }
}
