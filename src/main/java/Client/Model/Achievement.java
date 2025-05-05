package Client.Model;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;

/**
 * Object representing Achievements
 *
 * @author MartinFrick
 * @version 250416_0
 */
public class Achievement implements ISavableObject{

    private Image image;
    private String name;
    private Integer progress;
    private List<Integer> milestones;
    private String description;

    //Constructor for testing
    public Achievement(String name, Integer progress) {
        this.name=name;
        this.progress= progress;
    }
    public Achievement(String name) {
        this.name=name;
        this.progress= 0;
    }

    public Achievement(Image image, String name, Integer progress, List<Integer> milestones, String description) {
        this.image = image;
        this.name = name;
        this.progress = progress;
        this.milestones = new ArrayList<>(milestones);
        this.description = description;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public List<Integer> getMilestones() {
        return milestones;
    }

    public void setMilestones(List<Integer> milestones) {
        this.milestones = milestones;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getSaveString() {
        return String.join(",", name,String.valueOf(progress), description, getMilestonesAsString(), "PLACEHOLDER FOR ACHIEVEMENT IMAGE");

    }

    private String getMilestonesAsString() {
        StringBuilder returnString = new StringBuilder(String.valueOf(milestones.get(0)));
        for (int i = 1; i < milestones.size(); i++) {
            returnString.append("-").append(milestones.get(i));
        }
        return returnString.toString();
    }
}
