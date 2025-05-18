package Server.Controller.Authorization;

import Client.Model.User;
import Server.Controller.FileHandler;

import java.util.List;

public class PasswordMatchHandler extends AuthorizationHandler {
    @Override
    protected boolean control(User user) {
        List<User> users = FileHandler.getInstance().getUsers();
        String userMail = user.getEmail();
        String userPassword = user.getPassword();

        for (User currentUser : users) {
            if (currentUser.getEmail().equals(userMail)) {
                return currentUser.getPassword().equals(userPassword);
            }
        }
        return false;
    }
}
