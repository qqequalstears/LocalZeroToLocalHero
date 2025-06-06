package Client.Model.Initiative.Parent;

import Client.Model.Achievement;
import Client.Model.ISavableObject;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Object utilizing Template Design Pattern to represent initiatives that actors in the system can take (depending on actor in question role).
 * Includes abstract method(s) "startActivity" & "improveAchievements".
 * <p>
 * 12/4 Removed participants variable to be implemented in subclasses if/as needed.
 * 12/4 Removed categories variable as it is represented as the implementation of this class subclasses and is not necessary to save.
 *
 * @author MartinFrick
 * @version 250421_0
 */

public abstract class Initiative implements ISavableObject {

    private String title;
    private String description;
    private String location;
    private String duration;
    private String startTime;
    private String category;
    private List<String> comments = new ArrayList<>();
    private List<String> likes = new ArrayList<>();
    private List<Achievement> achievements = new ArrayList<>();
    private boolean isPublic;
    private List<String> participants = new ArrayList<>(); // user emails
    private List<Comment> commentList = new ArrayList<>();


    public Initiative(String category, String title, String description, String location, String duration, String startTime, List<String> comments, List<String> likes, boolean isPublic, List<Achievement> achievements)
    {
        this.title = title;
        this.description = description;
        this.location = location;
        this.duration = duration;
        this.startTime = startTime;
        this.comments = new ArrayList<>(comments);
        this.likes = new ArrayList<>(likes);
        this.isPublic = isPublic;
    }

    public Initiative(String title, String description, String location, String duration, String startTime, String category, boolean isPublic) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.duration = duration;
        this.startTime = startTime;
        this.category = category;
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

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public boolean join(String userEmail) {
        if (!participants.contains(userEmail) && !isFull()) {
            participants.add(userEmail);
            return true;
        }
        return false;
    }

    public boolean leave(String userEmail) {
        return participants.remove(userEmail);
    }

    public boolean isFull() {
        return false;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public void addComment(Comment comment) {
        commentList.add(comment);
    }

    public static class Comment {
        private String id;
        private String authorEmail;
        private String content;
        private String parentId; // null if top-level
        private List<Comment> replies = new ArrayList<>();
        private int likes = 0;
        private List<String> likedBy = new ArrayList<>();

        public Comment(String id, String authorEmail, String content, String parentId) {
            this.id = id;
            this.authorEmail = authorEmail;
            this.content = content;
            this.parentId = parentId;
        }

        public String getId() {
            return id;
        }

        public String getAuthorEmail() {
            return authorEmail;
        }

        public String getContent() {
            return content;
        }

        public String getParentId() {
            return parentId;
        }

        public List<Comment> getReplies() {
            return replies;
        }

        public int getLikes() {
            return likes;
        }

        public List<String> getLikedBy() {
            return likedBy;
        }

        public void addReply(Comment reply) {
            replies.add(reply);
        }

        public boolean like(String userEmail) {
            if (!likedBy.contains(userEmail)) {
                likedBy.add(userEmail);
                likes++;
                return true;
            }
            return false;
        }
    }

    @Override
    public String getSaveString() {
        return String.join(",", title, description, location, duration, startTime, getCommentsAsString(), getLikesAsString(), getAchievementsAsString(), String.valueOf(isPublic));
    }

    private CharSequence getCommentsAsString() {
        StringBuilder returnString = new StringBuilder(String.valueOf(comments.get(0)));
        for (int i = 1; i < comments.size(); i++) {
            returnString.append("-").append(comments.get(i));
        }
        return returnString.toString();
    }

    private CharSequence getLikesAsString() {
        StringBuilder returnString = new StringBuilder(String.valueOf(likes.get(0)));
        for (int i = 1; i < likes.size(); i++) {
            returnString.append("-").append(likes.get(i));
        }
        return returnString.toString();
    }

    private CharSequence getAchievementsAsString() {
        StringBuilder returnString = new StringBuilder(String.valueOf(achievements.get(0)));
        for (int i = 1; i < achievements.size(); i++) {
            returnString.append("-").append(achievements.get(i));
        }
        return returnString.toString();
    }

    public String getCategory() {
        return category;
    }
}
