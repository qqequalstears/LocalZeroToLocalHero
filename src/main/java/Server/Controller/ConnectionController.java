package Server.Controller;

import Common.Controller.Utility.Packager;
import Server.Controller.Authorization.AuthorizationController;
import org.json.JSONObject;

import java.net.Socket;
import java.io.*;
import java.time.LocalDateTime;

public class ConnectionController {

    private ConnectionListener connectionListener;
    private ClientUpdater clientUpdater;
    private Packager packager;
    private AuthorizationController authorizationController;
    private InitiativeManager initiativeManager;

    public ConnectionController() {
        authorizationController = new AuthorizationController(this);
        initiativeManager = new InitiativeManager();
        packager = new Packager();
        this.clientUpdater = new ClientUpdater();
        this.connectionListener = new ConnectionListener(2343, this);

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

    //TODO add interface for this method?--> Object object ==> metoden hade kunnat hantera de första delen av logiken i denna metoden? (Object>String>JSONOBJECT>String)@jansson
    public synchronized void revealIntention(Object object, ClientConnection sender) {
        String jsonString = (String) object;
        JSONObject jsonObject = new JSONObject(jsonString);
        System.out.println(jsonObject);
        String intention = (String) jsonObject.get("type");
        System.out.println("Intention is " + intention);

        switch (intention) {
            case "login":
                String mail = (String) jsonObject.get("mail");
                boolean successfulLogin = authorizationController.tryLogin(jsonObject, clientUpdater);
                sendLoginStatus(sender, mail, successfulLogin);
                // After successful login, deliver any stored notifications
                if (successfulLogin) {
                    File file = new File("notifications_" + mail + ".txt");
                    if (file.exists()) {
                        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                JSONObject notificationPackage = new JSONObject();
                                notificationPackage.put("type", "notification");
                                notificationPackage.put("notification", line);
                                sender.sendObject(notificationPackage.toString());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        file.delete(); // Clear notifications after sending
                    }
                }
                break;
            case "logout":
                mail = (String) jsonObject.get("mail");
                clientUpdater.removeOnlineClient(mail);
                break;
            case "register":
                boolean successfulRegister = authorizationController.tryRegister(jsonObject);
                mail = (String) jsonObject.get("mail");
                sendRegisterStatus(sender, mail, successfulRegister);
                break;
            case "createInitiative" :
                boolean success = initiativeManager.createNewInitiative(jsonObject);
                sendCreateInitiativeStatus(success, sender);
                break;
            case "sendMessage":
                String senderId = jsonObject.getString("senderId");
                String recipientId = jsonObject.getString("recipientId");
                String subject = jsonObject.getString("subject");
                String content = jsonObject.getString("content");
                
                // Create message package to forward to recipient
                JSONObject messagePackage = new JSONObject();
                messagePackage.put("type", "newMessage");
                messagePackage.put("senderId", senderId);
                messagePackage.put("recipientId", recipientId);
                messagePackage.put("subject", subject);
                messagePackage.put("content", content);
                messagePackage.put("timestamp", LocalDateTime.now().toString());
                
                // Forward message to recipient if online
                ClientConnection receiver = clientUpdater.getClientConnection(recipientId);
                if (receiver != null) {
                    receiver.sendObject(messagePackage.toString());
                }
                
                // Store message in file for persistence
                try {
                    File file = new File("messages.txt");
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                        writer.write(String.join("|",
                            senderId,
                            recipientId,
                            subject,
                            content,
                            LocalDateTime.now().toString(),
                            "false"
                        ));
                        writer.newLine();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
                // Send notification
                sendNotification("You have received a new message from " + senderId, recipientId);
                break;
            default:
                System.out.println("Intention was not found");
                break;
        }
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
    }
}