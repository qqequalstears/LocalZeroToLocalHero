package Server.Controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientConnection implements Runnable {
    private Socket socket;
    private ConnectionController connectionController;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public ClientConnection(Socket socket, ConnectionController connectionController) {
        this.socket = socket;
        this.connectionController = connectionController;
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        System.out.println("Thread started for client: " + socket.getRemoteSocketAddress());
        try {
            while (!Thread.currentThread().isInterrupted() && socket.isConnected() && !socket.isClosed()) {
                try {
                    System.out.println("Listening to objects");
                    Object object = inputStream.readObject();
                    if (object != null) {
                        System.out.println("RECEIVED OBJECT: " + object);
                        connectionController.revealIntention(object, this);
                    }
                } catch (java.io.EOFException e) {
                    System.out.println("Client disconnected: " + socket.getRemoteSocketAddress());
                    break;
                } catch (java.io.IOException e) {
                    System.err.println("IO error with client " + socket.getRemoteSocketAddress() + ": " + e.getMessage());
                    break;
                } catch (ClassNotFoundException e) {
                    System.err.println("Class not found error: " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("Unexpected error handling client request: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.err.println("Fatal error in server client connection: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("Closing connection for client: " + socket.getRemoteSocketAddress());
            closeConnection();
        }
    }

    public void sendObject(Object object) {
        try {
            if (outputStream != null && socket.isConnected() && !socket.isClosed()) {
                System.out.println("SENT OBJECT: " + object);
                outputStream.writeObject(object);
                outputStream.flush();
            } else {
                System.err.println("Cannot send object - connection is closed");
            }
        } catch (IOException e) {
            System.err.println("Error sending object to client: " + e.getMessage());
            closeConnection();
        }
    }

    private void closeConnection() {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (Exception e) {
            System.err.println("Error closing connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    public String getClientAddress() {
        return socket != null ? socket.getRemoteSocketAddress().toString() : "Unknown";
    }
}
