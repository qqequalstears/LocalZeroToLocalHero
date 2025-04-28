package Client.Controller;

import Client.View.Login.LogInStage;
import Client.View.UserNotifier;
import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {
        Application.launch(LogInStage.class, args);
    }
}
