package Server.Controller;

import Common.Controller.Utility.Packager;
import Server.Controller.Authorization.AuthorizationController;
import org.json.JSONObject;

import java.net.Socket;

public class ConnectionController {

    private ConnectionListener connectionListener;
    private ClientUpdater clientUpdater;
    private Packager packager;
    private AuthorizationController authorizationController;
    private InitiativeManager initiativeManager;

    public ConnectionController () {
        authorizationController = new AuthorizationController(this);
        initiativeManager = new InitiativeManager();
        packager = new Packager();
        this.clientUpdater = new ClientUpdater();
        this.connectionListener = new ConnectionListener(2343,this);

    }
    public synchronized void addConnection(Socket socket) {
        try{
            ClientConnection clientConnection = new ClientConnection(socket, this);
            clientUpdater.addClient(clientConnection);
            new Thread(clientConnection).start();
        }catch(Exception e){
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
                String mail = (String) jsonObject.get("mail");
                boolean successfulLogin = authorizationController.tryLogin(jsonObject, clientUpdater);
                sendLoginStatus(sender, mail , successfulLogin);
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
            default:
                System.out.println("Intention was not found");
                break;
        }
    }

    public void sendNotification(String notification,String mailToReceiver) {
        ClientConnection receiver = clientUpdater.getClientConnection(mailToReceiver);
        System.out.println("Sending notification to " + receiver);
        if (receiver != null) {
            JSONObject notificationPackage = new JSONObject();
            notificationPackage.put("type", "notification");
            notificationPackage.put("notification", notification);
            receiver.sendObject(notificationPackage.toString());
        } else {
            //todo Hantera offline klienter
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
        sucess.put("type",status);
        sender.sendObject(sucess.toString());
    }

    private void sendLoginStatus(ClientConnection sender, String mail, boolean successfulLogin) {
        JSONObject sucess = new JSONObject();
        String status = "unSuccessfulLogin";
        if (successfulLogin) {
            status = "successfulLogin";
            clientUpdater.addOnlineClient(mail, sender);
        }
        sucess.put("type",status);
        sender.sendObject(sucess.toString());
    }
}