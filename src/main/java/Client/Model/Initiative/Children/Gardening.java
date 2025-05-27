package Client.Model.Initiative.Children;

import Client.Model.Achievement;
import Client.Model.Initiative.Parent.Initiative;
import Client.Model.Log;
import Client.Model.User;
import Server.Model.AchievementTracker;

import java.util.List;

/**
 * Object representing initiative of gardening.
 * <p>
 * 21/4 Updated according to superclass changes. /MartinFrick
 *
 * @author MartinFrick
 * @version 250421_0
 */

public class Gardening extends Initiative implements Log {

    private User needsHelp;
    private List<User> helpers = new java.util.ArrayList<>();

    public Gardening(String category, String title, String description, String location, String duration, String startTime, List<String> comments, List<String> likes, boolean isPublic, List<Achievement> achievements) {
        super(category, title, description, location, duration, startTime, comments, likes, isPublic, achievements);
    }

    public Gardening(String title, String description, String location, String duration, String startTime, String category, boolean isPublic) {
        super(title, description, location, duration, startTime, category, isPublic);
    }

    @Override
    public void startActivity() {
        System.out.println("HI, MY NAME IS GARDENING, I AM TOLD TO 'START ACTIVITY' WHICH I AM, ITS JUST THAT THIS METHOD DOESNT DO ANYTHING YET");
    }

    @Override
    public void improveAchievements() {
        AchievementTracker.getInstance().improveAchievements(super.getAchievements(), this);
    }

    @Override
    public void Log() {
        System.out.println("HI, MY NAME IS GARDENING AND I LOGGGGGGGGGGGGGGGGGGGGGGGGGGGG LOG LOG LOG");
    }

    public User getNeedsHelp() {
        return needsHelp;
    }

    public void setNeedsHelp(User needsHelp) {
        this.needsHelp = needsHelp;
    }

    public List<User> getHelpers() {
        return helpers;
    }

    public void setHelpers(List<User> helpers) {
        this.helpers = helpers;
    }

}
