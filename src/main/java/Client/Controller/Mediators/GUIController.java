package Client.Controller.Mediators;

import Client.Controller.ConnectionController;
import Client.Controller.GUIControllers.FxController;
import Client.Controller.GUIControllers.GUIControllerRegistry;
import Client.Controller.GUIControllers.Intitiative.InitiativeController;
import Client.View.CreateInitiative.CreateInitiativeStage;
import Client.View.Home.HomeStage;
import Client.View.Login.LogInStage;
import Client.View.StageCreator;
import Client.View.UserNotifier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GUIController {
    private Map<String, StageCreator> stageCreators;
    private static GUIController instance;
    private ConnectionController connectionController;


    private GUIController() {
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

    public void login(String mail, String password) { // todo Flytta till en annan klass?

    }

    public void register(String mail, String password, String name, String city) { //todo Flytta till en annan klass?

    }

    public void successfulLogIn() {
        FxController loginController = GUIControllerRegistry.getInstance().get(UserNotifier.class.getName());
        loginController.closeStage();
    }

    public static GUIController getInstance() {
        if (instance == null) {
            instance = new GUIController();
        }
        return instance;
    }
}