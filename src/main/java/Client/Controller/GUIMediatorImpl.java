package Client.Controller;

import Client.Model.GUIEvents;
import Client.View.Home.HomeController;
import Client.View.UserNotifier;

import java.util.concurrent.ConcurrentHashMap;

public class GUIMediatorImpl implements GUIMediator{
    private static GUIMediator instance;
    private ConcurrentHashMap<String, Object> controllers;

    private GUIMediatorImpl() {
        controllers = new ConcurrentHashMap<>();
    }

    @Override
    public void registerController(String key, Object controller) {
        controllers.put(key,controller);
    }

    @Override
    public void notify(String event, Object... data) {
        GUIEvents eventType = GUIEvents.valueOf(event.toUpperCase());

        switch (eventType) {
            case LOGIN:
                ClientController clientController = (ClientController) controllers.get(ClientController.class.getName());
                clientController.sendLoginToServer((String) data[0], (String) data[1]);
                break;
            case REGISTER:
                clientController = (ClientController) controllers.get(ClientController.class.getName());
                clientController.sendUserToServer((String) data[0],(String) data[1],(String) data[2],(String) data[3]);
                break;
            case NEWSTAGE:
                HomeController homeController = new HomeController();
                homeController.creatHomeStage();
                break;
            case NOTIFYUSER:
                UserNotifier userNotifier = (UserNotifier) controllers.get(UserNotifier.class.getName());
                userNotifier.informUser((String) data[0]);
                break;
        }
    }

    public synchronized static GUIMediator getInstance() {
        if (instance == null) {
            instance = new GUIMediatorImpl();
        }
        return instance;
    }

}
