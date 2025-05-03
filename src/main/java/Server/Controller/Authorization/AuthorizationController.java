package Server.Controller.Authorization;

import Client.Model.User;
import Server.Controller.ClientConnection;
import Server.Controller.ClientUpdater;
import Server.Controller.ConnectionController;
import org.json.JSONObject;

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
}
