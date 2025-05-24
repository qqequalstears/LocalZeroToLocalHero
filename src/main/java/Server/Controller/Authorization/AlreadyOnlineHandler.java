package Server.Controller.Authorization;

import Client.Model.User;
import Server.Controller.ClientUpdater;

public class AlreadyOnlineHandler extends AuthorizationHandler {
    private ClientUpdater clientUpdater;

    public AlreadyOnlineHandler(ClientUpdater clientUpdater) {
        this.clientUpdater = clientUpdater;
    }

    @Override
    protected boolean control(User user) {
        String email = user.getEmail();

        if (clientUpdater.userOnline(email)) {
            return false;
        }
        return true;
    }
}
