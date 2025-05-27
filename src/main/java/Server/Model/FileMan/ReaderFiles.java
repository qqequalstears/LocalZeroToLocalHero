package Server.Model.FileMan;

import Client.Model.Achievement;
import Client.Model.Initiative.Children.CarPool;
import Client.Model.Initiative.Children.GarageSale;
import Client.Model.Initiative.Children.Gardening;
import Client.Model.Initiative.Children.ToolSharing;
import Client.Model.Initiative.Parent.Initiative;
import Client.Model.Role;
import Client.Model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * The FileReader class provides methods for reading data from a file.
 * It follows the Singleton design pattern, ensuring only one instance of the
 * class exists at any time.
 * The class offers functionality to read the entire content of a file as a
 * string.
 * It handles common file I/O operations, such as file reading, error handling,
 * and empty file handling.
 * <p>
 * The class includes methods for:
 * - Reading the entire content of a file.
 * - Handling cases where the file does not exist or when an error occurs during
 * reading.
 *
 * @author Jansson Anton
 * @date 2025-04-07
 */

public class ReaderFiles implements IDataFetcher {

    private static ReaderFiles instance;

    private static final File destinationUser = FileDestinationFactory.getUserDataFile();
    private static final File destinationAchievements = FileDestinationFactory.getAchievementDataFile();
    private static final File destinationLog = FileDestinationFactory.getLogDataFile();
    private static final File destinationActiveIntiative = FileDestinationFactory.getActiveInitiativeDataFile();

    private ReaderFiles() {
    }

    /**
     * Provides a singleton instance of the FileReader class.
     * This method ensures that only one instance of FileReader exists at any
     * given time.
     * If an instance does not already exist, it creates a new one; otherwise, it
     * returns the existing instance.
     *
     * @return The singleton instance of the FileReader class.
     * @author Jansson Anton
     * @Date 2025-04-07
     */
    public static synchronized ReaderFiles getInstance() {
        if (instance == null) {
            instance = new ReaderFiles();
        }
        return instance;
    }

    /**
     * Reads the entire content of a file and returns it as a string with \n for
     * every line in the file.
     * If the file is empty, it returns an empty string.
     * If the file does not exist or an error occurs during reading, an error
     * message is returned.
     *
     * @param file The path to the file to be read. This can be an absolute
     *             or relative file path.
     * @return A string containing the entire content of the file. If an error
     * occurs, an error message is returned.
     * Returns "File not found at: <destination>" if the file is not found,
     * or "Error reading the file: <message>" if an error occurs during
     * reading.
     * @throws FileNotFoundException If the file does not exist at the specified
     *                               destination.
     * @throws IOException           If an I/O error occurs while reading the file.
     * @author Jansson Anton
     * @Date 2025-04-07
     */
    private String readWholeCSVFile(File file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder returnString = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                returnString.append(line + "\n");
            }
            br.close();
            return returnString.toString();

        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe);
            return "File not found at: " + file;
        } catch (IOException e) {
            return "Error reading the file: " + e.getMessage();
        }
    }

    /**
     * Reads the content of the users file and returns it as a {@String}.
     *
     * @return A containing the entire content of the users file.
     * If an error occurs during reading, an error message is returned.
     * @author Jansson Anton
     * @Date 2025-04-07
     */
    private String readUsers() {
        return readWholeCSVFile(destinationUser);
    }

    /**
     * Reads the content of the achievements file and returns it as a string.
     *
     * @return A string containing the entire content of the achievements file.
     * If an error occurs during reading, an error message is returned.
     * @author Jansson Anton
     * @Date 2025-04-07
     */
    private String readAchievements() {
        return readWholeCSVFile(destinationAchievements);
    }

    /**
     * Reads the content of the log file and returns it as a string.
     *
     * @return A string containing the entire content of the log file.
     * If an error occurs during reading, an error message is returned.
     * @author Jansson Anton
     * @Date 2025-04-16
     */
    private String readLog() {
        return readWholeCSVFile(destinationLog);
    }

    /**
     * Reads the content of the active initiative file and returns it as a string.
     *
     * @return A string containing the entire content of the active initiative file.
     * If an error occurs during reading, an error message is returned.
     * @author Jansson Anton
     * @Date 2025-04-16
     */
    private String readActiveIntiative() {
        return readWholeCSVFile(destinationActiveIntiative);
    }

    /**
     * Fetches all user data from the users file.
     *
     * @return A string containing all user data.
     * If an error occurs during reading, an error message is returned.
     * @author Jansson Anton
     * @Date 2025-04-16
     */
    @Override
    public String fetchAllUserData() {
        return readUsers();
    }

    /**
     * Fetches all users as a list of {@code User} objects.
     *
     * @return A list of {@code User} objects.
     * @throws UnsupportedOperationException This method is not yet implemented.
     * @author Anton Persson
     * @Date 2025-04-16
     */
    @Override
    public List<User> fetchAllUsers() {
        List<User> users = new ArrayList<>();
        String csvContent = readWholeCSVFile(destinationUser);
        String[] lines = csvContent.split("\n");

        for (int i = 1; i < lines.length; i++) {
            String[] contents = lines[i].split(",");
            if (contents.length < 5) {
                continue;
            }
            String email = contents[0].trim();
            String password = contents[1].trim();
            String name = contents[2].trim();
            String location = contents[3].trim();
            String rolesString = contents[4].trim();

            List<Role> roles = new ArrayList<>();
            String[] roleContents = rolesString.split("-");
            for (String role : roleContents) {
                role = role.trim();
                roles.add(Role.valueOf(role));
            }
            users.add(new User(name, location, email, password, roles));
        }
        return users;
    }

    /**
     * Fetches all achievement data from the achievements file.
     *
     * @return A string containing all achievement data.
     * If an error occurs during reading, an error message is returned.
     * @author Jansson Anton
     * @Date 2025-04-16
     */
    @Override
    public String fetchAllAchievementsData() {
        return readAchievements();
    }

    /**
     * Fetches all user achievement data.
     *
     * @return A string containing all user achievement data.
     * @throws UnsupportedOperationException This method is not yet implemented.
     * @author Jansson Anton
     * @Date 2025-04-16
     */
    @Override
    public String fetchAllUserAchievementsData() {
        throw new UnsupportedOperationException("Unimplemented method 'fetchUserAchievementsData'");
    }

    /**
     * Fetches achievement data for a specific user.
     *
     * @param userName The name of the user whose achievement data is to be fetched.
     * @return A string containing the achievement data for the specified user.
     * @throws UnsupportedOperationException This method is not yet implemented.
     * @author Jansson Anton
     * @Date 2025-04-16
     */
    @Override
    public String fetchUserAchievementsData(String userName) {
        throw new UnsupportedOperationException("Unimplemented method 'fetchUserAchievementsData'");
    }

    /**
     * Fetches all log data from the log file.
     *
     * @return A string containing all log data.
     * If an error occurs during reading, an error message is returned.
     * @author Jansson Anton
     * @Date 2025-04-16
     */
    @Override
    public String fetchAllLogData() {
        return readLog();
    }

    /**
     * Fetches all active initiative data from the active initiative file.
     *
     * @return A string containing all active initiative data.
     * If an error occurs during reading, an error message is returned.
     * @author Jansson Anton
     * @Date 2025-04-16
     */
    @Override
    public String fetchAllActiveIntiativeData() {
        return readActiveIntiative();
    }

    @Override
    public String fetchOneUserLocationData(String email) {
        List<User> users = fetchAllUsers();

        for (User user : users) {
            if (user.getEmail().equals(email)) {

                return user.getLocation();
            }
        }
        return "User not found";
    }


    /**
     * Returns all active initiatives.
     *
     * @return 2025-05-22
     * @author Martin Frick
     */
    @Override
    public List<Initiative> fetchAllActiveInitiatives() {
        List<Initiative> initiatives = new ArrayList<>();
        String csvContent = readWholeCSVFile(destinationActiveIntiative);
        System.out.println("[DEBUG] CSV content: " + csvContent);
        String[] lines = csvContent.split("\n");
        System.out.println("[DEBUG] Number of lines in CSV: " + lines.length);

        for (int i = 1; i < lines.length; i++) {
            String[] contents = lines[i].split(",", -1);
            System.out.println("[DEBUG] Line " + i + " split into " + contents.length + " parts: " + java.util.Arrays.toString(contents));
            if (contents.length < 12) {
                System.out.println("[DEBUG] Skipping line " + i + " - insufficient columns");
                continue;
            }

            String initiativeID = contents[0].trim();
            String title = contents[1].trim();
            String description = contents[2].trim();
            String location = contents[3].trim();
            String duration = contents[4].trim();
            String startTime = contents[5].trim();
            String creator = contents[6].trim();
            String participant = contents[7].trim();
            String participants = contents[8].trim();
            boolean isPublic = Boolean.parseBoolean(contents[9].trim());
            String itemsToSell = contents[10].trim();
            String numberOfSeats = contents[11].trim();
            String commentsJson = contents.length > 12 ? contents[12].trim() : "";
            List<String> comments = new ArrayList<>();
            List<String> likes = new ArrayList<>();
            List<Achievement> achievements = new ArrayList<>();

            System.out.println("[DEBUG] Processing initiative: " + initiativeID + " - " + title + " at " + location);

            Initiative initiative = null;
            switch (initiativeID) {
                case "CarPool":
                    initiative = new CarPool(title, description, location, duration, startTime, numberOfSeats, initiativeID, isPublic);
                    System.out.println("[DEBUG] Created CarPool initiative");
                    break;
                case "Garage Sale":
                    initiative = new GarageSale(title, description, location, duration, startTime, itemsToSell, initiativeID, isPublic);
                    System.out.println("[DEBUG] Created GarageSale initiative");
                    break;
                case "Gardening":
                    initiative = new Gardening(initiativeID, title, description, location, duration, startTime, comments, likes, isPublic, achievements);
                    System.out.println("[DEBUG] Created Gardening initiative");
                    break;
                case "ToolSharing":
                    initiative = new ToolSharing(initiativeID, title, description, location, duration, startTime, comments, likes, isPublic, achievements);
                    System.out.println("[DEBUG] Created ToolSharing initiative");
                    break;
                default:
                    System.out.println("[DEBUG] Unknown initiative type: " + initiativeID);
            }
            
            if (initiative != null) {
                // Parse and set participants
                if (!participants.isEmpty()) {
                    String[] participantArray = participants.split(";");
                    List<String> participantList = new ArrayList<>();
                    for (String p : participantArray) {
                        if (!p.trim().isEmpty()) {
                            participantList.add(p.trim());
                        }
                    }
                    initiative.setParticipants(participantList);
                    System.out.println("[DEBUG] Set participants for " + title + ": " + participantList);
                }
                
                // Parse and set comments
                if (!commentsJson.isEmpty()) {
                    try {
                        String unescapedJson = commentsJson.replace("§", ",").replace("¶", "\n");
                        JSONArray commentsArray = new JSONArray(unescapedJson);
                        List<Initiative.Comment> commentList = new ArrayList<>();
                        for (int j = 0; j < commentsArray.length(); j++) {
                            Initiative.Comment comment = unpackComment(commentsArray.getJSONObject(j));
                            if (comment != null) {
                                commentList.add(comment);
                            }
                        }
                        initiative.setCommentList(commentList);
                        System.out.println("[DEBUG] Loaded " + commentList.size() + " comments for " + title);
                    } catch (Exception e) {
                        System.out.println("[DEBUG] Error parsing comments for " + title + ": " + e.getMessage());
                    }
                }
                
                initiatives.add(initiative);
                System.out.println("[DEBUG] Added initiative to list: " + title);
            }
        }

        System.out.println("[DEBUG] Total initiatives loaded: " + initiatives.size());
        return initiatives;
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

}
