package Client.Model.Initiative.Children;

import Client.Model.Achievement;
import Client.Model.Initiative.Parent.Initiative;
import Client.Model.Log;
import Client.Model.User;
import Server.Model.AchievementTracker;

import java.util.ArrayList;
import java.util.List;

/**
 * Object representing initiative of a garage sale.
 * <p>
 * 21/4 Updated according to superclass changes. /MartinFrick
 *
 * @author MartinFrick
 * @version 250421_0
 */

public class GarageSale extends Initiative implements Sale, Log {
    private String itemsToSell;
    private User seller;
    List<User> participants;


    public GarageSale(String title, String description, String location, String duration, String startTime, List<String> comments, List<String> likes, boolean isPublic, List<Achievement> achievements) {
        super(title, description, location, duration, startTime, comments, likes, isPublic, achievements);
    }

    public GarageSale(String title, String description, String location, String duration, String startTime, String sellList, String category, boolean isPublic) {
        super(title, description, location, duration, startTime, category, isPublic);
        this.itemsToSell = sellList;
        participants = new ArrayList<>();
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
        AchievementTracker.getInstance().improveAchievements(super.getAchievements(), this);
    }

    @Override
    public void Log() {
        System.out.println("YEEEHAW, GarageSale is logging hell yea!");
    }

    public String getItemsToSell() {
        return itemsToSell;
    }
}
