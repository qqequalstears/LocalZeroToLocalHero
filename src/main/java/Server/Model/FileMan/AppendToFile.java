package Server.Model.FileMan;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * The `AppendToFile` class is a singleton implementation of the `IDataAppender`
 * interface.
 * It provides methods to append data to files, such as users,
 * achievements, logs,
 * and active initiatives. The class ensures that data is appended to the
 * specified files
 * and handles file creation if the files do not already exist.
 *
 * @author Jansson Anton
 * @date 2025-04-16
 */
public class AppendToFile implements IDataAppender {
    private static AppendToFile instance;
    private static final File destinationUser = FileDestinationFactory.getUserDataFile();
    private static final File destinationAchievements = FileDestinationFactory.getAchievementDataFile();
    private static final File destinationLog = FileDestinationFactory.getLogDataFile();
    private static final File destinationActiveIntiative = FileDestinationFactory.getActiveInitiativeDataFile();

    /**
     * Returns the singleton instance of the `AppendToFile` class.
     * This method ensures that only one instance of the class is created and shared
     * across the application.
     *
     * @return The singleton instance of the `AppendToFile` class.
     * @author Jansson Anton
     * @date 2025-04-07
     */
    public static synchronized AppendToFile getInstance() {
        if (instance == null) {
            instance = new AppendToFile();
        }
        return instance;
    }

    /**
     * Appends the given data to the specified file.
     * If the file doesn't exist, it will be created. If the file exists, the data
     * will be appended to it.
     *
     * @param file The path of the file where the data should be appended.
     * @param data The data that will be appended to the file.
     * @return A message indicating whether the data was appended successfully or if
     * there was an error.
     * @throws IOException           If an I/O error occurs while writing to the
     *                               file.
     * @throws FileNotFoundException If the specified file cannot be found (although
     *                               this will likely be caught by the FileWriter
     *                               itself).
     * @author Jansson Anton
     * @Date 2025-04-16
     */
    private String appendToFile(File file, String data) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.write(data);
            bw.newLine();
            bw.flush();
            bw.close();
            return "Appended new data to file successfully";
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe);
            return "Internal server error";
        } catch (IOException e) {
            System.out.println(e);
            return "Internal server error";
        }
    }

    // TODO the " appendCollectionToFile() " method
    // is not used in
    // the
    // current
    // implementation of
    // the code, but
    // it might be
    // useful in the
    // future.
    // The method is NOT
    // finished yet and
    // needs further
    // dev.
    // 2025-04-16
    // @Jansson
    private String appendCollectionToFile(File destination, List<ISavableObject> listOfObjects) {

        for (ISavableObject savableObject : listOfObjects) {
            String data = savableObject.getSavableString();
            appendToFile(destination, data);
        }
        return "";
    }

    /**
     * Append the given data to the users file.
     *
     * @param data The data that will be appended to the users file.
     * @return A message indicating whether the data was appended successfully or if
     * an error occurred.
     * @author Jansson Anton
     * @Date 2025-04-07
     */
    private String appendToUsers(String data) {
        return appendToFile(destinationUser, data);
    }

    /**
     * Append the given data to the achievements file,(this without appending).
     *
     * @param data The data that will be appended to the achievements file.
     * @return A message indicating whether the data was appended successfully or if
     * an error occurred.
     * @author Jansson Anton
     * @Date 2025-04-07
     */
    private String appendToAchievements(String data) {
        return appendToFile(destinationAchievements, data);
    }

    /**
     * Appends the given data to the log file.
     *
     * @param data The data that will be appended to the log file.
     * @return A message indicating whether the data was appended successfully or if an error occurred.
     * @author Jansson Anton
     * @date 2025-04-16
     */
    private String appendToLog(String data) {
        return appendToFile(destinationLog, data);
    }

    /**
     * Appends the given data to the active initiative file.
     *
     * @param data The data that will be appended to the active initiative file.
     * @return A message indicating whether the data was appended successfully or if an error occurred.
     * @author Jansson Anton
     * @date 2025-04-16
     */
    private String appendToActiveIntiative(String data) {
        return appendToFile(destinationActiveIntiative, data);
    }

    /**
     * Appends the given data to the users file.
     *
     * @param data The data that will be appended to the users file.
     * @return A message indicating whether the data was appended successfully or if an error occurred.
     * @author Jansson Anton
     * @date 2025-04-07
     */
    @Override
    public String appendToUsersFile(String data) {
        return appendToUsers(data);
    }

    /**
     * Appends the given data to the achievements file.
     *
     * @param data The data that will be appended to the achievements file.
     * @return A message indicating whether the data was appended successfully or if an error occurred.
     * @author Jansson Anton
     * @date 2025-04-07
     */
    @Override
    public String appendToAchievementsFile(String data) {
        return appendToAchievements(data);
    }

    /**
     * Appends the given log entry to the log file.
     *
     * @param logEntery The log entry that will be appended to the log file.
     * @return A message indicating whether the log entry was appended successfully or if an error occurred.
     * @author Jansson Anton
     * @date 2025-04-16
     */
    @Override
    public String appendLogEnteryToFile(String logEntery) {
        return appendToLog(logEntery);
    }

    /**
     * Appends the given data to the active initiative file.
     *
     * @param data The data that will be appended to the active initiative file.
     * @return A message indicating whether the data was appended successfully or if an error occurred.
     * @author Jansson Anton
     * @date 2025-04-16
     */
    @Override
    public String appendActiveIntiativeToFile(String data) {
        return appendToActiveIntiative(data);
    }


}
