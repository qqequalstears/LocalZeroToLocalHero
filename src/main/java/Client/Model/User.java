package Client.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Object representing a user in runtime.
 *
 * @author MartinFrick
 * @version 250416_0
 */
public class User implements Log, ISavableObject {

    private String name;
    private String location;
    private String email;
    private String password;
    private List<Role> roles;
    private LoginCredentials loginCredentials;
    private List<Achievement> achievements;

    public User(String name, String location, LoginCredentials loginCredentials) {
        this.name = name;
        this.location = location;
        this.roles = new ArrayList<>();
        this.loginCredentials = loginCredentials;

        setEmail(loginCredentials.getMail());
        setPassword(loginCredentials.getPassword());
    }


    public User(String name, String location, List<Role> roles, LoginCredentials loginCredentials) {
        this.name = name;
        this.location = location;
        this.roles = new ArrayList<>(roles);
        this.loginCredentials = loginCredentials;

        setEmail(loginCredentials.getMail());
        setPassword(loginCredentials.getPassword());
    }

    public User(String name, String location, String email, String password) {
        this.name = name;
        this.location = location;
        this.email = email;
        this.password = password;
        this.roles = new ArrayList<>();
        this.loginCredentials = new LoginCredentials(email, password);


        //TODO ska det finnas ett default på role? @jansson
        roles.add(Role.Resident);

    }

    public User(String name, String location, String email, String password, List<Role> roles) {
        this.name = name;
        this.location = location;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public User(String mail, String password) {
        this.email = mail;
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LoginCredentials getLoginCredentials() {
        return loginCredentials;
    }

    public void setLoginCredentials(LoginCredentials loginCredentials) {
        this.loginCredentials = loginCredentials;
    }

    public void addAchievement(Achievement achievement) {
        if (achievements == null) {
            achievements = new ArrayList<>();
        }
        achievements.add(achievement);
    }
    public List<Achievement> getAchievements() {
        return achievements;
    }


    @Override
    public void Log() {
        System.out.println("I R GOOD CLASS IMPLAMANTING LOUGGING TIHI! I AM USER OBJECKT HIHIHI");
    }


    @Override
    public String getSaveString() {
        return String.join(",", email, password, name, location, getRolesAsString() );
    }

    private String getRolesAsString() {
        StringBuilder returnString = new StringBuilder(String.valueOf(roles.get(0)));
        for (int i = 1; i < roles.size(); i++) {
            returnString.append("-").append(roles.get(i));
        }
        return returnString.toString();
    }

    public void setAchievements(List<Achievement> achievements) {
        this.achievements = achievements;
    }
}
