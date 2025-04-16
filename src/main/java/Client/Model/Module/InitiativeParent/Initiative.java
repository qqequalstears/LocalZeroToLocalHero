package Client.Model.Module.InitiativeParent;

import Client.Model.Module.Achievement;
import Client.Model.Module.PLACEHOLDERcategory;
import Client.Model.Module.User;

import java.util.ArrayList;
import java.util.List;

/**
 *  Object utilizing Template Design Pattern to represent initiative or actions that actors in the system can take depending (on its role).
 *  Includes abstract method(s) "startActivity" & "improveAchievements".
 *
 * @author MartinFrick
 * @version 250416_0
 */

public abstract class Initiative {

    private String title;
    private String description;
    private String location;
    private String duration;
    private String startTime;
    private List<PLACEHOLDERcategory> categories;
    private List<User> participants;
    private List<String> comments;
    private List<String> likes;
    private List<Achievement> achievements;
    private boolean isPublic = false;


    public Initiative(String title, String description, String location, String duration, String startTime, List<PLACEHOLDERcategory> categories, List<User> participants, List<String> comments, List<String> likes, boolean isPublic, List<Achievement> achievements) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.duration = duration;
        this.startTime = startTime;
        this.categories = new ArrayList<>(categories);
        this.participants = new ArrayList<>(participants);
        this.comments = new ArrayList<>(comments);
        this.likes = new ArrayList<>(likes);
        this.achievements = new ArrayList<>(achievements);
        this.isPublic = isPublic;
    }

    public abstract void startActivity();

    public abstract void improveAchievements();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public List<PLACEHOLDERcategory> getCategories() {
        return categories;
    }

    public void setCategories(List<PLACEHOLDERcategory> categories) {
        this.categories = categories;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<Achievement> achievements) {
        this.achievements = achievements;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
