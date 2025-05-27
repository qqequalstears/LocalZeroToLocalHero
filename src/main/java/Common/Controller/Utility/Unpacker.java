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
        String category = obj.getString("initiativeID");
        String title = obj.getString("title");
        String description = obj.getString("description");
        String location = obj.getString("location");
        String duration = obj.getString("duration");
        String startTime = obj.getString("startTime");
        boolean isPublic = obj.getBoolean("isPublic");

        Initiative initiative = null;

        if (category.equals("CarPool")) {
            String numberOfSeats = obj.getString("numberOfSeats");
            
            CarPool cp = new CarPool(title, description, location, duration, startTime, numberOfSeats, category, isPublic);
            
            // Only set destination if it exists in the JSON
            if (obj.has("destination")) {
                String destination = obj.getString("destination");
                cp.setDestination(destination);
            }

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

            initiative = cp;
        }

        else if (category.equals("Garage Sale")) {
            String itemsToSell = obj.getString("itemsToSell");

            GarageSale gs = new GarageSale(title, description, location, duration, startTime, itemsToSell, category, isPublic);

            if (obj.has("seller")) {
                gs.setSeller(unpackaUser(obj.getJSONObject("seller")));
            }

            initiative = gs;
        }

        else if (category.equals("Gardening")) {
            Gardening g = new Gardening(title, description, location, duration, startTime, category, isPublic);

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

            initiative = g;
        }

        else if (category.equals("ToolSharing")) {
            ToolSharing ts = new ToolSharing(title, description, location, duration, startTime, category, isPublic);

            if (obj.has("loaner")) {
                ts.setLoaner(unpackaUser(obj.getJSONObject("loaner")));
            }

            if (obj.has("lender")) {
                ts.setLender(unpackaUser(obj.getJSONObject("lender")));
            }

            initiative = ts;
        }

        // Handle participants and comments for all initiative types
        if (initiative != null) {
            // Unpack participants
            if (obj.has("participants")) {
                JSONArray participantsArray = obj.getJSONArray("participants");
                List<String> participants = new ArrayList<>();
                for (int i = 0; i < participantsArray.length(); i++) {
                    participants.add(participantsArray.getString(i));
                }
                initiative.setParticipants(participants);
            }

            // Unpack comments
            if (obj.has("comments")) {
                JSONArray commentsArray = obj.getJSONArray("comments");
                System.out.println("[DEBUG] Unpacking " + commentsArray.length() + " comments for initiative: " + title);
                List<Initiative.Comment> comments = new ArrayList<>();
                for (int i = 0; i < commentsArray.length(); i++) {
                    Initiative.Comment comment = unpackComment(commentsArray.getJSONObject(i));
                    if (comment != null) {
                        System.out.println("[DEBUG] Unpacked comment: " + comment.getContent());
                        comments.add(comment);
                    }
                }
                initiative.setCommentList(comments);
            } else {
                System.out.println("[DEBUG] No comments field found for initiative: " + title);
            }
        }

        return initiative;
    }

    private Initiative.Comment unpackComment(JSONObject commentObj) {
        String id = commentObj.getString("id");
        String authorEmail = commentObj.getString("authorEmail");
        String content = commentObj.getString("content");
        String parentId = commentObj.optString("parentId", null);

        Initiative.Comment comment = new Initiative.Comment(id, authorEmail, content, parentId);

        // Unpack likes
        if (commentObj.has("likedBy")) {
            JSONArray likedByArray = commentObj.getJSONArray("likedBy");
            for (int i = 0; i < likedByArray.length(); i++) {
                comment.like(likedByArray.getString(i));
            }
        }

        // Unpack replies
        if (commentObj.has("replies")) {
            JSONArray repliesArray = commentObj.getJSONArray("replies");
            for (int i = 0; i < repliesArray.length(); i++) {
                Initiative.Comment reply = unpackComment(repliesArray.getJSONObject(i));
                if (reply != null) {
                    comment.addReply(reply);
                }
            }
        }

        return comment;
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
