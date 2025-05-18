package Server;

import Server.Controller.ConnectionController;

public class Start {
    public static void main(String[] args) {
        // Start the server
        ConnectionController connectionController = new ConnectionController();
        System.out.println("Server started");
    }
}
