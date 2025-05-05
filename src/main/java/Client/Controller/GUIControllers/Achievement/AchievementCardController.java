package Client.Controller.GUIControllers.Achievement;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import Client.Model.Achievement;

import java.io.InputStream;

public class AchievementCardController {

    @FXML private ImageView achievementImage;
    @FXML private Label title;
    @FXML private ProgressBar progressBar;

    public void setData(Achievement achievement) {
        InputStream in = getClass().getResourceAsStream("/Achievement/Icons/winning.jpg");
        if (in == null) {
            System.out.println("Hittade inte bilden!");
        } else {
            Image image = new Image(in);
            achievementImage.setImage(image);
        }

        title.setText(achievement.getName());
        progressBar.setProgress(achievement.getProgress() / 100.0);
    }
}
