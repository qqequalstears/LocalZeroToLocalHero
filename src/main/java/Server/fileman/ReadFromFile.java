package Server.fileman;

import java.io.*;

import Server.Model.IDataFetcher;


/**
 * The ReadFromFile class provides methods for reading data from a file.
 * It follows the Singleton design pattern, ensuring only one instance of the class exists at any time.
 * The class offers functionality to read the entire content of a file as a string.
 * It handles common file I/O operations, such as file reading, error handling, and empty file handling.
 * <p>
 * The class includes methods for:
 * - Reading the entire content of a file.
 * - Handling cases where the file does not exist or when an error occurs during reading.
 *
 * @author Jansson Anton
 * @date 2025-04-07
 */

public class ReadFromFile implements IDataFetcher{

    private static ReadFromFile instance;

    private static final String destinationUser = "src\\main\\java\\server\\fileman\\fileStorage\\users.txt";  //TODO the path will prob. change. This is from @Janssons local testing 
    private static final String destinationAchievements = "src\\main\\java\\server\\fileman\\fileStorage\\achievements.txt";  //TODO the path will prob. change. This is from @Janssons local testing

    /**
     * Provides a singleton instance of the ReadFromFile class.
     * This method ensures that only one instance of ReadFromFile exists at any given time.
     * If an instance does not already exist, it creates a new one; otherwise, it returns the existing instance.
     *
     * @return The singleton instance of the ReadFromFile class.
     * @author Jansson Anton
     * @Date 2025-04-07
     */
    public static ReadFromFile getInstance() {
        if (instance == null) {
            instance = new ReadFromFile();
        }
        return instance;
    }


    /**
     * Reads the entire content of a file and returns it as a string with \n for every line in the file.
     * If the file is empty, it returns an empty string.
     * If the file does not exist or an error occurs during reading, an error message is returned.
     *
     * @param destination The path to the file to be read. This can be an absolute or relative file path.
     * @return A string containing the entire content of the file. If an error occurs, an error message is returned.
     * Returns "File not found at: <destination>" if the file is not found,
     * or "Error reading the file: <message>" if an error occurs during reading.
     * @throws FileNotFoundException If the file does not exist at the specified destination.
     * @throws IOException           If an I/O error occurs while reading the file.
     * @author Jansson Anton
     * @Date 2025-04-07
     */
    public String readWholeFile(String destination) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(destination));
            StringBuilder returnString = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                returnString.append(line+"\n");
            }
            br.close();
            return returnString.toString();

        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe);
            return "File not found at: " + destination;
        } catch (IOException e) {
            return "Error reading the file: " + e.getMessage();
        }
    }

    /**
     * Reads the content of the users file and returns it as a string.
     *
     * @return A string containing the entire content of the users file.
     * If an error occurs during reading, an error message is returned.
     * @author Jansson Anton
     * @Date 2025-04-07
     */
    public String readUsers() {
        return readWholeFile(destinationUser);
    }

    /**
     * Reads the content of the achievements file and returns it as a string.
     *
     * @return A string containing the entire content of the achievements file.
     * If an error occurs during reading, an error message is returned.
     * @author Jansson Anton
     * @Date 2025-04-07
     */
    public String readAchievements() {
        return readWholeFile(destinationAchievements);
    }


    @Override
    public String fetchAllUserData() {
        return readUsers();
    }


    @Override
    public String fetchAchievementsData() {
      return readAchievements();
    }


    @Override
    public String fetchUserAchievementsData() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fetchUserAchievementsData'");
    }


    @Override
    public String fetchUserAchievementsData(String userName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fetchUserAchievementsData'");
    }


}
