package Client.Model.Initiative.Children;

import Client.Model.Achievement;
import Client.Model.Initiative.Parent.Initiative;
import Client.Model.Log;
import java.util.List;

/**
 *  Object representing initiative of gardening.
 *
 *  21/4 Updated according to superclass changes. /MartinFrick
 *
 * @author MartinFrick
 * @version 250421_0
 */

public class Gardening extends Initiative implements Log {

    public Gardening(String category, String title, String description, String location, String duration, String startTime, List<String> comments, List<String> likes, boolean isPublic, List<Achievement> achievements) {
        super(category, title, description, location, duration, startTime, comments, likes, isPublic, achievements);
    }

    @Override
    public void startActivity() {
        System.out.println("HI, MY NAME IS GARDENING, I AM TOLD TO 'START ACTIVITY' WHICH I AM, ITS JUST THAT THIS METHOD DOESNT DO ANYTHING YET");
    }

    @Override
    public void improveAchievements() {
        System.out.println("HI, MY NAME IS GARDENING, I AM TOLD TO 'IMPROVE ACHIEVEMENTS' WHICH I AM, ITS JUST THAT THIS METHOD DOESNT DO ANYTHING YET");
    }

    @Override
    public void Log() {
        System.out.println("HI, MY NAME IS GARDENING AND I LOGGGGGGGGGGGGGGGGGGGGGGGGGGGG LOG LOG LOG");
    }


}
