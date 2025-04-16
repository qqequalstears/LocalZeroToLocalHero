package Client.Controller;

import Client.Controller.GUIControllers.LoginController;
import Client.Model.GUIEvents;
import Client.View.Home.HomeStage;
import Client.View.Login.LogInStage;
import Client.View.StageCreator;
import Client.View.UserNotifier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GUIMediatorImpl implements GUIMediator{
    private static GUIMediator instance;
    private Map<String, Object> controllers;
    private Map<String, StageCreator> stageCreators;

    private GUIMediatorImpl() {
        controllers = new ConcurrentHashMap<>();
        stageCreators = new ConcurrentHashMap<>();

        stageCreators.put("HOMESTAGE", () -> new HomeStage().createStage());
        stageCreators.put("LOGINSTAGE", () -> new LogInStage().createStage());
    }

    @Override
    public void registerController(String key, Object controller) {
        controllers.put(key,controller);
    }

    @Override
    public void notify(String event, Object... data) {
        GUIEvents eventType = GUIEvents.valueOf(event.toUpperCase());

        ClientController clientController = (ClientController) controllers.get(ClientController.class.getName());

        switch (eventType) {
            case LOGIN:
                clientController.sendLoginToServer((String) data[0], (String) data[1]);
                break;
            case REGISTER:
                clientController.sendUserToServer((String) data[0],(String) data[1],(String) data[2],(String) data[3]);
                break;
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

    public synchronized static GUIMediator getInstance() {
        if (instance == null) {
            instance = new GUIMediatorImpl();
        }
        return instance;
    }
}