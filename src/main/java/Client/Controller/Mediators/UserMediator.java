package Client.Controller.Mediators;

import Client.Controller.ClientController;
import Client.Model.GUIEvents;

import java.util.HashMap;
import java.util.Map;

public class UserMediator implements Mediator {
    private Map<String, Object> controllers;

    public UserMediator() {
        controllers = new HashMap<>();
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
        }
    }
}