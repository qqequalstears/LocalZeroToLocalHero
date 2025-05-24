package Server.Controller;

import Client.Model.Initiative.Children.CarPool;
import Client.Model.Initiative.Children.GarageSale;
import Client.Model.Initiative.Children.ToolSharing;
import Client.Model.Initiative.Parent.Initiative;
import Server.Model.AchievementTracker;
import org.json.JSONObject;

public class InitiativeManager {

    public InitiativeManager() {}

    public boolean createNewInitiative(JSONObject newInitiative) {
        String name = newInitiative.getString("name");
        String description = newInitiative.getString("description");
        String location = newInitiative.getString("location");
        String duration = newInitiative.getString("duration");
        String startTime = newInitiative.getString("startTime");
        String numberOfSeats = newInitiative.getString("numberOfSeats");
        String sellList = newInitiative.getString("sellList");
        String category = newInitiative.getString("category");
        String creator = newInitiative.getString("creator");
        boolean isPublic = newInitiative.getBoolean("isPublic");
        Initiative initiativeToCreate = null;
        boolean dataIsValid;

        switch (category) {
            case "Carpool":
                dataIsValid = validateCarpool(name,description,location,duration,startTime,numberOfSeats);
                initiativeToCreate = new CarPool(name, description, location, duration, startTime, numberOfSeats, category, isPublic);
                break;
            case "Garage Sale":
                dataIsValid = validateGarageSale(name,description,location,duration,startTime,sellList);
                initiativeToCreate = new GarageSale(name,description,location,duration,startTime,sellList, category, isPublic);
                break;
            case "Gardening", "ToolSharing":
                dataIsValid = validateRegularInitiative(name,description,location,duration,startTime);
                initiativeToCreate = new ToolSharing(name,description,location,duration,startTime, category, isPublic);
                break;
            default:
                dataIsValid = false;
        }
        if (dataIsValid) {
            String initiativeCSV = formatNewInitiativeToCSV(initiativeToCreate, creator);
            FileHandler.getInstance().createInitiative(initiativeCSV);
            AchievementTracker.getInstance().improveAchievementCSV(initiativeToCreate);
            return true;
        }
        return false;
    }

    private boolean validateRegularInitiative(String name, String description, String location, String duration, String startTime) {
        return  isValidField(name) &&
                isValidField(description) &&
                isValidField(location) &&
                isValidField(duration) &&
                isValidField(startTime);
    }

    private boolean validateCarpool(String name, String description, String location, String duration, String startTime, String numberOfSeats) {
        return validateRegularInitiative(name, description, location, duration, startTime) && isValidField(numberOfSeats);
    }

    private boolean validateGarageSale(String name, String description, String location, String duration, String startTime, String sellList) {
        return validateRegularInitiative(name, description, location, duration, startTime) && isValidField(sellList);
    }

    private boolean isValidField(String input) {
        return input != null && !input.isEmpty() && !input.contains(",");
    }

    private String formatNewInitiativeToCSV(Initiative initiative, String creator) {

        String category = initiative.getCategory();
        String title = initiative.getTitle();
        String description = initiative.getDescription();
        String location = initiative.getLocation();
        String duration = initiative.getDuration();
        String startTime = initiative.getStartTime();
        String isPublic = String.valueOf(initiative.isPublic());

        String participant = "";
        String participants = "";
        String itemsToSell = "";
        String numberOfSeats = "";

        if (initiative instanceof CarPool carPool) {
            numberOfSeats = carPool.getNumberOfSeats();
        } else if(initiative instanceof GarageSale garageSale) {
            itemsToSell = garageSale.getItemsToSell();
        }
        return String.join(",", category, title, description, location, duration, startTime, creator, participant, participants, isPublic, itemsToSell, numberOfSeats);
    }

}
