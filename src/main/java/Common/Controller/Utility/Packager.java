package Common.Controller.Utility;

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
}
