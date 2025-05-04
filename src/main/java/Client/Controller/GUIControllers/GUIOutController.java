package Client.Controller.GUIControllers;

import Client.Controller.ConnectionController;

public class GUIOutController {
    private static GUIOutController instance;
    private ConnectionController connectionController;

    private GUIOutController() {

    }

    public void login(String mail, String password) {
        connectionController.sendLoginToServer(mail, password);
    }

    public void register(String mail, String password, String name, String city) {
        connectionController.sendRegisterToServer(mail, password, name, city);
    }

    public void setConnectionController(ConnectionController connectionController) {
        this.connectionController = connectionController;
    }

    public static GUIOutController getInstance() {
        if (instance == null) {
            instance = new GUIOutController();
        }
        return instance;
    }

    public void logout() {
        connectionController.sendLogout();
    }
}
