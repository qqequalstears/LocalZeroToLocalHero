package Client.Model.Initiative.Children;

import Client.Model.Achievement;
import Client.Model.Initiative.Parent.Initiative;
import Client.Model.Log;
import Client.Model.User;
import Server.Model.AchievementTracker;

import java.util.ArrayList;
import java.util.List;

/**
 * Object representing initiative of a car pool.
 * Changed 24/4 to be "CarPool" from "transportation". /MartinFrick
 * 21/4 Updated according to superclass changes. /MartinFrick
 *
 * @author MartinFrick
 * @version 250421_1
 */

public class CarPool extends Initiative implements durationCalculation, Log {
    private User driver;
    private List<User> passengers;
    private String numberOfSeats;

    private String destination;


    public CarPool(String destination, String category, String title, String description, String location, String duration, String startTime, List<String> comments, List<String> likes, boolean isPublic, List<Achievement> achievements) {
        super(category,  title,  description,  location,  duration,  startTime,  comments, likes,  isPublic, achievements);
    }

    public CarPool(String title, String description, String location, String duration, String startTime, String numberOfSeats, String category, boolean isPublic) {
        super(title, description, location, duration, startTime, category, isPublic);
        this.numberOfSeats = numberOfSeats;
        passengers = new ArrayList<>();
    }

    @Override
    public void startActivity() {
        System.out.println("HI, MY NAME IS TRANSPORTATION, I AM TOLD TO 'START ACTIVITY' WHICH I AM, ITS JUST THAT THIS METHOD DOESNT DO ANYTHING YET");
    }

    @Override
    public void improveAchievements() {
        AchievementTracker.getInstance().improveAchievements(super.getAchievements(), this);
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

    public String getNumberOfSeats() {
        return numberOfSeats;
    }
}
