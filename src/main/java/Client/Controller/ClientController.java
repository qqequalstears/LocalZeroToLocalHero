package Client.Controller;

import Client.Controller.Mediators.Mediator;
import Client.Controller.Mediators.MediatorManager;
import Client.Model.LoginCredentials;
import Client.Model.User;

/**
 * ClientController
 *
 * Revised variables used in sendUserToServer methods creation of User. (LoginCredantials had name and city in it, mail and password was outside) /MartinFrick
 *
 * @author AntonPersson
 * @version 250421_0 Updated by MartinFrick
 */
public class ClientController {
    private Mediator mediator;

    public ClientController() {
        mediator = MediatorManager.getInstance().getMediator("GUI");
        mediator.registerController(this.getClass().getName(), this);
    }

    public void sendUserToServer(String mail, String password, String name, String city) {
        User user = new User(name, city, new LoginCredentials(mail, password));
        //todo Skicka till servern
    }

    // name,  location,  email,  password, List<Role> roles

    public void sendLoginToServer(String mail, String password) {
        LoginCredentials loginCredentials = new LoginCredentials(mail,password);
        //todo Skicka till servern
    }

    public void successfulLogin(boolean success) {
        if (success) {
            mediator.notify("SUCCESSLOGIN");
        } else {
            mediator.notify("NOTIFYUSER", "Wrong mail or password entered");
        }
    }
}
