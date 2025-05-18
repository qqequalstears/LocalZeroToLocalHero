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
        System.out.println("Thread started");
        try {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("Listening to objects");
                Object object = inputStream.readObject();
                System.out.println("RECIEVED OBJECT");
                connectionController.revealIntention(object, this); //TODo seöver ifall "object" kan bli något interface / castas till typ jsonObject ist. ==>
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void sendObject(Object object) {
        try {
            System.out.println("SENT OBJECT" + object);
            outputStream.writeObject(object);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
