package Client.View.UserInfo;

import Client.View.StageCreator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UserInfoStage implements StageCreator {
    @Override
    public void createStage() {
        Stage recipeStage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/UserInfo/UserInfoStage.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        recipeStage.setScene(new Scene(root, 600, 350));
        recipeStage.setResizable(false);
        recipeStage.show();
    }
}
