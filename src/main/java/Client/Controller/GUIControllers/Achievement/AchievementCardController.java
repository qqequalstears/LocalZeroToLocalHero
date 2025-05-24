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
        InputStream in;
        if(achievement.getName().equalsIgnoreCase("Gardening")){
            in = getClass().getResourceAsStream("/Achievement/Icons/Gardening.jpg");
        }else if(achievement.getName().equalsIgnoreCase("Carpool")){
            in = getClass().getResourceAsStream("/Achievement/Icons/Carpool.jpg");
        }
        else if(achievement.getName().equalsIgnoreCase("Garage sale")){
            in = getClass().getResourceAsStream("/Achievement/Icons/GarageSale.jpg");
        }
        else if(achievement.getName().equalsIgnoreCase("Toolsharing")){
            in = getClass().getResourceAsStream("/Achievement/Icons/ToolSharing.jpg");
        }
        else {
            in = getClass().getResourceAsStream("/Achievement/Icons/winning.jpg");
        }

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
