package Client.Controller;

import Client.View.Login.LogInStage;
import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {
        new ConnectionController();
        Application.launch(LogInStage.class, args);
    }
}
