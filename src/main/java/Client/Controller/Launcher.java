package Client.Controller;

import Client.Controller.GUIControllers.LoginController;
import Client.View.Login.LogInStage;
import Client.View.UserNotifier;
import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {
        ClientController clientController = new ClientController();
        UserNotifier userNotifier = new UserNotifier();

        GUIMediatorImpl.getInstance().registerController(clientController.getClass().getName(),clientController);
        GUIMediatorImpl.getInstance().registerController(userNotifier.getClass().getName(), userNotifier);
        Application.launch(LogInStage.class, args);
    }
}
