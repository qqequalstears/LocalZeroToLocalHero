package Server.Controller;

import Common.Controller.Utility.Packager;
import Common.Controller.Utility.Unpacker;
import Server.Controller.Authorization.AuthorizationController;
import org.json.JSONObject;

import java.net.Socket;

public class ConnectionController {

    private ConnectionListener connectionListener;
    private ClientUpdater clientUpdater;
    private Unpacker unpacker;
    private Packager packager;
    private AuthorizationController authorizationController;

    public ConnectionController () {
        authorizationController = new AuthorizationController(this);
        unpacker = new Unpacker();
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

    public void revealIntention(Object object, ClientConnection sender) {
        String jsonString = (String) object;
        JSONObject jsonObject = new JSONObject(jsonString);
        System.out.println(jsonObject);
        String intention = (String) jsonObject.get("type");

        switch (intention) {
            case "login":
                sendLoginStatus(sender, authorizationController.tryLogin(jsonObject, clientUpdater));
               break;
            default:
                System.out.println("Intention was not found");
                break;
        }
    }

    private void sendLoginStatus(ClientConnection sender, boolean successfulLogin) {
        JSONObject sucess = new JSONObject();
        String status = "unSuccessfulLogin";
        if (successfulLogin) {
            status = "successfulLogin";
        }
        sucess.put("type",status);
        sender.sendObject(sucess.toString());
    }

}