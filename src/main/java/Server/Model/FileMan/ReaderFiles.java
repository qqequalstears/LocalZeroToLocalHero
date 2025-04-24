package Server.Model.FileMan;

import java.io.*;
import java.util.List;

import Server.Model.User;

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
    private static final File destinationActiveIntiative=FileDestinationFactory.getActiveInitiativeDataFile();

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
     *         occurs, an error message is returned.
     *         Returns "File not found at: <destination>" if the file is not found,
     *         or "Error reading the file: <message>" if an error occurs during
     *         reading.
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
     *         If an error occurs during reading, an error message is returned.
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
     *         If an error occurs during reading, an error message is returned.
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
     *         If an error occurs during reading, an error message is returned.
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
     *         If an error occurs during reading, an error message is returned.
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
     *         If an error occurs during reading, an error message is returned.
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
     * @author Jansson Anton
     * @Date 2025-04-16
     */
    @Override
    public List<User> fetchAllUsers() {
        throw new UnsupportedOperationException("Unimplemented method 'fetchAllUsers'");
    }

    /**
     * Fetches all achievement data from the achievements file.
     *
     * @return A string containing all achievement data.
     *         If an error occurs during reading, an error message is returned.
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
     *         If an error occurs during reading, an error message is returned.
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
     *         If an error occurs during reading, an error message is returned.
     * @author Jansson Anton
     * @Date 2025-04-16
     */
    @Override
    public String fetchAllActiveIntiativeData() {
        return readActiveIntiative();
    }

}
