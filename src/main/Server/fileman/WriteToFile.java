package Server.fileman;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import Server.Model.IDataWriter;


/**
 * The WriteToFile class provides methods for writing data to a file.
 * It supports two main operations: appending data to a file and overwriting the file with new data.
 * The class follows the Singleton design pattern to ensure that only one instance of WriteToFile exists at any time.
 * It also handles file creation if the file doesn't already exist and manages common I/O errors.
 * <p>
 * This class is used for managing file writing operations where:
 * - Data can be appended to an existing file.
 * - The file can be overwritten with new content.
 * <p>
 * The class handles errors related to file writing and provides informative messages in case of failure.
 *
 * @author Jansson Anton
 * @Date 2025-04-07
 */
public class WriteToFile implements IDataWriter {

    private static WriteToFile instance;
    private static final String destinationUser = "src\\main\\java\\server\\fileman\\fileStorage\\users.txt";  //TODO the path will prob. change. This is from @Janssons local testing
    private static final String destinationAchievements = "src\\main\\java\\server\\fileman\\fileStorage\\achievements.txt";  //TODO the path will prob. change. This is from @Janssons local testing


    /**
     * Provides a singleton instance of the WriteToFile class.
     * This method ensures that only one instance of WriteToFile exists at any given time.
     * If an instance does not already exist, it creates a new one; otherwise, it returns the existing instance.
     *
     * @return The singleton instance of the WriteToFile class.
     * @author Jansson Anton
     * @Date 2025-04-07
     */
    public static WriteToFile getInstance() {
        if (instance == null) {
            instance = new WriteToFile();
        }
        return instance;
    }


    /**
     * Appends the given data to the specified file.
     * If the file doesn't exist, it will be created. If the file exists, the data will be appended to it.
     *
     * @param destination The path of the file where the data should be appended.
     * @param data The data that will be appended to the file.
     * @return A message indicating whether the data was appended successfully or if there was an error.
     *
     * @throws IOException If an I/O error occurs while writing to the file.
     * @throws FileNotFoundException If the specified file cannot be found (although this will likely be caught by the FileWriter itself).
     * @author Jansson Anton
     * @Date 2025-04-07
     */
    public String appendToFile(String destination,String data) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(destination,true));
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


    /**
     * Writes the given data to the specified file.
     * The file is overwritten (not appended) when this method is called.
     * If the file does not exist, it will be created.
     *
     * @param destination The path of the file where the data should be written.
     *                    This can be an absolute or relative path.
     * @param data The content to be written to the file. If the data is empty or null, it will write nothing to the file.
     * @return A message indicating whether the operation was successful or an error occurred.
     *         Returns "Appended new data to file successfully" on success, or "Internal server error" on failure.
     *
     * @throws FileNotFoundException If the file cannot be created or opened for writing due to incorrect path or permissions.
     * @throws IOException If an I/O error occurs during the writing process, such as a failure to write to the file.
     * @author Jansson Anton
     * @Date 2025-04-07
     */
    public String writeToFile(String destination,String data){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(destination,false));
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


    /**
     * Append the given data to the users file.
     *
     * @param data The data that will be appended to the users file.
     * @return A message indicating whether the data was appended successfully or if an error occurred.
     * @author Jansson Anton
     * @Date 2025-04-07
     */
    public String appendToUsers(String data) {
        return appendToFile(destinationUser, data);
    }


    /**
     * Append the given data to the achievements file,(this without appending).
     *
     * @param data The data that will be appended to the achievements file.
     * @return A message indicating whether the data was appended successfully or if an error occurred.
     * @author Jansson Anton
     * @Date 2025-04-07
     */
    public String appendToAchievements(String data) {
        return appendToFile(destinationAchievements, data);
    }

    /**
     * Writes the given data to the users file, (this without appending).
     *
     * @param data The data that will be written to the users file.
     * @return A message indicating whether the data was written successfully or if an error occurred.
     * @author Jansson Anton
     * @Date 2025-04-07
     */
    public String writeToUsers(String data) {
        return writeToFile(destinationUser, data);
    }


    /**
     * Writes the given data to the achievements file,(this without appending).
     *
     * @param data The data that will be written to the achievements file.
     * @return A message indicating whether the data was written successfully or if an error occurred.
     * @author Jansson Anton
     * @Date 2025-04-07
     */
    public String writeToAchievements(String data) {
        return writeToFile(destinationAchievements, data);
    }


    @Override
    public String writeUsersToFile(String data) {
    return writeToUsers(data);
    }


    @Override
    public String appendToUsersFile(String data) {
    return appendToUsers(data);
    }


    @Override
    public String writeAchievementsToFile(String data) {
        return writeToAchievements(data);
    }


    @Override
    public String appendToAchievementsFile(String data) {
        return appendToAchievements(data);
    }
}