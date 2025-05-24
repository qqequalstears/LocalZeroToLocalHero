package Client.Controller;

import Client.Controller.GUIControllers.GUIInController;
import Client.Controller.GUIControllers.GUIOutController;
import Client.Model.Achievement;
import Client.Model.Initiative.Children.CarPool;
import Client.Model.Initiative.Children.GarageSale;
import Client.Model.Initiative.Children.Gardening;
import Client.Model.Initiative.Children.ToolSharing;
import Client.Model.Initiative.Parent.Initiative;
import Client.Model.Notifications;
import Client.Model.User;
import Client.Model.Message;
import Common.Controller.Utility.Packager;
import Common.Controller.Utility.Unpacker;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONArray;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @update  Martin Frick
 * Added a lot of functions for initiatives
 *
 * @author Anton Persson
 */
public class ConnectionController {
    private ClientConnection clientConnection;
    private GUIInController guiInController;
    private Packager packager;
    private Unpacker unpacker;
    private User connectedUser;
    private List<Initiative> activeInitiatives = new ArrayList<>();


    public ConnectionController() {
        try {
            GUIOutController.getInstance().setConnectionController(this);
            guiInController = GUIInController.getInstance();
            packager = new Packager();
            unpacker = new Unpacker();
            connectToServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectToServer() throws IOException {
        String serverAddress = "127.0.0.1";
        int serverPort = 2343;
        clientConnection = new ClientConnection(serverAddress, serverPort, this);
        clientConnection.connect();
    }

    public void disconnectFromServer() throws IOException {
        if (clientConnection != null) {
            clientConnection.disconnect();
        }
    }

    public void revealIntention(Object object) {
        System.out.println("[DEBUG] revealIntention called with: " + object);
        String jsonString = (String) object;
        JSONObject jsonObject = new JSONObject(jsonString);
        String intention = (String) jsonObject.get("type");
        System.out.println("Gained an object" + jsonObject);

        switch (intention) {
            case "successfulLogin":
                GUIOutController.getInstance().getConnectionController().sendRequestForInitiatives();
                guiInController.successfulLogIn();
                break;
            case "unSuccessfulLogin":
                guiInController.notifyUser("You are already online or you gave wrong credentials");
                break;
            case "unSuccessfulRegister":
                guiInController.notifyUser("Mail is already in use, or you gave wrong credentials. Remember that , is not allowed");
                break;
            case "notification":
                newNotification(jsonObject);
                break;
            case "newMessage":
                String senderId = jsonObject.getString("senderId");
                String recipientId = jsonObject.getString("recipientId");
                String subject = jsonObject.getString("subject");
                String content = jsonObject.getString("content");
                String timestamp = jsonObject.getString("timestamp");

                Message message = new Message(senderId, recipientId, subject, content);
                try {
                    java.lang.reflect.Field tsField = Message.class.getDeclaredField("timestamp");
                    tsField.setAccessible(true);
                    tsField.set(message, LocalDateTime.parse(timestamp));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                MessageController.getInstance().addMessage(message);
                break;
            case "unSuccessfulInitiativeCreation":
                guiInController.notifyUser("The initiative could not be created. Please make sure no commas in included anywhere and that every field has text");
                break;
            case "SuccessfulInitiativeCreation":
                guiInController.successfulInitiativeCreation();
                break;
            case "achievementsLocation":
                JSONArray jsonArray = (JSONArray) jsonObject.get("achievements");
                convertingAchievements(jsonArray);
                break;
            case "logResponse":
                JSONArray logArray = (JSONArray) jsonObject.get("log");
                convertingLogs(logArray);
                break;
            case "updateClients":
                updateClients(jsonObject);
                break;
            case "showUserRoles":
                showUserRoles(jsonObject);
                break;
            case "updateInitiatives":
                updateInitiatives(jsonObject);
                break;
            default:
                guiInController.notifyUser("Something went wrong in the application");
        }
    }

    public void sendRegisterToServer(String mail, String password, String name, String city) {
        JSONObject registerJson = packager.createRegisterJSON(mail, password, name, city);
        connectedUser = new User(mail, password);
        sendJsonObject(registerJson);
    }

    public void sendLoginToServer(String mail, String password) {
        JSONObject loginJson = packager.createLoginJSON(mail, password);
        connectedUser = new User(mail, password); //todo Möjligtvis att detta behövs ske genom att servern skickar hela user objektet
        sendJsonObject(loginJson);
    }

    public void sendIntention(String intention) {
        JSONObject jsonObject = packager.createIntentionJson(intention);
        sendJsonObject(jsonObject);
    }

    public void sendLogout() {
        guiInController.logout();
        JSONObject logoutJson = packager.createLogoutJson(connectedUser.getEmail());
        sendJsonObject(logoutJson);
    }

    public void sendNewInitiativeToServer(String name, String description, String location, String duration, String startTime, String numberOfSeats, String sellList, String category, boolean ispublic) {
        JSONObject newInitiative = packager.createNewInitiative(name, description, location, duration, startTime, numberOfSeats, sellList, category, ispublic, connectedUser.getEmail());
        sendJsonObject(newInitiative);
    }

    public void sendJsonObject(JSONObject jsonObject) {
        String dataToSend = jsonObject.toString();
        clientConnection.sendObject(dataToSend);
    }

    private void newNotification(JSONObject jsonObject) {
        System.out.println("REACHED NOTIFICATION METHOD");
        String notification = (String) jsonObject.get("notification");
        Notifications.addNotification(notification);
        guiInController.newNotification();
    }

    public void requestAchievements() {
        JSONObject requestAchievements = packager.createAchievementRequestJson(connectedUser.getEmail());
        sendJsonObject(requestAchievements);
    }

    private void convertingAchievements(JSONArray jsonArray) {
        List<Achievement> achievements = unpacker.unpackJsonArray(jsonArray, obj -> {
            String name = obj.getString("category");
            String progress = obj.getString("progress");
            String description = obj.getString("description");
            return new Achievement(name, Integer.parseInt(progress), description);
        });
        GUIInController.getInstance().achievement(achievements);
    }

    private void convertingLogs(JSONArray jsonArray) {
        List<String> logs = unpacker.unpackJsonArray(jsonArray, obj -> {
            String log = obj.getString("logEntry");
            String time = obj.getString("date");
            String together = log + "," + time;
            return together;
        });
        GUIInController.getInstance().responseLogs(logs);
    }

    public void sendNewLogEntry(String logEntry) {
        JSONObject newLogEntry = packager.createNewLogEntry(connectedUser.getEmail(), logEntry);
        sendJsonObject(newLogEntry);
    }

    public void requestLog() {
        JSONObject requestLog = packager.requestLog(connectedUser.getEmail());
        sendJsonObject(requestLog);
    }

    public User getConnectedUser() {
        return connectedUser;
    }

    private void updateClients(JSONObject jsonObject) {
        List<String> userMails = new ArrayList<>();

        JSONArray userArray = jsonObject.getJSONArray("listOfUsers");
        for (int i = 0; i < userArray.length(); i++) {
            JSONObject userJson = userArray.getJSONObject(i);
            userMails.add(userJson.getString("email"));
        }

        guiInController.updateClients(userMails);
    }

    /**
     * Builds and sends JSON request for all initiatives.
     * @autor Martin Frick
     */
    public void sendRequestForInitiatives(){
        System.out.println("Client sending getInitiatives");
        JSONObject getInitJson = packager.createIntentionJson("getInitiatives");
        sendJsonObject(getInitJson);
    }

    public void getUserInfo(String mailOfUser) {
        JSONObject mailToUser = packager.createCollectUserInfoJSON(mailOfUser, connectedUser.getEmail());
        sendJsonObject(mailToUser);
    }

    private void showUserRoles(JSONObject roles) {
        String userMail = (String) roles.get("userMail");
        String userName = (String) roles.get("name");
        String userLocation = (String) roles.get("location");
        boolean isAdmin = (boolean) roles.get("isAdmin");
        List<String> userRoles = new ArrayList<>();

        JSONArray rolesJSONArray = (JSONArray) roles.get("roles");

        for (int i = 0; i < rolesJSONArray.length(); i++) {
            JSONObject roleJSON = rolesJSONArray.getJSONObject(i);
            String role = roleJSON.getString("role");
            userRoles.add(role);
        }
        guiInController.showUserInfo(userMail, userName, userLocation, userRoles, isAdmin);

    }

    public void updateUsersRoles(String[] roles, String mail) {
        JSONObject updateRoles = packager.createUpdateRolesJSON(roles, mail);
        sendJsonObject(updateRoles);
    }

    private void updateInitiatives(JSONObject jsonObject) {
        JSONArray array = jsonObject.getJSONArray("listOfInitiatives");
        List<String> titles = new ArrayList<>();
        activeInitiatives.clear();

        for (int i = 0; i < array.length(); i++) {
            JSONObject Jobj = array.getJSONObject(i);
            String title = Jobj.getString("title");
            titles.add(title);

            Initiative initiative = unpacker.unpackInitiative(Jobj);
            if (initiative != null) {
                activeInitiatives.add(initiative);
            } else {
                System.err.println("ERROR!!! Failed to unpack initiative: " + Jobj.toString());
            }

        }

        guiInController.updateInitiatives(titles);
    }

    public void updateInitiativesFromCache() {
        List<String> titles = new ArrayList<>();
        for (Initiative i : activeInitiatives) {
            titles.add(i.getTitle());
        }
        guiInController.updateInitiatives(titles);
    }

    /**
     * @author Martin Frick
     * @param titleOfInitiativeToReturn
     * @return Initiative with title
     */
    public Initiative getSpecificInitiative(String titleOfInitiativeToReturn) {
        for (Initiative i : activeInitiatives) {
            if (i.getTitle().equals(titleOfInitiativeToReturn)) {
                return i;
            }
        }
        return null;
    }

    public String getCurrentInitiativeType(String title) {
        Initiative selected = getSpecificInitiative(title);
        if (selected instanceof CarPool) return "CarPool";
        if (selected instanceof GarageSale) return "Garage Sale";
        if (selected instanceof Gardening) return "Gardening";
        if (selected instanceof ToolSharing) return "ToolSharing";
        return "BadType";
    }




}