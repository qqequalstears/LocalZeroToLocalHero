package Client.Controller.GUIControllers;

import Client.Controller.Registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GUIControllerRegistry implements Registry {
    private static GUIControllerRegistry instance;
    private Map<String, FxController> controllerMap;

    private GUIControllerRegistry() {
        controllerMap = new ConcurrentHashMap<>();
    }

    public void add(String key, FxController controller) {
        controllerMap.put(key,controller);
    }

    public FxController get(String key) {
        return controllerMap.get(key);
    }

    public boolean contains(String key) {
        return controllerMap.containsKey(key);
    }

    public void remove(String key) {
        controllerMap.remove(key);
    }

    public static GUIControllerRegistry getInstance() {
        if (instance == null) {
            instance = new GUIControllerRegistry();
        }
        return instance;
    }
}
