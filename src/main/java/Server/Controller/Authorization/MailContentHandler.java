package Server.Controller.Authorization;

import Client.Model.User;

public class MailContentHandler extends AuthorizationHandler{
    @Override
    protected boolean control(User user) {
        String mail = user.getEmail();
        return !mail.contains(",") && mail.contains("@");
    }
}