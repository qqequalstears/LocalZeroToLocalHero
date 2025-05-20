package Client.Controller;

import Client.Controller.GUIControllers.GUIInController;
import Client.Controller.GUIControllers.GUIOutController;
import Client.Model.Achievement;
import Client.Model.Notifications;
import Client.Model.User;
import Client.Model.Message;
import Common.Controller.Utility.Packager;
import Common.Controller.Utility.Unpacker;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class ConnectionController {
    private ClientConnection clientConnection;
    private GUIInController guiInController;
    private Packager packager;
    private Unpacker unpacker;
    private User connectedUser;

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
            default:
                guiInController.notifyUser("Something went wrong in the application");
        }
    }

    public void sendRegisterToServer(String mail, String password, String name, String city) {
        JSONObject registerJson = packager.createRegisterJSON(mail, password, name, city);
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
    public List<Achievement> getAchievements() {
        return connectedUser.getAchievements();
    }

    public User getConnectedUser() {
        return connectedUser;
    }
}