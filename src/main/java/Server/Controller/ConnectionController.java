package Server.Controller;

import Common.Controller.Utility.Packager;
import Common.Controller.Utility.Unpacker;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;

public class ConnectionController {

    private ConnectionListener connectionListener;
    private ClientUpdater clientUpdater;
    private Unpacker unpacker;
    private Packager packager;

    public ConnectionController () {
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

    public void unpackObject(Object object, ClientConnection sender) {
        String jsonString = (String) object;
        System.out.println("OBJECT IS " + jsonString);
        JSONObject jsonObject = new JSONObject(jsonString);
        System.out.println(jsonObject);
        String objectType = (String) jsonObject.get("type");
        if (objectType.equals("login")) {
            System.out.println("IT WORKED");
            JSONObject sucess = new JSONObject();
            sucess.put("type","successfulLogin");
            sender.sendObject(sucess.toString());
        }
    }
}