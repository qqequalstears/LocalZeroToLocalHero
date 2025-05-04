package Client.Controller;

import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnection extends Thread {
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

    public void connect() {
        start();
    }

    public void connectSocket(){
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
        System.out.println("Disconecting");
        try {
            if (socket != null) {
                socket.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }catch(IOException e){
            System.out.println("Error closing connection: " + serverAddress + ":" + serverPort);
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            if (socket == null) {
                connectSocket();
            }
            while (!Thread.currentThread().isInterrupted()) {
                    Object object = inputStream.readObject();
                    connectionController.revealIntention(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
            disconnect();
        }
    }

    public void sendObject(Object object) {
        try {
            System.out.println("SENT OBJECT \n" + object);
            outputStream.writeObject(object);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}