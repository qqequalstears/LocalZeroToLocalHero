package Server.Controller;

import Client.Model.Initiative.Parent.Initiative;
import Client.Model.User;
import Server.Model.FileMan.*;
import Server.Model.FileMan.Proxy.FileAppenderProxy;
import Server.Model.FileMan.Proxy.FileReaderProxy;
import Server.Model.FileMan.Proxy.FileWriterProxy;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class FileHandler {
    private static FileHandler instance;

    private FileHandler() {
        // Private constructor to prevent instantiation
    }

    public static synchronized FileHandler getInstance() {
        synchronized (FileHandler.class) {
            if (instance == null) {
                instance = new FileHandler();
            }
        }
        return instance;
    }

    // Mock method for this class. This is not a real method, but it is used to
    // demonstrate how one might save data to a file.
    public String saveAllUsers(String dString) {
        IDataSaver dataSaver = FileWriterProxy.getInstance();
        dataSaver.saveUser(dString);
        return "Data saved successfully";
    }

    public List<User> getUsers() {
        //return ReaderFiles.getInstance().fetchAllUsers();
        //return FileReaderProxy.getInstance().fetchAllUsers();
        IDataFetcher dataFetcher = FileReaderProxy.getInstance();
        return dataFetcher.fetchAllUsers();
    }

    public String registerUser(String csvContent) {
        IDataSaver dataAppender = FileAppenderProxy.getInstance();
        //test.appendToUsersFile(csvContent);
        dataAppender.saveUser(csvContent);
        return "Data saved successfully";

    }

    public String createInitiative(String csvContent) {
        IDataSaver dataAppender = FileAppenderProxy.getInstance();
        dataAppender.saveActiveIntiative(csvContent);
        return "Data saved successfully";
    }

    public String replaceRoles(String mail, List<String> newRoles) {
        String csvContent = updateUsersNewRoles(mail, newRoles);
        //WriteToFile.getInstance().replaceUsers(csvContent);
        //  FileWriterProxy.getInstance().writeUsersToFile(csvContent);
        IDataSaver dataSaver = FileWriterProxy.getInstance();
        dataSaver.saveUser(csvContent);
        return "Roles replaced successfully";
    }


    /**
     * @param mail
     * @param newRoles
     * @return
     * @author Anton Persson
     */
    public String updateUsersNewRoles(String mail, List<String> newRoles) {
        IDataFetcher dataFetcher = FileReaderProxy.getInstance();
        String csvContent = dataFetcher.fetchAllUserData();
        String[] lines = csvContent.split("\n");

        StringBuilder updatedCSV = new StringBuilder();
        //  updatedCSV.append(lines[0]).append("\n");

        for (int i = 1; i < lines.length; i++) {
            String[] contents = lines[i].split(",");
            if (contents.length < 5) {
                continue;
            }
            String email = contents[0].trim();
            if (email.equals(mail)) {
                String joinedRoles = String.join(" - ", newRoles);
                StringBuilder updatedLine = new StringBuilder();
                for (int j = 0; j < 4; j++) {
                    updatedLine.append(contents[j]).append(",");
                }
                updatedLine.append(joinedRoles);
                updatedCSV.append(updatedLine).append("\n");
            } else {
                updatedCSV.append(lines[i]).append("\n");
            }
        }
        return updatedCSV.toString();
    }

    public void updateAchievements(String csvContent) { //TODO oanvänd metod?
        AppendToFile.getInstance().appendToAchievementsFile(csvContent);
    }

    public List<Initiative> getAllActiveInitiatives() {

        return ReaderFiles.getInstance().fetchAllActiveInitiatives();
    }

    public String fetchAllLogData() {
        IDataFetcher dataFetcher = FileReaderProxy.getInstance();
        return dataFetcher.fetchAllLogData();
    }

    public void appendLogEntryToFile(String logEntryString) {
        IDataSaver dataSaver = FileAppenderProxy.getInstance();
        dataSaver.saveLog(logEntryString);
    }

    public String fetchOneUserLocationData(String email) {
        IDataFetcher dataFetcher = FileReaderProxy.getInstance();
        return dataFetcher.fetchOneUserLocationData(email);
    }

    public String fetchAllAchievementsData() {
        IDataFetcher dataFetcher = FileReaderProxy.getInstance();
        return dataFetcher.fetchAllAchievementsData();
    }

    public String writeAchievementsToFile(String data) {
        IDataSaver dataSaver = FileWriterProxy.getInstance();
        return dataSaver.saveAchievements(data);
    }

    public void updateInitiative(Initiative initiative) {
        // Get all current initiatives
        List<Initiative> allInitiatives = getAllActiveInitiatives();
        
        // Find and replace the initiative with the updated one
        for (int i = 0; i < allInitiatives.size(); i++) {
            if (allInitiatives.get(i).getTitle().equals(initiative.getTitle())) {
                allInitiatives.set(i, initiative);
                break;
            }
        }
        
        // Rebuild the CSV content and save it
        StringBuilder csvContent = new StringBuilder();
        csvContent.append("Category,Title,Description,Location,Duration,StartTime,Creator,Participant,Participants,IsPublic,ItemsToSell,NumberOfSeats,Comments\n");
        
        for (Initiative init : allInitiatives) {
            String category = init.getCategory();
            String title = init.getTitle();
            String description = init.getDescription();
            String location = init.getLocation();
            String duration = init.getDuration();
            String startTime = init.getStartTime();
            String isPublic = String.valueOf(init.isPublic());
            
            String participants = "";
            if (init.getParticipants() != null && !init.getParticipants().isEmpty()) {
                participants = String.join(";", init.getParticipants());
            }
            
            String itemsToSell = "";
            String numberOfSeats = "";
            
            if (init instanceof Client.Model.Initiative.Children.CarPool carPool) {
                numberOfSeats = carPool.getNumberOfSeats();
            } else if (init instanceof Client.Model.Initiative.Children.GarageSale garageSale) {
                itemsToSell = garageSale.getItemsToSell();
            }
            
            // Serialize comments to JSON string
            String commentsJson = "";
            if (init.getCommentList() != null && !init.getCommentList().isEmpty()) {
                JSONArray commentsArray = new JSONArray();
                for (Initiative.Comment comment : init.getCommentList()) {
                    commentsArray.put(packComment(comment));
                }
                // Use Base64 encoding instead of character replacement to avoid encoding issues
                String jsonString = commentsArray.toString();
                commentsJson = java.util.Base64.getEncoder().encodeToString(jsonString.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            }
            
            csvContent.append(String.join(",", category, title, description, location, duration, startTime, "", "", participants, isPublic, itemsToSell, numberOfSeats, commentsJson)).append("\n");
        }
        
        // Save the updated CSV
        IDataSaver dataSaver = FileWriterProxy.getInstance();
        dataSaver.saveActiveIntiative(csvContent.toString());
    }

    private JSONObject packComment(Initiative.Comment comment) {
        JSONObject commentJson = new JSONObject();
        commentJson.put("id", comment.getId());
        commentJson.put("authorEmail", comment.getAuthorEmail());
        commentJson.put("content", comment.getContent());
        commentJson.put("parentId", comment.getParentId());
        commentJson.put("likes", comment.getLikes());
        
        JSONArray likedByArray = new JSONArray();
        for (String email : comment.getLikedBy()) {
            likedByArray.put(email);
        }
        commentJson.put("likedBy", likedByArray);
        
        JSONArray repliesArray = new JSONArray();
        for (Initiative.Comment reply : comment.getReplies()) {
            repliesArray.put(packComment(reply));
        }
        commentJson.put("replies", repliesArray);
        
        return commentJson;
    }
}
