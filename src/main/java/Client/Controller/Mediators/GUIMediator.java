package Client.Controller.Mediators;

import Client.Controller.GUIControllers.Intitiative.InitiativeController;
import Client.Controller.GUIControllers.LoginController.LoginController;
import Client.Model.GUIEvents;
import Client.View.CreateInitiative.CreateInitiativeStage;
import Client.View.Home.HomeStage;
import Client.View.Login.LogInStage;
import Client.View.StageCreator;
import Client.View.UserNotifier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GUIMediator implements Mediator{
    private Map<String, StageCreator> stageCreators;
    private Map<String, Object> controllers;


    public GUIMediator() {
        controllers = new ConcurrentHashMap<>();
        stageCreators = new ConcurrentHashMap<>();

        stageCreators.put("HOMESTAGE", () -> new HomeStage().createStage());
        stageCreators.put("LOGINSTAGE", () -> new LogInStage().createStage());
        stageCreators.put("INITIATIVESTAGE", () -> new InitiativeController());
        stageCreators.put("CREATEINITIATIVE", () -> new CreateInitiativeStage().createStage());
    }

    @Override
    public void registerController(String key, Object controller) {

    }

    @Override
    public void notify(String event, Object... data) {
        GUIEvents eventType = GUIEvents.valueOf(event.toUpperCase());

        switch (eventType) {
            case NEWSTAGE:
                StageCreator creator = stageCreators.get((String) data[0]);
                creator.createStage();
                break;
            case NOTIFYUSER:
                UserNotifier userNotifier = (UserNotifier) controllers.get(UserNotifier.class.getName());
                userNotifier.informUser((String) data[0]);
                break;
            case SUCCESSLOGIN:
                LoginController loginController = (LoginController) controllers.get(LoginController.class.getName());
                loginController.closeStage();
        }
    }
}