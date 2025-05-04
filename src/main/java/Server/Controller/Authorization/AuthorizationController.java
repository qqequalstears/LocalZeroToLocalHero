package Server.Controller.Authorization;

import Client.Model.Role;
import Client.Model.User;
import Server.Controller.ClientConnection;
import Server.Controller.ClientUpdater;
import Server.Controller.ConnectionController;
import Server.Controller.FileHandler;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AuthorizationController {
    private ConnectionController connectionController;

    public AuthorizationController(ConnectionController connectionController) {
        this.connectionController = connectionController;
    }

    public boolean tryLogin(JSONObject jsonObject, ClientUpdater clientUpdater) {
        String mail = (String) jsonObject.get("mail");
        String password = (String) jsonObject.get("password");
        User user = new User(mail, password);
        System.out.println("Mail is " + mail + " ----------------- Password is " + password);

        AuthorizationHandler loginChain = new AlreadyOnlineHandler(clientUpdater);
        AuthorizationHandler userExistsHandler = new UserExistsHandler();
        AuthorizationHandler passwordMatchHandler = new PasswordMatchHandler();

        loginChain.setNext(userExistsHandler);
        userExistsHandler.setNext(passwordMatchHandler);

        return loginChain.handleAuthorization(user);
    }

    public boolean tryRegister(JSONObject jsonObject) {
        String mail = (String) jsonObject.get("mail");
        String password = (String) jsonObject.get("password");
        User user = new User(mail, password);

        AuthorizationHandler registerChain = new MailContentHandler();
        AuthorizationHandler passwordContentHandler = new PasswordContentHandler();
        AuthorizationHandler emailInUserHandler = new EmailInUseHandler();

        registerChain.setNext(passwordContentHandler);
        passwordContentHandler.setNext(emailInUserHandler);

        boolean canRegister = registerChain.handleAuthorization(user);

        if (canRegister) {
            registerUser(jsonObject);
        }

        return canRegister;
    }

    private void registerUser(JSONObject jsonObject) {
        String mail = (String) jsonObject.get("mail");
        String password = (String) jsonObject.get("password");
        String name = (String) jsonObject.get("name");
        String city = (String) jsonObject.get("city");
        String csvRegister = String.join(",", mail, password, name, city, "Resident");

        FileHandler.getInstance().registerUser(csvRegister);
    }
}
