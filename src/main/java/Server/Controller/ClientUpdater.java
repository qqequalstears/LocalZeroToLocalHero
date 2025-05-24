package Server.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.*;

public class ClientUpdater {
    private ArrayList<ClientConnection> clientConnections;
    private Iterator<ClientConnection> iterator;
    Map<String, ClientConnection> onlineClients;

    public ClientUpdater() {
        this.clientConnections = new ArrayList<>();
        onlineClients = new HashMap<>();
    }

    public void addClient(ClientConnection clientConnection) {
        this.clientConnections.add(clientConnection);
    }

    public void addOnlineClient(String email, ClientConnection clientConnection) {
        onlineClients.put(email, clientConnection);
    }

    public void removeClient(ClientConnection clientConnection) {
        this.clientConnections.remove(clientConnection);
    }

    public List<ClientConnection> getClientConnections() {
        return new ArrayList<>(onlineClients.values());
    }

    public boolean userOnline(String email) {
        return onlineClients.containsKey(email);
    }

    public void removeOnlineClient(String key) {
        onlineClients.remove(key);
    }

    public ClientConnection getClientConnection(String mail) {
        return onlineClients.get(mail);
    }
}
