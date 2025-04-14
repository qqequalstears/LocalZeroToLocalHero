package Server.Controller;

import Server.Model.User;

public abstract class LoginHandler {
    protected LoginHandler next;
    public void setNext(LoginHandler next) {
        this.next = next;
    }
    public abstract void handleLogin(User user);
}
