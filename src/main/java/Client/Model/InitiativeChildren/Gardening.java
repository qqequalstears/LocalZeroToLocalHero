package Client.Model.InitiativeChildren;

import Client.Model.Achievement;
import Client.Model.InitiativeParent.Initiative;
import Client.Model.Log;
import Client.Model.PLACEHOLDERcategory;
import Client.User;

import java.util.List;

/**
 *  Object representing initiative of gardening.
 *
 *
 * @author MartinFrick
 * @version 250417_0
 */
public class Gardening extends Initiative implements Log {

    public Gardening(String title, String description, String location, String duration, String startTime, List<PLACEHOLDERcategory> categories, List<User> participants, List<String> comments, List<String> likes, boolean isPublic, List<Achievement> achievements) {
        super(title, description, location, duration, startTime, categories, participants, comments, likes, isPublic, achievements);
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
