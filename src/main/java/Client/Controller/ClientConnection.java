package Client.Controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnection {
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private String serverAddress;
    private int serverPort;
    private ConnectionController connectionController;

    public ClientConnection(String serverAddress, int serverPort, ConnectionController connectionController) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.connectionController = connectionController;
    }

    public void connect(){
        try {
            socket = new Socket(serverAddress, serverPort);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        }
        catch (Exception e) {
            System.out.println("Error connecting to server: " + serverAddress + ":" + serverPort);
            e.printStackTrace();
        }
        System.out.println("Connected to server: " + serverAddress + ":" + serverPort);
    }
    public void disconnect() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }catch(IOException e){
            System.out.println("Error closing connection: " + serverAddress + ":" + serverPort);
            e.printStackTrace();
        }
    }
}
