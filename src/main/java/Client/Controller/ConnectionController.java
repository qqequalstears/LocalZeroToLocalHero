package Client.Controller;
import Client.Controller.Mediators.GUIController;
import Client.Model.LoginCredentials;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectionController {
    private ClientConnection clientConnection;
    private ConnectionController instance;
    private GUIController guiController;

    private ConnectionController(){
        try {
            connectToServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void connectToServer() throws IOException{
        String serverAddress = "127.0.0.1";
        int serverPort = 2343;
        clientConnection = new ClientConnection(serverAddress, serverPort, this);
        clientConnection.connect();
    }
    public void disconnectFromServer() throws IOException{
        if(clientConnection != null){
            clientConnection.disconnect();
        }
    }

    public void unpackObject(Object object) {

    }

    public void sendUserToServer(String mail, String password, String name, String city) {
        JSONObject registerJson = new JSONObject();
        registerJson.put("mail"     ,    mail);
        registerJson.put("password" ,    password);
        registerJson.put("name"     ,    name);
        registerJson.put("city"     ,    city);

        //todo Skicka till servern
    }

    public void sendLoginToServer(String mail, String password) {
        LoginCredentials loginCredentials = new LoginCredentials(mail,password);
        //todo Skicka till servern
    }

    public void successfulLogin(boolean success) {
        if (success) {
            guiController.successfulLogIn();
        } else {
            guiController.notifyUser("Wrong mail or password entered");
        }
    }

    public ConnectionController getInstance() {
        if (instance == null) {
            instance = new ConnectionController();
        }
        return instance;
    }
}