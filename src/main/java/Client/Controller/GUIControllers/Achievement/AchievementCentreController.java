package Client.Controller.GUIControllers.Achievement;

import Client.Model.Achievement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class AchievementCentreController {
    @FXML private FlowPane achievementFlowPane;

    @FXML
    public void initialize() {
        //List<Achievement> achievements = GUIOutController.getInstance().getConnectedUser();
        List<Achievement> achievements = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            achievements.add(new Achievement("Achievement " + (i + 1),i*10));
        }

        for(Achievement achievement : achievements) {
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Achievement/AchievementCard.fxml"));
                VBox card = loader.load();
                AchievementCardController controller = loader.getController();
                controller.setData(achievement);
                achievementFlowPane.getChildren().add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
