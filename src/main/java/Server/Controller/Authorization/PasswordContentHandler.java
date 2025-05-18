package Server.Controller.Authorization;

import Client.Model.User;

public class PasswordContentHandler extends AuthorizationHandler {
    @Override
    protected boolean control(User user) {
        String password = user.getPassword();
        return !password.contains(",");
    }
}
