package Server.Controller;

import Client.Model.Role;
import Client.Model.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    public JSONObject collectUserInfo(JSONObject jsonObject) {
        String userMail = (String) jsonObject.get("mail");
        User userToCollect = null;
        String userLocation = null;
        String userName = null;

        List<User> users = FileHandler.getInstance().getUsers();

        for (User user : users) {
            if (user.getEmail().equals(userMail)) {
                userToCollect = user;
                userLocation = user.getLocation();
                userName = user.getName();
                break;
            }
        }

        JSONObject userRolesJSON = new JSONObject();
        userRolesJSON.put("type", "showUserRoles");
        userRolesJSON.put("userMail", userMail);
        JSONArray rolesJSON = new JSONArray();
        for (Role role : userToCollect.getRoles()) {
            JSONObject roleJSON = new JSONObject();
            roleJSON.put("role", role);
            rolesJSON.put(roleJSON);
        }
        userRolesJSON.put("roles", rolesJSON);
        userRolesJSON.put("name", userName);
        userRolesJSON.put("location", userLocation);

        return userRolesJSON;
    }

    public boolean isAdmin(String mail) {
        List<User> users = FileHandler.getInstance().getUsers();
        for (User user : users) {
            if (user.getEmail().equals(mail) && user.getRoles().contains(Role.Admin)) {
                return true;
            }
        }
        return false;
    }

    public void updateRoles(JSONObject jsonObject) {
        String mail = (String) jsonObject.get("mail");
        List<String> newRoles = new ArrayList<>();

        JSONArray rolesJSONArray = jsonObject.getJSONArray("roles");
        for (int i = 0; i < rolesJSONArray.length(); i++) {
            JSONObject roleJson = rolesJSONArray.getJSONObject(i);
            String role = roleJson.getString("role");
            if (!role.isEmpty()) {
                newRoles.add(role);
            }
        }

        FileHandler.getInstance().replaceRoles(mail, newRoles);
    }
}
