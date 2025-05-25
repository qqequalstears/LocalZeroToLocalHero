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

    public void connectSocket() {
        try {
            socket = new Socket(serverAddress, serverPort);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
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
        } catch (IOException e) {
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
                try {
                    Object object = inputStream.readObject();
                    if (object != null) {
                        connectionController.revealIntention(object);
                    }
                } catch (java.io.EOFException e) {
                    System.out.println("Server closed connection unexpectedly. Attempting to reconnect...");
                    disconnect();
                    Thread.sleep(1000); // Wait before reconnecting
                    connectSocket();
                    if (!isConnected()) {
                        System.err.println("Failed to reconnect to server");
                        break;
                    }
                } catch (java.io.IOException e) {
                    System.err.println("IO error in client connection: " + e.getMessage());
                    disconnect();
                    break;
                } catch (ClassNotFoundException e) {
                    System.err.println("Class not found error: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Fatal error in client connection: " + e.getMessage());
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

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }
}