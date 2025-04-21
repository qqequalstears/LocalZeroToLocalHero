package Client.Controller;

import Client.Controller.Mediators.GUIMediator;
import Client.View.Login.LogInStage;
import Client.View.UserNotifier;
import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {
        new ClientController();
        new UserNotifier();

        Application.launch(LogInStage.class, args);
    }
}
