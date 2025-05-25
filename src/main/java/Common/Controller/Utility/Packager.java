package Common.Controller.Utility;

import Client.Model.Initiative.Children.CarPool;
import Client.Model.Initiative.Children.GarageSale;
import Client.Model.Initiative.Children.Gardening;
import Client.Model.Initiative.Children.ToolSharing;
import Client.Model.Initiative.Parent.Initiative;
import Client.Model.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class Packager {
    public byte[] converJsonToByte(JSONObject object) {
        return object.toString().getBytes(StandardCharsets.UTF_8);
    }

    public JSONObject createLoginJSON(String mail, String password) {
        JSONObject loginJson = new JSONObject();
        loginJson.put("type", "login");
        loginJson.put("mail", mail);
        loginJson.put("password", password);
        return loginJson;
    }

    public JSONObject createRegisterJSON(String mail, String password, String name, String city) {
        JSONObject registerJson = new JSONObject();
        registerJson.put("type", "register");
        registerJson.put("mail", mail);
        registerJson.put("password", password);
        registerJson.put("name", name);
        registerJson.put("city", city);
        return registerJson;
    }

    public JSONObject createIntentionJson(String intention) {
        JSONObject intentnionJson = new JSONObject();
        intentnionJson.put("type", intention);
        return intentnionJson;
    }

    public JSONObject createLogoutJson(String userMail) {
        JSONObject logoutJson = new JSONObject();
        logoutJson.put("type", "logout");
        logoutJson.put("mail", userMail);
        return logoutJson;
    }

    public JSONObject createNewInitiative(String name, String description, String location, String duration, String startTime, String numberOfSeats, String sellList, String category, boolean ispublic, String creator) {
        JSONObject newInitiativeJson = new JSONObject();
        newInitiativeJson.put("type", "createInitiative");
        newInitiativeJson.put("name", name);
        newInitiativeJson.put("description", description);
        newInitiativeJson.put("location", location);
        newInitiativeJson.put("duration", duration);
        newInitiativeJson.put("startTime", startTime);
        newInitiativeJson.put("numberOfSeats", numberOfSeats);
        newInitiativeJson.put("sellList", sellList);
        newInitiativeJson.put("category", category);
        newInitiativeJson.put("isPublic", ispublic);
        newInitiativeJson.put("creator", creator);
        return newInitiativeJson;
    }

    public JSONObject createCollectUserInfoJSON(String mailOfUser, String connectionMail) {
        JSONObject collectUserJSON = new JSONObject();
        collectUserJSON.put("type","collectUserInfo");
        collectUserJSON.put("mail", mailOfUser);
        collectUserJSON.put("connectionMail", connectionMail);
        return  collectUserJSON;
    }

    public JSONObject createUpdateRolesJSON(String[] roles, String mail) {
        JSONObject rolesJSON = new JSONObject();
        JSONArray rolesArray = new JSONArray();
        for (String role : roles) {
            JSONObject roleJSON = new JSONObject();
            roleJSON.put("role", role);
            rolesArray.put(roleJSON);
        }
        rolesJSON.put("type", "updateRoles");
        rolesJSON.put("roles", rolesArray);
        rolesJSON.put("mail", mail);
        return rolesJSON;
    }

    /**
     * @author Martin Frick
     * @date 2025-05-23
     * @param cp
     * @return JSONObject
     */
    public JSONObject createJsonForInitiativeCarPool(CarPool cp) {
        JSONObject initiativesJson = new JSONObject();

        initiativesJson.put("initiativeID", cp.getCategory());
        initiativesJson.put("title", cp.getTitle());
        initiativesJson.put("description", cp.getDescription());
        initiativesJson.put("location", cp.getLocation());
        initiativesJson.put("duration", cp.getDuration());
        initiativesJson.put("startTime", cp.getStartTime());
        initiativesJson.put("isPublic", cp.isPublic());
        initiativesJson.put("numberOfSeats", cp.getNumberOfSeats());
        initiativesJson.put("destination", cp.getDestination());

        // Add participants
        JSONArray participantsArray = new JSONArray();
        for (String participant : cp.getParticipants()) {
            participantsArray.put(participant);
        }
        initiativesJson.put("participants", participantsArray);

        // Add comments
        JSONArray commentsArray = new JSONArray();
        for (Initiative.Comment comment : cp.getCommentList()) {
            commentsArray.put(packComment(comment));
        }
        initiativesJson.put("comments", commentsArray);

        if (cp.getDriver() != null) {
            initiativesJson.put("driver", packUser(cp.getDriver()));
        }

        JSONArray passengerArray = new JSONArray();
        for (User passenger : cp.getPassengers()) {
            passengerArray.put(packUser(passenger));
        }
        initiativesJson.put("passengers", passengerArray);

        return initiativesJson;
    }

    /**
     * @author Martin Frick
     * @date 2025-05-23
     * @param gs
     * @return JSONObject
     */
    public JSONObject createJsonForInitiativeGarageSale(GarageSale gs) {
        JSONObject initiativesJson = new JSONObject();

        initiativesJson.put("initiativeID", gs.getCategory());
        initiativesJson.put("title", gs.getTitle());
        initiativesJson.put("description", gs.getDescription());
        initiativesJson.put("location", gs.getLocation());
        initiativesJson.put("duration", gs.getDuration());
        initiativesJson.put("startTime", gs.getStartTime());
        initiativesJson.put("isPublic", gs.isPublic());
        initiativesJson.put("itemsToSell", gs.getItemsToSell());

        // Add participants
        JSONArray participantsArray = new JSONArray();
        for (String participant : gs.getParticipants()) {
            participantsArray.put(participant);
        }
        initiativesJson.put("participants", participantsArray);

        // Add comments
        JSONArray commentsArray = new JSONArray();
        for (Initiative.Comment comment : gs.getCommentList()) {
            commentsArray.put(packComment(comment));
        }
        initiativesJson.put("comments", commentsArray);

        if (gs.getSeller() != null) {
            initiativesJson.put("seller", packUser(gs.getSeller()));
        }

        return initiativesJson;
    }

    /**
     * @author Martin Frick
     * @date 2025-05-23
     * @param g
     * @return JSONObject
     */
    public JSONObject createJsonForInitiativeGargening(Gardening g) {
        JSONObject initiativesJson = new JSONObject();

        initiativesJson.put("initiativeID", g.getCategory());
        initiativesJson.put("title", g.getTitle());
        initiativesJson.put("description", g.getDescription());
        initiativesJson.put("location", g.getLocation());
        initiativesJson.put("duration", g.getDuration());
        initiativesJson.put("startTime", g.getStartTime());
        initiativesJson.put("isPublic", g.isPublic());

        // Add participants
        JSONArray participantsArray = new JSONArray();
        for (String participant : g.getParticipants()) {
            participantsArray.put(participant);
        }
        initiativesJson.put("participants", participantsArray);

        // Add comments
        JSONArray commentsArray = new JSONArray();
        for (Initiative.Comment comment : g.getCommentList()) {
            commentsArray.put(packComment(comment));
        }
        initiativesJson.put("comments", commentsArray);

        if (g.getNeedsHelp() != null) {
            initiativesJson.put("needsHelp", packUser(g.getNeedsHelp()));
        }

        JSONArray helperArray = new JSONArray();
        for (User helper : g.getHelpers()) {
            helperArray.put(packUser(helper));
        }
        initiativesJson.put("helpers", helperArray);

        return initiativesJson;
    }

    /**
     * @author Martin Frick
     * @date 2025-05-23
     * @param ts
     * @return JSONObject
     */
    public JSONObject createJsonForInitiativeToolSharing(ToolSharing ts) {
        JSONObject initiativesJson = new JSONObject();

        initiativesJson.put("initiativeID", ts.getCategory());
        initiativesJson.put("title", ts.getTitle());
        initiativesJson.put("description", ts.getDescription());
        initiativesJson.put("location", ts.getLocation());
        initiativesJson.put("duration", ts.getDuration());
        initiativesJson.put("startTime", ts.getStartTime());
        initiativesJson.put("isPublic", ts.isPublic());

        // Add participants
        JSONArray participantsArray = new JSONArray();
        for (String participant : ts.getParticipants()) {
            participantsArray.put(participant);
        }
        initiativesJson.put("participants", participantsArray);

        // Add comments
        JSONArray commentsArray = new JSONArray();
        for (Initiative.Comment comment : ts.getCommentList()) {
            commentsArray.put(packComment(comment));
        }
        initiativesJson.put("comments", commentsArray);

        if (ts.getLoaner() != null) {
            initiativesJson.put("loaner", packUser(ts.getLoaner()));
        }

        if (ts.getLender() != null) {
            initiativesJson.put("lender", packUser(ts.getLender()));
        }

        return initiativesJson;
    }

    /**
     * @author Martin Frick
     * @date 2025-05-23
     * @param user
     * @return JSONObject
     */
    private JSONObject packUser(User user) {
        JSONObject userJson = new JSONObject();
        userJson.put("email", user.getEmail());
        userJson.put("name", user.getName());
        userJson.put("location", user.getLocation());
        userJson.put("roles", new JSONArray(user.getRoles()));
        return userJson;
    }

    private JSONObject packComment(Initiative.Comment comment) {
        JSONObject commentJson = new JSONObject();
        commentJson.put("id", comment.getId());
        commentJson.put("authorEmail", comment.getAuthorEmail());
        commentJson.put("content", comment.getContent());
        commentJson.put("parentId", comment.getParentId());
        commentJson.put("likes", comment.getLikes());
        
        JSONArray likedByArray = new JSONArray();
        for (String email : comment.getLikedBy()) {
            likedByArray.put(email);
        }
        commentJson.put("likedBy", likedByArray);
        
        JSONArray repliesArray = new JSONArray();
        for (Initiative.Comment reply : comment.getReplies()) {
            repliesArray.put(packComment(reply));
        }
        commentJson.put("replies", repliesArray);
        
        return commentJson;
    }

    public JSONObject createAchievementJson(String name, String progress, String description) {
        JSONObject achievementJson = new JSONObject();
        achievementJson.put("name", name);
        achievementJson.put("progress", progress);
        achievementJson.put("description", description);
        return achievementJson;
    }

    public JSONObject createAchievementRequestJson(String email) {
        JSONObject achievementRequestJson = new JSONObject();
        achievementRequestJson.put("type", "requestAchievements");
        achievementRequestJson.put("email", email);
        return achievementRequestJson;
    }

    public JSONObject createNewLogEntry(String email, String logEntry) {
        JSONObject newLogEntryJson = new JSONObject();
        String time = LocalDateTime.now().toString();
        newLogEntryJson.put("type", "newLogEntry");
        newLogEntryJson.put("logEntry", logEntry);
        newLogEntryJson.put("email", email);
        newLogEntryJson.put("date", time);
        return newLogEntryJson;
    }

    public JSONObject requestLog(String email){
        JSONObject requestLogJson = new JSONObject();
        requestLogJson.put("type", "requestLog");
        requestLogJson.put("email", email);
        return requestLogJson;
    }

    // Add methods for creating join/leave/comment requests
    public JSONObject createJoinInitiativeJson(String userEmail, String initiativeTitle) {
        JSONObject joinJson = new JSONObject();
        joinJson.put("type", "joinInitiative");
        joinJson.put("userEmail", userEmail);
        joinJson.put("initiativeTitle", initiativeTitle);
        return joinJson;
    }

    public JSONObject createLeaveInitiativeJson(String userEmail, String initiativeTitle) {
        JSONObject leaveJson = new JSONObject();
        leaveJson.put("type", "leaveInitiative");
        leaveJson.put("userEmail", userEmail);
        leaveJson.put("initiativeTitle", initiativeTitle);
        return leaveJson;
    }

    public JSONObject createAddCommentJson(String userEmail, String initiativeTitle, String content) {
        JSONObject commentJson = new JSONObject();
        commentJson.put("type", "addComment");
        commentJson.put("userEmail", userEmail);
        commentJson.put("initiativeTitle", initiativeTitle);
        commentJson.put("content", content);
        return commentJson;
    }

    public JSONObject createReplyCommentJson(String userEmail, String initiativeTitle, String content, String parentCommentId) {
        JSONObject replyJson = new JSONObject();
        replyJson.put("type", "replyComment");
        replyJson.put("userEmail", userEmail);
        replyJson.put("initiativeTitle", initiativeTitle);
        replyJson.put("content", content);
        replyJson.put("parentCommentId", parentCommentId);
        return replyJson;
    }

    public JSONObject createLikeCommentJson(String userEmail, String initiativeTitle, String commentId) {
        JSONObject likeJson = new JSONObject();
        likeJson.put("type", "likeComment");
        likeJson.put("userEmail", userEmail);
        likeJson.put("initiativeTitle", initiativeTitle);
        likeJson.put("commentId", commentId);
        return likeJson;
    }

    public JSONObject createGetNeighborhoodInitiativesJson(String userEmail) {
        JSONObject neighborhoodJson = new JSONObject();
        neighborhoodJson.put("type", "getNeighborhoodInitiatives");
        neighborhoodJson.put("userEmail", userEmail);
        return neighborhoodJson;
    }
}
