package Server.Model.FileMan;

/**
 * The IDataSaver interface provides methods for saving various types of data,
 * such as user information, achievements, logs, and active initiatives.
 * Implementations of this interface should define how the data is saved.
 * 
 * @author Jansson Anton
 * @Date 2025-04-16
 */
public interface IDataSaver {

    /**
     * Saves user-related data.
     *
     * @param data The user data to be saved as a {@code String}.
     * @return A string indicating the result of the save operation, such as a
     *         success message or an error description.
     * @author Jansson Anton
     * @Date 2025-04-16
     */
    String saveUser(String data);

    /**
     * Saves achievement-related data.
     *
     * @param data The achievement data to be saved as a {@code String}.
     * @return A string indicating the result of the save operation, such as a
     *         success message or an error description.
     * @author Jansson Anton
     * @Date 2025-04-16
     */
    String saveAchievements(String data);

    /**
     * Saves a log entry.
     *
     * @param logEntry The log entry to be saved as a {@code String}.
     * @return A string indicating the result of the save operation, such as a
     *         success message or an error description.
     * @author Jansson Anton
     * @Date 2025-04-16
     */
    String saveLog(String logEntry);

    /**
     * Saves data related to an active initiative.
     *
     * @param data The active initiative data to be saved as a {@code String}.
     * @return A string indicating the result of the save operation, such as a
     *         success message or an error description.
     * @author Jansson Anton
     * @Date 2025-04-16
     */
    String saveActiveIntiative(String data);
}
