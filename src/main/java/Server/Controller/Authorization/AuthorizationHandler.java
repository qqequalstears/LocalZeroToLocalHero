package Server.Controller.Authorization;

import Client.Model.User;

public abstract class AuthorizationHandler {
    protected AuthorizationHandler next;

    public void setNext(AuthorizationHandler next) {
        this.next = next;
    }

    public boolean handleAuthorization(User user) {
        if (!control(user)) {
            return false;
        }
        if (next == null) {
            return true;
        } else {
            return next.handleAuthorization(user);
        }
    }

    protected abstract boolean control(User user);
}
