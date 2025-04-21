package Client.Model.InitiativeChildren;

import Client.Model.Achievement;
import Client.Model.InitiativeParent.Initiative;
import Client.Model.Log;
import Client.Model.PLACEHOLDERcategory;
import Client.User;
import java.util.List;

/**
 *  Object representing initiative of some form of transportation.
 *
 *
 * @author MartinFrick
 * @version 250417_0
 */

public class Transportation extends Initiative implements durationCalculation, Log {

    public Transportation(String title, String description, String location, String duration, String startTime, List<PLACEHOLDERcategory> categories, List<User> participants, List<String> comments, List<String> likes, boolean isPublic, List<Achievement> achievements) {
        super(title, description, location, duration, startTime, categories, participants, comments, likes, isPublic, achievements);

    }

    @Override
    public void startActivity() {
        System.out.println("HI, MY NAME IS TRANSPORTATION, I AM TOLD TO 'START ACTIVITY' WHICH I AM, ITS JUST THAT THIS METHOD DOESNT DO ANYTHING YET");
    }

    @Override
    public void improveAchievements() {
        System.out.println("HI, MY NAME IS TRANSPORTATION, I AM TOLD TO 'IMPROVE ACHIEVEMENTS' WHICH I AM, ITS JUST THAT THIS METHOD DOESNT DO ANYTHING YET");
    }

    @Override
    public int durationCalc() {
        System.out.println("HI, MY NAME IS TRANSPORTATION, IM PERFORMING 'DURATION CALC', HEH, IT ALWAYS RETURNS 69 HEH.");
        return 69;
    }

    @Override
    public void Log() {
        System.out.println("HI, MY NAME IS TRANSPORTATION, I R GOOT KLOGGER LOGG LOGG LOGGGGGER HEHEHEHETITIHTIHTI");
    }
}
