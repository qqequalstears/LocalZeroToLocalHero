package Server.Controller;

import Client.Model.Initiative.Children.CarPool;
import Client.Model.Initiative.Children.GarageSale;
import Client.Model.Initiative.Children.Gardening;
import Client.Model.Initiative.Children.ToolSharing;
import Client.Model.Initiative.Parent.Initiative;
import Client.Model.User;
import Common.Controller.Utility.Packager;
import Server.Controller.Authorization.AuthorizationController;
import Server.Service.MessageService;
import Server.Service.FileStorageService;
import Server.Service.NotificationService;
import Server.Model.AchievementTracker;
import Server.Model.FileMan.ReaderFiles;
import Server.Model.FileMan.WriteToFile;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONArray;

import java.net.Socket;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.List;

public class ConnectionController {

    private ConnectionListener connectionListener;
    private ClientUpdater clientUpdater;
    private Packager packager;
    private AuthorizationController authorizationController;
    private InitiativeManager initiativeManager;
    private MessageService messageService;
    private FileStorageService fileStorageService;
    private NotificationService notificationService;
    private UserManager userManager;
    private LogManager logManager;

    public ConnectionController() {
        authorizationController = new AuthorizationController(this);
        initiativeManager = new InitiativeManager();
        packager = new Packager();
        userManager = new UserManager();
        logManager = new LogManager();
        this.clientUpdater = new ClientUpdater();
        this.connectionListener = new ConnectionListener(2343, this);
        this.fileStorageService = new FileStorageService();
        this.notificationService = new NotificationService(clientUpdater, fileStorageService);
        this.messageService = new MessageService(clientUpdater, fileStorageService, notificationService);
    }

    public synchronized void addConnection(Socket socket) {
        try {
            ClientConnection clientConnection = new ClientConnection(socket, this);
            clientUpdater.addClient(clientConnection);
            new Thread(clientConnection).start();
        } catch (Exception e) {
            System.out.println("Error creating client connection: " + socket.getInetAddress());
            e.printStackTrace();
        }
        System.out.println("Client connected: " + socket.getInetAddress());
    }

    public synchronized void revealIntention(Object object, ClientConnection sender) {
        System.out.println("Inne i revealIntention() ");
        System.out.println("Received raw object: " + object);

        try {
            String jsonString = (String) object;
            JSONObject jsonObject = new JSONObject(jsonString);
            System.out.println("[DEBUG] Parsed JSONObject: " + jsonObject);

            String intention = jsonObject.getString("type");
            System.out.println("[DEBUG] Intention is " + intention);

            switch (intention) {
                case "login":
                    handleLogin(jsonObject, sender);
                    break;

                case "logout":
                    handleLogout(jsonObject);
                    break;

                case "register":
                    handleRegister(jsonObject, sender);
                    break;

                case "createInitiative":
                    handleCreateInitiative(jsonObject, sender);
                    break;

                case "sendMessage":
                    messageService.handleNewMessage(jsonObject);
                    break;

                case "collectUserInfo":
                    collectUserRoles(jsonObject, sender);
                    break;

                case "updateRoles":
                    userManager.updateRoles(jsonObject, this);
                    break;

                case "requestAchievements":
                    String email = jsonObject.getString("email");
                    sendAchievementForLocation(email, sender);
                    break;

                case "newLogEntry":
                    logManager.newLogEntry(jsonObject);
                    requestLog(jsonObject, sender);
                    break;

                case "requestLog":
                    requestLog(jsonObject, sender);
                    break;

                case "getInitiatives":
                    System.out.println("[DEBUG] Received request: getInitiatives");
                    sendAllActiveInitiatives(sender);
                    break;

                default:
                    System.err.println("[ERROR] Unrecognized intention: " + intention);
                    break;
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to process incoming object: " + object);
            e.printStackTrace();
        }

       /* String jsonString = (String) object;
        JSONObject jsonObject = new JSONObject(jsonString);
        System.out.println(jsonObject);
        String intention = (String) jsonObject.get("type");
        System.out.println("Intention is " + intention);

        switch (intention) {
            case "login":
                handleLogin(jsonObject, sender);
                break;
            case "logout":
                handleLogout(jsonObject);
                break;
            case "register":
                handleRegister(jsonObject, sender);
                break;
            case "createInitiative":
                handleCreateInitiative(jsonObject, sender);
                break;
            case "sendMessage":
                messageService.handleNewMessage(jsonObject);
                break;
            case "collectUserInfo":
                collectUserRoles(jsonObject, sender);
                break;
            case "updateRoles":
                userManager.updateRoles(jsonObject, this);
                break;
            case "requestAchievements":
                String email = jsonObject.getString("email");
                sendAchievementForLocation(email, sender);
                break;
            case "newLogEntry":
                logManager.newLogEntry(jsonObject);
                requestLog(jsonObject, sender);
            case "requestLog":
                requestLog(jsonObject, sender);
                break;
            case "getInitiatives":
                System.out.println("getInitiatives has been REACHED by req from client!!!!");
                sendAllActiveInitiatives(sender);
                break;
            default:
                System.out.println("Intention was not found");
                break;
        }*/
    }

    private void handleLogin(JSONObject jsonObject, ClientConnection sender) {
        String mail = (String) jsonObject.get("mail");
        boolean successfulLogin = authorizationController.tryLogin(jsonObject, clientUpdater);
        sendLoginStatus(sender, mail, successfulLogin);
        
        if (successfulLogin) {
            deliverStoredNotifications(mail, sender);
        }
    }

    private void deliverStoredNotifications(String mail, ClientConnection sender) {
        String filename = "notifications_" + mail + ".txt";
        String notifications = fileStorageService.readFile(filename);
        if (!notifications.isEmpty()) {
            for (String notification : notifications.split("\n")) {
                if (!notification.trim().isEmpty()) {
                    JSONObject notificationPackage = new JSONObject();
                    notificationPackage.put("type", "notification");
                    notificationPackage.put("notification", notification);
                    sender.sendObject(notificationPackage.toString());
                }
            }
            fileStorageService.deleteFile(filename);
        }
    }

    private void handleLogout(JSONObject jsonObject) {
        String mail = (String) jsonObject.get("mail");
        clientUpdater.removeOnlineClient(mail);
    }

    private void handleRegister(JSONObject jsonObject, ClientConnection sender) {
        boolean successfulRegister = authorizationController.tryRegister(jsonObject);
        String mail = (String) jsonObject.get("mail");
        sendRegisterStatus(sender, mail, successfulRegister);
    }

    private void handleCreateInitiative(JSONObject jsonObject, ClientConnection sender) {
        boolean success = initiativeManager.createNewInitiative(jsonObject);
        if(success){notifyOfAchievement(jsonObject);}
        sendCreateInitiativeStatus(success, sender);
    }

    public void sendNotification(String notification, String mailToReceiver) {
        System.out.println("[DEBUG] sendNotification called for: " + mailToReceiver + " with: " + notification);
        ClientConnection receiver = clientUpdater.getClientConnection(mailToReceiver);
        System.out.println("Sending notification to " + receiver);
        if (receiver != null) {
            JSONObject notificationPackage = new JSONObject();
            notificationPackage.put("type", "notification");
            notificationPackage.put("notification", notification);
            receiver.sendObject(notificationPackage.toString());
        } else {
            // Store notification in a file for offline users
            try {
                File file = new File("notifications_" + mailToReceiver + ".txt");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                    writer.write(notification);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendCreateInitiativeStatus(boolean success, ClientConnection creator) {
        JSONObject sucess = new JSONObject();
        String status = "unSuccessfulInitiativeCreation";
        if (success) {
            status = "SuccessfulInitiativeCreation";
        }
        sucess.put("type",status);
        creator.sendObject(sucess.toString());
    }

    private void sendRegisterStatus(ClientConnection sender, String mail, boolean successfulRegister) {
        JSONObject sucess = new JSONObject();
        String status = "unSuccessfulRegister";
        if (successfulRegister) {
            status = "successfulLogin";
            clientUpdater.addOnlineClient(mail, sender);
        }
        sucess.put("type", status);
        sender.sendObject(sucess.toString());

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sendEveryUser();
    }

    private void sendLoginStatus(ClientConnection sender, String mail, boolean successfulLogin) {
        JSONObject sucess = new JSONObject();
        String status = "unSuccessfulLogin";
        if (successfulLogin) {
            status = "successfulLogin";
            clientUpdater.addOnlineClient(mail, sender);
        }
        sucess.put("type", status);
        sender.sendObject(sucess.toString());

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sendEveryUser();
    }

    private void sendAchievementForLocation(String email, ClientConnection sender) {
        String location = ReaderFiles.getInstance().fetchOneUserData(email);
        JSONArray achievementList = AchievementTracker.getInstance().getAchievementsForLocation(location);
        JSONObject achievementPackage = new JSONObject();
        achievementPackage.put("type", "achievementsLocation");
        achievementPackage.put("achievements", achievementList);

        sender.sendObject(achievementPackage.toString());
    }

    private void requestLog(JSONObject jsonObject, ClientConnection sender){
        JSONObject logResponse = logManager.requestLog(jsonObject);
        sender.sendObject(logResponse.toString());
    }

    private void sendEveryUser() {
        List<User> users = FileHandler.getInstance().getUsers();

        JSONArray userArray = new JSONArray();
        for (User user : users) {
            JSONObject userJson = new JSONObject();
            userJson.put("email", user.getEmail());
            userArray.put(userJson);
        }

        JSONObject listOfUsers = new JSONObject();
        listOfUsers.put("type", "updateClients");
        listOfUsers.put("listOfUsers", userArray);

        List<ClientConnection> onlineClients = clientUpdater.getClientConnections();
        if (onlineClients != null) {
            for (ClientConnection client : onlineClients) {
                client.sendObject(listOfUsers.toString());
            }
        }
    }

    private void collectUserRoles(JSONObject jsonObject, ClientConnection connection) {
        String connectionMail = (String) jsonObject.get("connectionMail");
        JSONObject userRolesJSON = userManager.collectUserInfo(jsonObject);
        boolean connectionIsAdmin = userManager.isAdmin(connectionMail);
        userRolesJSON.put("isAdmin", connectionIsAdmin);

        connection.sendObject(userRolesJSON.toString());
    }
    private void notifyOfAchievement(JSONObject jsonObject){
        String location = jsonObject.getString("location");
        String category = jsonObject.getString("category");
        List<User> users = FileHandler.getInstance().getUsers();
        for (User user : users) {
            if (user.getLocation().equalsIgnoreCase(location)) {
                String mail = user.getEmail();
                String notification = "New progress on: " + category;
                sendNotification(notification, mail);
            }
        }
    }

    private void sendAllActiveInitiatives(ClientConnection sender) {
        System.out.println("sendAllActiveInitiatives() method in server side has been reached");
        List<Initiative> allActiveInitiatives = FileHandler.getInstance().getAllActiveInitiatives();
        System.out.println("Amount of initiatives fetched from filehandler is: " + allActiveInitiatives.size());
        JSONArray allActiveInitiativesArray = new JSONArray();

        for (int i = 0; i < allActiveInitiatives.size(); i++) {
            Initiative initiative = allActiveInitiatives.get(i);
            JSONObject initiativeJson = new JSONObject();

            if (initiative.getCategory().equals("CarPool")) {
                initiativeJson = packager.createJsonForInitiativeCarPool((CarPool) initiative);
            } else if (initiative.getCategory().equals("GarageSale")) {
                initiativeJson = packager.createJsonForInitiativeGarageSale((GarageSale) initiative);
            } else if (initiative.getCategory().equals("Gardening")) {
                initiativeJson = packager.createJsonForInitiativeGargening((Gardening) initiative);
            } else if (initiative.getCategory().equals("ToolSharing")) {
                initiativeJson = packager.createJsonForInitiativeToolSharing((ToolSharing) initiative);
            }

            allActiveInitiativesArray.put(initiativeJson);
        }

        JSONObject response = new JSONObject();
        response.put("type", "updateInitiatives");
        response.put("listOfInitiatives", allActiveInitiativesArray);

        System.out.println("DEBUUUUUGG Sending initiatives to client: " + response);

        sender.sendObject(response.toString());
    }


}
