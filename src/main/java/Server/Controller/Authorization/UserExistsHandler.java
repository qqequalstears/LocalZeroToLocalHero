package Server.Controller.Authorization;

import Client.Model.User;
import Server.Controller.FileHandler;

import java.util.List;

public class UserExistsHandler extends AuthorizationHandler {
    @Override
    protected boolean control(User user) {
        List<User> users = FileHandler.getInstance().getUsers();
        String userMail = user.getEmail();

        for (User currentUser : users) {
            if (currentUser.getEmail().equals(userMail)) {
                return true;
            }
        }
        return false;
    }
}
