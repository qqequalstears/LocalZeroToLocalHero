package Client.Controller.GUIControllers;

import Client.Controller.ConnectionController;
import Client.Model.Achievement;

import java.util.List;

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
    public List<Achievement> getConnectedUser() {

        return connectionController.getAchievements();
    }

    public void logout() {
        connectionController.sendLogout();
    }

    public void createInitiative(String name, String description, String location, String duration, String startTime, String numberOfSeats, String sellList, String category, boolean ispublic) {
        connectionController.sendNewInitiativeToServer(name, description, location, duration, startTime, numberOfSeats, sellList, category, ispublic);
    }

    public String getConnectedUserEmail() {
        if (connectionController != null && connectionController.getConnectedUser() != null) {
            return connectionController.getConnectedUser().getEmail();
        }
        return null;
    }
}
