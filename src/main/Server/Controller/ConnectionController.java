package Server.Controller;

import java.io.IOException;
import java.net.Socket;

public class ConnectionController {

    private ConnectionListener connectionListener;
    private ClientUpdater clientUpdater;

    public ConnectionController () {
        this.clientUpdater = new ClientUpdater();
        this.connectionListener = new ConnectionListener(8080,this);

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

    }
}
