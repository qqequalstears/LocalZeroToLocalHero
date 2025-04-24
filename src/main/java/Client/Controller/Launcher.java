package Client.Controller;

import Client.Controller.GUIControllers.LoginController;
import Client.View.Login.LogInStage;
import Client.View.UserNotifier;
import javafx.application.Application;

import java.io.IOException;

public class Launcher {
    public static void main(String[] args) {

        ConnectionController connectionController = new ConnectionController();
        try {
            connectionController.connectToServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Tryck Enter för att stänga klienten...");
        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            connectionController.disconnectFromServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        /*ClientController clientController = new ClientController();
        UserNotifier userNotifier = new UserNotifier();

        GUIMediatorImpl.getInstance().registerController(clientController.getClass().getName(),clientController);
        GUIMediatorImpl.getInstance().registerController(userNotifier.getClass().getName(), userNotifier);
        Application.launch(LogInStage.class, args);*/
    }
}
