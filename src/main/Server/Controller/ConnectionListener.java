package Server.Controller;

import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionListener extends Thread{

    private ServerSocket serverSocket;
    private ConnectionController connectionController;

    public ConnectionListener(int port, ConnectionController connectionController) {
        this.connectionController = connectionController;
        try {
            serverSocket = new ServerSocket(port);
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public synchronized void run(){
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress());
                connectionController.addConnection(socket);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
