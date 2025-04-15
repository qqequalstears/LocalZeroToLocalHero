package Client.Controller;

import Client.Model.LoginCredentials;
import Client.Model.User;

public class ClientController {
    public void sendUserToServer(String mail, String password, String name, String city) {
        User user = new User(mail, password, new LoginCredentials(name, city));
        System.out.println("de funka");
        //todo Skicka till servern
    }

    public void sendLoginToServer(String mail, String password) {
        LoginCredentials loginCredentials = new LoginCredentials(mail,password);
        //todo Skicka till servern
    }
}
