package Client.Controller;

public interface GUIMediator {
    public void registerController(String key, Object controller);

    public void notify(String event, Object... data);
}
