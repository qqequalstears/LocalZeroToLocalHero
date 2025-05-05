package Client.Controller.GUIControllers.Achievement;
import Client.Controller.ConnectionController;
import Client.Controller.GUIControllers.GUIOutController;
import Client.Model.User;
import Client.Model.Achievement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class AchievementController {


    /*public void populateAchievements() {
        List<Achievement> achievements = GUIOutController.getInstance().getConnectedUser();

        for(Achievement achievement : achievements) {
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/View/Achievement/AchievementCard.fxml"));
            VBox card = loader.load();
            AchievementCardController controller = loader.getController();
            controller.setData(achievement);
            achievementsFlowPane.getChildren().add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

     */
}
