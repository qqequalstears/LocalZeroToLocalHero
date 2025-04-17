package Server.Controller;

import java.util.ArrayList;
import java.util.Iterator;

public class ClientUpdater{
    private ArrayList<ClientConnection> clientConnections;
    private Iterator<ClientConnection> iterator;

    public ClientUpdater(){
        this.clientConnections = new ArrayList<>();

    }

    public void addClient(ClientConnection clientConnection){
        this.clientConnections.add(clientConnection);
    }
    public void removeClient(ClientConnection clientConnection){
        this.clientConnections.remove(clientConnection);
    }
    public ArrayList<ClientConnection> getClientConnections(){
        return clientConnections;
    }

}
