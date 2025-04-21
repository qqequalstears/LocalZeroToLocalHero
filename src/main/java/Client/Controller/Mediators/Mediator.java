package Client.Controller.Mediators;

public interface Mediator {
    public void registerController(String key, Object controller);

    public void notify(String event, Object... data);
}
