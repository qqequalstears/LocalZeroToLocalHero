package Server.Controller;

import Client.Model.Role;
import Client.Model.User;
import Common.Controller.Utility.Packager;
import Server.Controller.Authorization.AuthorizationController;
import Server.Service.MessageService;
import Server.Service.FileStorageService;
import Server.Service.NotificationService;
import org.json.JSONObject;
import org.json.JSONArray;

import java.net.Socket;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public ConnectionController() {
        authorizationController = new AuthorizationController(this);
        initiativeManager = new InitiativeManager();
        packager = new Packager();
        userManager = new UserManager();
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
        String jsonString = (String) object;
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
            default:
                System.out.println("Intention was not found");
                break;
        }
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
}
