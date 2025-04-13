package View.Home;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {
    public void creatHomeStage() {
        Stage recipeStage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/Home/Home.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        recipeStage.setScene(new Scene(root, 600, 400));
        recipeStage.setResizable(false);
        recipeStage.show();
    }
}
