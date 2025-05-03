package Client.Controller.GUIControllers;

import Client.Controller.ConnectionController;
import Client.Controller.GUIControllers.Intitiative.InitiativeController;
import Client.Controller.GUIControllers.LoginController.LoginController;
import Client.View.CreateInitiative.CreateInitiativeStage;
import Client.View.Home.HomeStage;
import Client.View.Login.LogInStage;
import Client.View.StageCreator;
import Client.View.UserNotifier;
import javafx.application.Platform;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GUIInController {
    private Map<String, StageCreator> stageCreators;
    private static GUIInController instance;

    private GUIInController() {
        stageCreators = new ConcurrentHashMap<>();

        stageCreators.put("HOMESTAGE", () -> new HomeStage().createStage());
        stageCreators.put("LOGINSTAGE", () -> new LogInStage().createStage());
        stageCreators.put("INITIATIVESTAGE", () -> new InitiativeController());
        stageCreators.put("CREATEINITIATIVE", () -> new CreateInitiativeStage().createStage());
    }

    public void createStage(String stageToCreate) {
        StageCreator creator = stageCreators.get(stageToCreate);
        creator.createStage();
    }

    public void notifyUser(String message) {
        UserNotifier userNotifier = new UserNotifier();
        userNotifier.informUser(message);
    }

    public void successfulLogIn() {
        FxController loginController = GUIControllerRegistry.getInstance().get(LoginController.class.getName());
        Platform.runLater(() -> {
            loginController.closeStage();
            createStage("HOMESTAGE");
        });
    }

    public static GUIInController getInstance() {
        if (instance == null) {
            instance = new GUIInController();
        }
        return instance;
    }
}