package Client.Controller.Mediators;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MediatorManager {
    private Map<String, Mediator> mediators;
    private static MediatorManager instance;

    private MediatorManager() {
        mediators = new ConcurrentHashMap<>();
        mediators.put("USER",new UserMediator());
        mediators.put("INITIATIVE", new InitiativeMediator());
        mediators.put("MESSAGE", new MessageMediator());
        mediators.put("GUI", new GUIMediator());
    }

    public Mediator getMediator(String key) {
        return mediators.get(key);
    }

    public synchronized static MediatorManager getInstance() {
        if (instance == null) {
            instance = new MediatorManager();
        }
        return instance;
    }
}
