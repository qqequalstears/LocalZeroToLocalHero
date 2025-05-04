package Server.Controller.Authorization;

import Client.Model.User;
import Server.Controller.FileHandler;

import java.util.List;

public class EmailInUseHandler extends AuthorizationHandler {
    @Override
    protected boolean control(User user) {
        List<User> users = FileHandler.getInstance().getUsers();
        String userMail = user.getEmail();

        System.out.println(userMail);
        for (User currentUser : users) {
            System.out.println(currentUser.getEmail());
            if (currentUser.getEmail().equals(userMail)) {
                return false;
            }
        }
        return true;
    }
}
