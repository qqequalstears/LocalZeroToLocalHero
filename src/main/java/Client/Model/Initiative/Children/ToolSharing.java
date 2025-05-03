package Client.Model.Initiative.Children;

import Client.Model.Achievement;
import Client.Model.Initiative.Parent.Initiative;
import Client.Model.Log;
import Server.Model.AchievementTracker;

import java.util.List;

/**
 *  Object representing initiative of loaning a tool.
 *
 *
 * @author MartinFrick
 * @version 250417_0
 */
public class ToolSharing extends Initiative implements durationCalculation, Log {

    public ToolSharing(String title, String description, String location, String duration, String startTime, List<String> comments, List<String> likes, boolean isPublic, List<Achievement> achievements) {
        super(title, description, location, duration, startTime, comments, likes, isPublic, achievements);
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
}
