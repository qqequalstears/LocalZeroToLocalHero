package Common.Controller.Utility;

import Client.Model.Initiative.Children.CarPool;
import Client.Model.Initiative.Children.GarageSale;
import Client.Model.Initiative.Children.Gardening;
import Client.Model.Initiative.Children.ToolSharing;
import Client.Model.Initiative.Parent.Initiative;
import Client.Model.Role;
import Client.Model.User;
import org.json.JSONArray;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Unpacker {
    public JSONObject unpackByteToJson(byte[] bytes) {
        String jsonString = new String(bytes, StandardCharsets.UTF_8);
        return new JSONObject(jsonString);
    }

    public <T> List<T> unpackJsonArray(JSONArray jsonArray, Function<JSONObject, T> mapper) {
        List<T> result = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            T item = mapper.apply(jsonObject);
            result.add(item);
        }
        return result;
    }

    /*private void convertingAchievements(JSONArray jsonArray) {
        Unpacker unpacker = null;
        List<Achievement> achievements = unpacker.unpackJsonArray(jsonArray, obj ->{
            String name = obj.getString("category");
            String progress = obj.getString("progress");
            String description = obj.getString("description");
            return new Achievement(name, Integer.parseInt(progress), description);
        });
        GUIInController.getInstance().achievement(achievements);
    }
*/

    public Initiative unpackInitiative(JSONObject obj) {
        String category = obj.has("initiativeID") ? obj.getString("initiativeID") : obj.optString("category", "SAKNAS");
        if (category.equals("SAKNAS")) {
            System.err.println("Error!!!! Initiative JSON missing 'initiativeID': " + obj.toString());
            return null;
        }
        String title = obj.getString("title");
        String description = obj.getString("description");
        String location = obj.getString("location");
        String duration = obj.getString("duration");
        String startTime = obj.getString("startTime");
        boolean isPublic = obj.getBoolean("isPublic");

        if (category.equals("CarPool")) {
            String numberOfSeats = obj.getString("numberOfSeats");
            String destination = obj.getString("destination");

            CarPool cp = new CarPool(title, description, location, duration, startTime, numberOfSeats, category, isPublic);
            cp.setDestination(destination);

            if (obj.has("driver")) {
                cp.setDriver(unpackaUser(obj.getJSONObject("driver")));
            }

            if (obj.has("passengers")) {
                JSONArray passengerArray = obj.getJSONArray("passengers");
                List<User> passengers = new ArrayList<>();
                for (int i = 0; i < passengerArray.length(); i++) {
                    passengers.add(unpackaUser(passengerArray.getJSONObject(i)));
                }
                cp.setPassengers(passengers);
            }

            return cp;
        }

        if (category.equals("Garage Sale")) {
            String itemsToSell = obj.getString("itemsToSell");

            GarageSale gs = new GarageSale(title, description, location, duration, startTime, itemsToSell, category, isPublic);

            if (obj.has("seller")) {
                gs.setSeller(unpackaUser(obj.getJSONObject("seller")));
            }

            return gs;
        }

        if (category.equals("Gardening")) {
            Gardening g = new Gardening(category, title, description, location, duration, startTime, new ArrayList<>(), new ArrayList<>(), isPublic, new ArrayList<>());

            if (obj.has("needsHelp")) {
                g.setNeedsHelp(unpackaUser(obj.getJSONObject("needsHelp")));
            }

            if (obj.has("helpers")) {
                JSONArray helperArray = obj.getJSONArray("helpers");
                List<User> helpers = new ArrayList<>();
                for (int i = 0; i < helperArray.length(); i++) {
                    helpers.add(unpackaUser(helperArray.getJSONObject(i)));
                }
                g.setHelpers(helpers);

            }

            return g;
        }

        if (category.equals("ToolSharing")) {
            ToolSharing ts = new ToolSharing(category, title, description, location, duration, startTime, new ArrayList<>(), new ArrayList<>(), isPublic, new ArrayList<>());

            if (obj.has("loaner")) {
                ts.setLoaner(unpackaUser(obj.getJSONObject("loaner")));
            }

            if (obj.has("lender")) {
                ts.setLender(unpackaUser(obj.getJSONObject("lender")));
            }

            return ts;
        }

        return null;
    }

    private User unpackaUser(JSONObject userObj) {
        String email = userObj.getString("email");
        String name = userObj.getString("name");
        String location = userObj.getString("location");
        List<Role> roles = new ArrayList<>();
        JSONArray rolesArray = userObj.getJSONArray("roles");
        for (int i = 0; i < rolesArray.length(); i++) {
            roles.add(Role.valueOf(rolesArray.getString(i)));
        }
        return new User(name, location, email, "placeholder", roles);
    }
}
