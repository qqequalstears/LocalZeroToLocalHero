package Client.Model.Module;

import java.util.ArrayList;
import java.util.List;

/**
 *  Object representing a user in runtime.
 *
 *
 * @author MartinFrick
 * @version 250416_0
 */
public class User implements Log {

    private String name;
    private String location;
    private String email;
    private String password;
    private List<Role> roles;


    public User(String name, String location, String email, String password, List<Role> roles) {
        this.name = name;
        this.location = location;
        this.email = email;
        this.password = password;
        this.roles = new ArrayList<>(roles);
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


    @Override
    public void Log() {
        System.out.println("I R GOOD CLASS IMPLAMANTING LOUGGING TIHI! I AM USER OBJECKT HIHIHI");
    }
}
