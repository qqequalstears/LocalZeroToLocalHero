package Client.Model.Module.InitiativeChildren;

import Client.Model.Module.Achievement;
import Client.Model.Module.InitiativeParent.Initiative;
import Client.Model.Module.PLACEHOLDERcategory;
import Client.Model.Module.User;


import java.util.List;

public class Transportation extends Initiative  {

    public Transportation(String title, String description, String location, String duration, String startTime, List<PLACEHOLDERcategory> categories, List<User> participants, List<String> comments, List<String> likes, boolean isPublic, List<Achievement> achievements) {
        super(title, description, location, duration, startTime, categories, participants, comments, likes, isPublic, achievements);

    }

    @Override
    public void startActivity(){
        setStartTime("10:00");
    }

    public void improveAchievments(){
        //super.setAchievements(getAchievements());
        System.out.println("whatever");
    }

}
