package Server.Model.FileMan;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The WriteToFile class provides methods for writing data to a file.
 * It supports two main operations: appending data to a file and overwriting the
 * file with new data.
 * The class follows the Singleton design pattern to ensure that only one
 * instance of WriteToFile exists at any time.
 * It also handles file creation if the file doesn't already exist and manages
 * common I/O errors.
 * <p>
 * This class is used for managing file writing operations where:
 * - Data can be appended to an existing file.
 * - The file can be overwritten with new content.
 * <p>
 * The class handles errors related to file writing and provides informative
 * messages in case of failure.
 *
 * @author Jansson Anton
 * @Date 2025-04-07
 */
public class WriteToFile implements IDataWriter {
    private static final File destinationUser = FileDestinationFactory.getUserDataFile();
    private static final File destinationAchievements = FileDestinationFactory.getAchievementDataFile();
    private static final File destinationLog = FileDestinationFactory.getLogDataFile();
    private static final File destinationActiveIntiative = FileDestinationFactory.getActiveInitiativeDataFile();

    private static WriteToFile instance;


    private WriteToFile(){}
    /**
     * Provides a singleton instance of the WriteToFile class.
     * Ensures that only one instance of WriteToFile exists at any given time.
     * If an instance does not already exist, it creates a new one; otherwise, it
     * returns the existing instance.
     *
     * @return The singleton instance of the WriteToFile class.
     * @author Jansson Anton
     * @Date 2025-04-15
     */
    public static synchronized WriteToFile getInstance() {
        if (instance == null) {
            instance = new WriteToFile();
        }
        return instance;
    }

    /**
     * Writes the given data to the specified file.
     * The file is overwritten (not appended) when this method is called.
     * If the file does not exist, it will be created.
     *
     * @param file   The path of the file where the data should be written.
     * @param header The header to be written at the top of the file.
     * @param data   The content to be written to the file.
     * @return A message indicating the result of the operation.
     * @author Jansson Anton
     * @Date 2025-04-15
     */
    private String writeToCSVFile(File file, String header, String data) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
            if (header != null && !header.isEmpty()) {
                bw.write(header);
            } else {
                bw.close();
                return "Header is empty or null";
            }
            bw.write(data);
            bw.newLine();
            bw.flush();
            bw.close();
            return "Wrote new data to file successfully";
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe);
            return "Internal server error";
        } catch (IOException e) {
            System.out.println(e);
            return "Internal server error";
        }
    }

    /**
     * /**
     * Writes the given data to the users file, (this without appending).
     *
     * @param data The data that will be written to the users file.
     * @return A message indicating whether the data was written successfully or if
     * an error occurred.
     * @author Jansson Anton
     * @Date 2025-04-07
     */
    private String writeToUsers(String data) {
        String header = "email,password,name,location,roles\n";
        return writeToCSVFile(destinationUser, header, data);
    }

    /**
     * Writes the given data to the achievements file,(this without appending).
     *
     * @param data The data that will be written to the achievements file.
     * @return A message indicating whether the data was written successfully or if
     * an error occurred.
     * @author Jansson Anton
     * @Date 2025-04-07
     */
    private String writeToAchievements(String data) {
        String header = "achievementID,achievementName,achievementDescription,achievementPoints,location\n"; // TODO this is a
        // placeholder. The
        // header needs to
        // be changed to the
        // corresponding
        // header.
        return writeToCSVFile(destinationAchievements, header, data);
    }

    /**
     * Writes the given log entry to the log file.
     * The file will be overwritten (not appended) when this method is called.
     * If the file does not exist, it will be created.
     *
     * @param logEntry The log entry to be written to the log file.
     * @return A message indicating whether the log entry was written successfully
     * or if an error occurred.
     * @author Jansson Anton
     * @Date 2025-04-16
     */
    private String writeToLog(String logEntry) {
        String header = "logID,logEntry,date,time\n"; // TODO this is a placeholder. The header needs to be changed to the
        // corresponding header.
        return writeToCSVFile(destinationLog, header, logEntry);
    }

    /**
     * Writes the given data to the active initiative file.
     * The file will be overwritten (not appended) when this method is called.
     * If the file does not exist, it will be created.
     *
     * @param data The data to be written to the active initiative file.
     * @return A message indicating whether the data was written successfully or if
     * an error occurred.
     * @author Jansson Anton
     * @Date 2025-04-16
     */
    private String writeToActiveIntiative(String data) {
        String header = "intiativeID,intiativeName,intiativeDescription,intiativePoints\n"; // TODO this is a
        // placeholder. The header
        // needs to be changed to
        // the corresponding header.
        return writeToCSVFile(destinationActiveIntiative, header, data);
    }

    /**
     * Writes the given user data to the users file.
     *
     * @param data The user data to be written to the file.
     * @return A message indicating whether the user data was written successfully
     * or if an error occurred.
     * @author Jansson Anton
     * @Date 2025-04-07
     */
    @Override
    public String writeUsersToFile(String data) {
        return writeToUsers(data);
    }

    /**
     * Writes the given achievements data to the achievements file.
     *
     * @param data The achievements data to be written to the file.
     * @return A message indicating whether the achievements data was written
     * successfully or if an error occurred.
     * @author Jansson Anton
     * @Date 2025-04-07
     */
    @Override
    public String writeAchievementsToFile(String data) {
        return writeToAchievements(data);
    }

    /**
     * Writes the given active initiative data to the active initiative file.
     *
     * @param data The active initiative data to be written to the file.
     * @return A message indicating whether the active initiative data was written
     * successfully or if an error occurred.
     * @author Jansson Anton
     * @Date 2025-04-16
     */
    @Override
    public String writeActiveInitiativeToFile(String data) {
        return writeToActiveIntiative(data);
    }

    /**
     * Writes the given log entry to the log file.
     *
     * @param logEntry The log entry to be written to the file.
     * @return A message indicating whether the log entry was written successfully
     * or if an error occurred.
     * @author Jansson Anton
     * @Date 2025-04-16
     */
    @Override
    public String writeLogEntryToFile(String logEntry) {
        return writeToLog(logEntry);
    }



    public void replaceUsers(String csvContent) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(destinationUser))) {
            writer.write(csvContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}