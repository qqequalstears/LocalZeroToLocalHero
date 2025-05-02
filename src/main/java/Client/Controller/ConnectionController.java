package Client.Controller;
import Client.Controller.GUIControllers.GUIInController;
import Client.Controller.GUIControllers.GUIOutController;
import Client.Model.LoginCredentials;
import Common.Controller.Utility.Packager;
import Common.Controller.Utility.Unpacker;
import org.json.JSONObject;

import java.io.IOException;

public class ConnectionController {
    private ClientConnection clientConnection;
    private GUIInController guiInController;
    private Packager packager;
    private Unpacker unpacker;

    public ConnectionController(){
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
        String jsonString = (String) object;
        JSONObject jsonObject = new JSONObject(jsonString);
        String objectType = (String) jsonObject.get("type");
        if (objectType.equals("successfulLogin")) {
            System.out.println("FEEEESSSTTTT");
            guiInController.successfulLogIn();
        }
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
        JSONObject loginJson = packager.createLoginJSON(mail, password);
        sendJsonObject(loginJson);
    }

    public void successfulLogin(boolean success) {
        if (success) {
            guiInController.successfulLogIn();
        } else {
            guiInController.notifyUser("Wrong mail or password entered");
        }
    }

    private void sendJsonObject(JSONObject jsonObject) {
        String dataToSend = jsonObject.toString();
        clientConnection.sendObject(dataToSend);
    }
}