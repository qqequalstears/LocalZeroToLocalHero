package Client.Model.Initiative.Children;

import Client.Model.Achievement;
import Client.Model.Initiative.Parent.Initiative;
import Client.Model.Log;
import Client.Model.User;
import Server.Model.AchievementTracker;

import java.util.List;

/**
 * Object representing initiative of loaning a tool.
 *
 * @author MartinFrick
 * @version 250417_0
 */
public class ToolSharing extends Initiative implements durationCalculation, Log {

    private User loaner;
    private User lender;

    public ToolSharing(String category, String title, String description, String location, String duration, String startTime, List<String> comments, List<String> likes, boolean isPublic, List<Achievement> achievements) {
        super(category, title, description, location, duration, startTime, comments, likes, isPublic, achievements);
    }

    public ToolSharing(String title, String description, String location, String duration, String startTime, String category, boolean isPublic) {
        super(title, description, location, duration, startTime, category, isPublic);
    }

    @Override
    public void startActivity() {
        System.out.println("HI, MY NAME IS TOOLSHARING, I AM TOLD TO 'START ACTIVITY' WHICH I AM, ITS JUST THAT THIS METHOD DOESNT DO ANYTHING YET");
    }

    @Override
    public void improveAchievements() {
        AchievementTracker.getInstance().improveAchievements(super.getAchievements(), this);
    }

    @Override
    public int durationCalc() {
        System.out.println("TOOLSHARING 'DURATION CALC' RETURNING 66 HEHEHEHEHE ");
        return 66;
    }

    @Override
    public void Log() {
        System.out.println("HI, MY NAME IS TOOLSHARING LOG BLOGGALOGGABLOGGIBLOGGBLOGGLOGG LOG");
    }

    public User getLoaner() {
        return loaner;
    }

    public void setLoaner(User loaner) {
        this.loaner = loaner;
    }

    public User getLender() {
        return lender;
    }

    public void setLender(User lender) {
        this.lender = lender;
    }
}
