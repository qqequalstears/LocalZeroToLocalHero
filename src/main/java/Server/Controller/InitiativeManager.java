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
        
        System.out.println("[DEBUG] Creating initiative - Category: " + category + ", Name: " + name + ", NumberOfSeats: " + numberOfSeats);

        switch (category) {
            case "CarPool":
            case "Carpool":
                dataIsValid = validateCarpool(name,description,location,duration,startTime,numberOfSeats);
                System.out.println("[DEBUG] Carpool validation result: " + dataIsValid);
                if (dataIsValid) {
                    initiativeToCreate = new CarPool(name, description, location, duration, startTime, numberOfSeats, "CarPool", isPublic);
                }
                break;
            case "Garage Sale":
                dataIsValid = validateGarageSale(name,description,location,duration,startTime,sellList);
                System.out.println("[DEBUG] GarageSale validation result: " + dataIsValid);
                if (dataIsValid) {
                    initiativeToCreate = new GarageSale(name,description,location,duration,startTime,sellList, category, isPublic);
                }
                break;
            case "Gardening":
                dataIsValid = validateRegularInitiative(name,description,location,duration,startTime);
                System.out.println("[DEBUG] Gardening validation result: " + dataIsValid);
                if (dataIsValid) {
                    initiativeToCreate = new ToolSharing(name,description,location,duration,startTime, category, isPublic);
                }
                break;
            case "ToolSharing":
                dataIsValid = validateRegularInitiative(name,description,location,duration,startTime);
                System.out.println("[DEBUG] ToolSharing validation result: " + dataIsValid);
                if (dataIsValid) {
                    initiativeToCreate = new ToolSharing(name,description,location,duration,startTime, category, isPublic);
                }
                break;
            default:
                System.out.println("[DEBUG] Unknown category: " + category);
                dataIsValid = false;
        }
        System.out.println("[DEBUG] Final validation - dataIsValid: " + dataIsValid + ", initiativeToCreate: " + (initiativeToCreate != null));
        
        // TEMPORARY: Force success for testing
        if (initiativeToCreate != null) {
            System.out.println("[DEBUG] FORCING SUCCESS FOR TESTING");
            dataIsValid = true;
        }
        
        if (dataIsValid && initiativeToCreate != null) {
            // Add creator as the first participant
            initiativeToCreate.join(creator);
            
            String initiativeCSV = formatNewInitiativeToCSV(initiativeToCreate, creator);
            System.out.println("[DEBUG] Generated CSV: " + initiativeCSV);
            FileHandler.getInstance().createInitiative(initiativeCSV);
            AchievementTracker.getInstance().improveAchievementCSV(initiativeToCreate);
            System.out.println("[DEBUG] Initiative created successfully!");
            return true;
        }
        System.out.println("[DEBUG] Initiative creation failed!");
        return false;
    }

    private boolean validateRegularInitiative(String name, String description, String location, String duration, String startTime) {
        boolean nameValid = isValidField(name);
        boolean descValid = isValidField(description);
        boolean locValid = isValidField(location);
        boolean durValid = isValidField(duration);
        boolean timeValid = isValidField(startTime);
        System.out.println("[DEBUG] Regular validation - Name: " + nameValid + ", Desc: " + descValid + ", Loc: " + locValid + ", Dur: " + durValid + ", Time: " + timeValid);
        return nameValid && descValid && locValid && durValid && timeValid;
    }

    private boolean validateCarpool(String name, String description, String location, String duration, String startTime, String numberOfSeats) {
        boolean regularValid = validateRegularInitiative(name, description, location, duration, startTime);
        boolean seatsValid = isValidField(numberOfSeats);
        System.out.println("[DEBUG] Carpool validation - Regular fields: " + regularValid + ", NumberOfSeats: " + seatsValid);
        return regularValid && seatsValid;
    }

    private boolean validateGarageSale(String name, String description, String location, String duration, String startTime, String sellList) {
        return validateRegularInitiative(name, description, location, duration, startTime) && isValidField(sellList);
    }

    private boolean isValidField(String input) {
        boolean isValid = input != null && !input.isEmpty() && !input.contains(",");
        if (!isValid) {
            System.out.println("[DEBUG] Invalid field: '" + input + "' (null: " + (input == null) + ", empty: " + (input != null && input.isEmpty()) + ", contains comma: " + (input != null && input.contains(",")) + ")");
        }
        return isValid;
    }
    
    private boolean isValidFieldOrEmpty(String input) {
        // Allow empty strings for optional fields
        return input != null && !input.contains(",");
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
        if (initiative.getParticipants() != null && !initiative.getParticipants().isEmpty()) {
            participants = String.join(";", initiative.getParticipants());
        }
        String itemsToSell = "";
        String numberOfSeats = "";

        if (initiative instanceof CarPool carPool) {
            numberOfSeats = carPool.getNumberOfSeats();
        } else if(initiative instanceof GarageSale garageSale) {
            itemsToSell = garageSale.getItemsToSell();
        }
        return String.join(",", category, title, description, location, duration, startTime, creator, participant, participants, isPublic, itemsToSell, numberOfSeats);
    }

    public void updateInitiativeParticipants(Initiative initiative) {
        // Update the initiative in the CSV file with new participants
        FileHandler.getInstance().updateInitiative(initiative);
    }

    public void updateInitiativeComments(Initiative initiative) {
        // Update the initiative in the CSV file with new comments
        FileHandler.getInstance().updateInitiative(initiative);
    }

}
