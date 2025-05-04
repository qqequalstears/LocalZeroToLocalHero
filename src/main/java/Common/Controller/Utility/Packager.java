package Common.Controller.Utility;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

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
}
