package Server.Model;
/**
 * The {@code IDataWriter} interface defines methods for writing and appending data to files.
 * It provides functionality for handling user data and achievements data, allowing for both
 * writing new data and appending to existing files.
 * <p>
 * Potential future improvements include splitting this interface into two separate interfaces:
 * one for writing data and another for appending data. Additionally, methods for handling objects
 * (e.g., {@code User}) or collections of objects (e.g., {@code List<User>}) may be added.
 * 
 * @author Jansson Anton
 * @Date 2025-04-14
 */

public interface IDataWriter {

    //TODO might need to refactor the naming of some methods. The current naming is more a placeholder/first draft of the potential names for the methods. 2025-04-14 @Jansson 


    /**
     * Writes the given String of data to a file.
     *
     * @param data The user data to be written to the file.
     * @return A message indicating whether the user data was written successfully or if there was an error.
     * @author Jansson Anton
     * @Date 2025-04-14
     */
    String writeUsersToFile(String data);

    /**
     * Appends the given user data to an existing file.
     *
     * @param data The user data to be appended to the file.
     * @return A message indicating whether the user data was appended successfully or if there was an error.
     * @author Jansson Anton
     * @Date 2025-04-14
     */
    String appendToUsersFile(String data);

    /**
     * Writes the given String of data to a file.
     *
     * @param data The achievements data to be written to the file.
     * @return A message indicating whether the achievements data was written successfully or if there was an error.
     * @author Jansson Anton
     * @Date 2025-04-14
     */
    String writeAchievementsToFile(String data);

    /**
     * Appends the given achievements data to an existing file.
     *
     * @param data The achievements data to be appended to the file.
     * @return A message indicating whether the achievements data was appended successfully or if there was an error.
     * @author Jansson Anton
     * @Date 2025-04-14
     */
    String appendToAchievementsFile(String data);
}
