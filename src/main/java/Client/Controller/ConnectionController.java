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
        try {
            String serverAddress = "127.0.0.1";
            int serverPort = 2343;
            
            // Close existing connection if any
            if (clientConnection != null) {
                clientConnection.disconnect();
            }
            
            clientConnection = new ClientConnection(serverAddress, serverPort, this);
            clientConnection.connect();
            
            // Give the connection a moment to establish
            Thread.sleep(500);
            
            if (!clientConnection.isConnected()) {
                throw new IOException("Failed to establish connection to server");
            }
            
            System.out.println("Successfully connected to server");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Connection interrupted");
        }
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
                System.out.println("[DEBUG] Received unSuccessfulInitiativeCreation");
                guiInController.notifyUser("The initiative could not be created. Please make sure no commas in included anywhere and that every field has text");
                break;
            case "SuccessfulInitiativeCreation":
                System.out.println("[DEBUG] Received SuccessfulInitiativeCreation");
                guiInController.successfulInitiativeCreation();
                break;
            case "joinInitiativeSuccess":
                guiInController.notifyUser("Successfully joined initiative!");
                break;
            case "joinInitiativeFailure":
                String joinFailureMessage = jsonObject.optString("message", "Failed to join initiative");
                guiInController.notifyUser(joinFailureMessage);
                break;
            case "leaveInitiativeSuccess":
                guiInController.notifyUser("Successfully left initiative!");
                break;
            case "leaveInitiativeFailure":
                String leaveFailureMessage = jsonObject.optString("message", "Failed to leave initiative");
                guiInController.notifyUser(leaveFailureMessage);
                break;
            case "addCommentSuccess":
                guiInController.notifyUser("Comment added successfully!");
                break;
            case "addCommentFailure":
                String commentFailureMessage = jsonObject.optString("message", "Failed to add comment");
                guiInController.notifyUser(commentFailureMessage);
                break;
            case "replyCommentSuccess":
                guiInController.notifyUser("Reply added successfully!");
                break;
            case "replyCommentFailure":
                String replyFailureMessage = jsonObject.optString("message", "Failed to add reply");
                guiInController.notifyUser(replyFailureMessage);
                break;
            case "likeCommentSuccess":
                guiInController.notifyUser("Comment liked!");
                break;
            case "likeCommentFailure":
                String likeFailureMessage = jsonObject.optString("message", "Failed to like comment");
                guiInController.notifyUser(likeFailureMessage);
                break;
            case "neighborhoodInitiatives":
                handleNeighborhoodInitiatives(jsonObject);
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
        try {
            // Check if connection is still valid
            if (clientConnection == null || !clientConnection.isConnected()) {
                System.out.println("Connection lost, attempting to reconnect...");
                connectToServer();
                // Give a moment for connection to establish
                Thread.sleep(100);
            }
            
            String dataToSend = jsonObject.toString();
            clientConnection.sendObject(dataToSend);
        } catch (Exception e) {
            System.err.println("Failed to send JSON object: " + e.getMessage());
            guiInController.notifyUser("Connection error. Please try again.");
            
            // Attempt to reconnect
            try {
                connectToServer();
            } catch (IOException reconnectException) {
                System.err.println("Failed to reconnect: " + reconnectException.getMessage());
                guiInController.notifyUser("Unable to reconnect to server. Please restart the application.");
            }
        }
    }

    private void newNotification(JSONObject jsonObject) {
        System.out.println("[DEBUG] Client received notification JSON: " + jsonObject.toString());
        String notification = (String) jsonObject.get("notification");
        System.out.println("[DEBUG] Notification content: " + notification);
        
        Notifications.addNotification(notification);
        System.out.println("[DEBUG] Notification added to model, new count: " + Notifications.getUnreadCount());
        
        guiInController.newNotification();
        System.out.println("[DEBUG] GUI notification update triggered");
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
        System.out.println("[DEBUG] Client updateInitiatives called with: " + jsonObject.toString());
        JSONArray array = jsonObject.getJSONArray("listOfInitiatives");
        System.out.println("[DEBUG] Client received " + array.length() + " initiatives");
        List<String> titles = new ArrayList<>();
        activeInitiatives.clear();

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            String title = obj.getString("title");
            titles.add(title);
            System.out.println("[DEBUG] Client processing initiative: " + title);

            Initiative initiative = unpacker.unpackInitiative(obj);
            if (initiative != null) {
                activeInitiatives.add(initiative);
            }
        }

        System.out.println("[DEBUG] Client calling guiInController.updateInitiatives with " + titles.size() + " titles");
        guiInController.updateInitiatives(titles);
        // Ensure the selected initiative object is up-to-date
        String selectedTitle = guiInController.getCurrentlySelectedInitiative();
        if (selectedTitle != null) {
            Initiative updated = getSpecificInitiative(selectedTitle);
            if (updated != null) {
                guiInController.setCurrentlySelectedInitiative(selectedTitle); // triggers refresh
            }
        }
        guiInController.populateViewSelectedInitiativeSceen();
        System.out.println("[DEBUG] Client finished updating initiatives");
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

    public void sendJoinInitiative(String userEmail, String initiativeTitle) {
        JSONObject joinJson = packager.createJoinInitiativeJson(userEmail, initiativeTitle);
        sendJsonObject(joinJson);
    }

    public void sendLeaveInitiative(String userEmail, String initiativeTitle) {
        JSONObject leaveJson = packager.createLeaveInitiativeJson(userEmail, initiativeTitle);
        sendJsonObject(leaveJson);
    }

    public void sendAddComment(String userEmail, String initiativeTitle, String content) {
        JSONObject commentJson = packager.createAddCommentJson(userEmail, initiativeTitle, content);
        sendJsonObject(commentJson);
    }

    public void sendReplyComment(String userEmail, String initiativeTitle, String content, String parentCommentId) {
        JSONObject replyJson = packager.createReplyCommentJson(userEmail, initiativeTitle, content, parentCommentId);
        sendJsonObject(replyJson);
    }

    public void sendLikeComment(String userEmail, String initiativeTitle, String commentId) {
        JSONObject likeJson = packager.createLikeCommentJson(userEmail, initiativeTitle, commentId);
        sendJsonObject(likeJson);
    }

    public void sendGetNeighborhoodInitiatives(String userEmail) {
        System.out.println("[DEBUG] sendGetNeighborhoodInitiatives called for user: " + userEmail);
        JSONObject neighborhoodJson = packager.createGetNeighborhoodInitiativesJson(userEmail);
        System.out.println("[DEBUG] Sending JSON: " + neighborhoodJson.toString());
        sendJsonObject(neighborhoodJson);
    }

    private void handleNeighborhoodInitiatives(JSONObject jsonObject) {
        System.out.println("[DEBUG] handleNeighborhoodInitiatives received response");
        JSONArray initiativesArray = jsonObject.getJSONArray("initiatives");
        System.out.println("[DEBUG] Received " + initiativesArray.length() + " initiatives");
        List<String> titles = new ArrayList<>();
        activeInitiatives.clear();

        for (int i = 0; i < initiativesArray.length(); i++) {
            JSONObject obj = initiativesArray.getJSONObject(i);
            String title = obj.getString("title");
            titles.add(title);

            Initiative initiative = unpacker.unpackInitiative(obj);
            if (initiative != null) {
                activeInitiatives.add(initiative);
            }
        }

        guiInController.updateInitiatives(titles);
        guiInController.notifyUser("Showing " + titles.size() + " neighborhood initiatives");
    }

}