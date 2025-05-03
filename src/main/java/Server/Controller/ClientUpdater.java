package Server.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ClientUpdater{
    private ArrayList<ClientConnection> clientConnections;
    private Iterator<ClientConnection> iterator;
    Map<String, ClientConnection> onlineClients;

    public ClientUpdater(){
        this.clientConnections = new ArrayList<>();
        onlineClients = new HashMap<>();
    }

    public void addClient(ClientConnection clientConnection){
        this.clientConnections.add(clientConnection);
    }

    public void addOnlineClient(String email, ClientConnection clientConnection) {
        onlineClients.put(email, clientConnection);
    }

    public void removeClient(ClientConnection clientConnection){
        this.clientConnections.remove(clientConnection);
    }
    public ArrayList<ClientConnection> getClientConnections(){
        return clientConnections;
    }

    public boolean userOnline(String email) {
        return onlineClients.containsKey(email);
    }

}
