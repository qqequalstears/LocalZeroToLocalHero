package Client.Model.Initiative.Children;

import Client.Model.Achievement;
import Client.Model.Initiative.Parent.Initiative;
import Client.Model.Log;

import java.util.List;

/**
 * Object representing initiative of a garage sale.
 *
 *  21/4 Updated according to superclass changes. /MartinFrick
 *
 * @author MartinFrick
 * @version 250421_0
 */

public class GarageSale extends Initiative implements Sale, Log {


    public GarageSale(String title, String description, String location, String duration, String startTime, List<String> comments, List<String> likes, boolean isPublic, List<Achievement> achievements) {
        super(title, description, location, duration, startTime, comments, likes, isPublic, achievements);
    }

    @Override
    public int transaction() {
        return 0;
    }

    @Override
    public void startActivity() {
        System.out.println("Im GARAGE SALE, I AM TOLD TO 'START ACTIVITY' WHICH I AM, ITS JUST THAT THIS METHOD DOESNT DO ANYTHING YET");
    }

    @Override
    public void improveAchievements() {
        System.out.println("Im GARAGE SALE, I AM TOLD TO 'IMPROVE ACHIEVEMENTS' WHICH I AM, ITS JUST THAT THIS METHOD DOESNT DO ANYTHING YET");
    }

    @Override
    public void Log() {
        System.out.println("YEEEHAW, GarageSale is logging hell yea!");
    }
}
