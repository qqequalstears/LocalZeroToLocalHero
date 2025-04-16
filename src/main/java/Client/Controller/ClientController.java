package Client.Controller;

import Client.Model.LoginCredentials;
import Client.Model.User;

public class ClientController {
    private GUIMediator guiMediator;

    public ClientController() {
        guiMediator = GUIMediatorImpl.getInstance();
        guiMediator.registerController(this.getClass().getName(), this);
    }

    public void sendUserToServer(String mail, String password, String name, String city) {
        User user = new User(mail, password, new LoginCredentials(name, city));
        //todo Skicka till servern
    }

    public void sendLoginToServer(String mail, String password) {
        LoginCredentials loginCredentials = new LoginCredentials(mail,password);
        //todo Skicka till servern
    }

    public void successfulLogin(boolean success) {
        if (success) {
            guiMediator.notify("SUCCESSLOGIN");
        } else {
            guiMediator.notify("NOTIFYUSER", "Wrong mail or password entered");
        }
    }
}
