package Client.Model;

public class User {
    private String name;
    private String city;
    private LoginCredentials loginCredentials;
    private Roles[] roles;

    public User(String name, String city, Roles[] roles) {
        this.name = name;
        this.city = city;
        this.roles = roles;
    }

    public User(String name, String city, LoginCredentials loginCredentials) {
        this.name = name;
        this.city = city;
        this. loginCredentials = loginCredentials;
    }

    public User(String name, String city, LoginCredentials loginCredentials, Roles[] roles) {
        this.name = name;
        this.city = city;
        this. loginCredentials = loginCredentials;
        this.roles = roles;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LoginCredentials getLoginCredentials() {
        return loginCredentials;
    }

    public void setLoginCredentials(LoginCredentials loginCredentials) {
        this.loginCredentials = loginCredentials;
    }

    public Roles[] getRoles() {
        return roles;
    }

    public void setRoles(Roles[] roles) {
        this.roles = roles;
    }
}
