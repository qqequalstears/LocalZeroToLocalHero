package Client.Controller;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectionController {
    private ClientConnection clientConnection;

    public ConnectionController(){

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
}
