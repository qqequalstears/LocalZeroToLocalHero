package Server.Model.FileMan;

/**
 * The {@code IDataWriter} interface defines methods for writing and appending
 * data to files.
 * It provides functionality for handling user data and achievements data,
 * allowing for both
 * writing new data and appending to existing files.
 * <p>
 * Potential future improvements include splitting this interface into two
 * separate interfaces:
 * one for writing data and another for appending data. Additionally, methods
 * for handling objects
 * (e.g., {@code User}) or collections of objects (e.g., {@code List<User>}) may
 * be added.
 *
 * @author Jansson Anton
 * @Date 2025-04-14
 */

public interface IDataWriter extends IDataSaver {

    // TODO might need to refactor the naming of some methods. The current naming is
    // more a placeholder/first draft of the potential names for the methods.
    // 2025-04-14 @Jansson

    // TODO instead of "write" and "append", "save" might be better naming --> more
    // cleare for scenaros outside of file writing 2025-04-15 @Jansson

    /**
     * Writes the given String of data to a file.
     *
     * @param data The user data to be written to the file.
     * @return A message indicating whether the user data was written successfully
     * or if there was an error.
     * @author Jansson Anton
     * @Date 2025-04-14
     */
    String writeUsersToFile(String data);

    /**
     * Writes the given String of data to a file.
     *
     * @param data The achievements data to be written to the file.
     * @return A message indicating whether the achievements data was written
     * successfully or if there was an error.
     * @author Jansson Anton
     * @Date 2025-04-14
     */
    String writeAchievementsToFile(String data);

    /**
     * Writes the given log entry to a file.
     *
     * @param logEntry The log entry to be written to the file.
     * @return A message indicating whether the log entry was written successfully
     * or if there was an error.
     * @author Jansson Anton
     * @Date 2025-04-16
     */
    String writeLogEntryToFile(String logEntry);

    /**
     * Writes the given active initiative data to a file.
     *
     * @param data The active initiative data to be written to the file.
     * @return A message indicating whether the active initiative data was written
     * successfully or if there was an error.
     * @author Jansson Anton
     * @Date 2025-04-16
     */
    String writeActiveInitiativeToFile(String data);

    /**
     * Saves user data by writing it to a file.
     *
     * @param data The user data to be saved.
     * @return A message indicating whether the user data was saved successfully
     * or if there was an error.
     * @author Jansson Anton
     * @Date 2025-04-16
     */
    @Override
    default String saveUser(String data) {
        return writeUsersToFile(data);
    }

    /**
     * Saves achievements data by writing it to a file.
     *
     * @param data The achievements data to be saved.
     * @return A message indicating whether the achievements data was saved
     * successfully or if there was an error.
     * @author Jansson Anton
     * @Date 2025-04-16
     */
    @Override
    default String saveAchievements(String data) {
        return writeAchievementsToFile(data);
    }

    /**
     * Saves a log entry by writing it to a file.
     *
     * @param logEntry The log entry to be saved.
     * @return A message indicating whether the log entry was saved successfully
     * or if there was an error.
     * @author Jansson Anton
     * @Date 2025-04-16
     */
    @Override
    default String saveLog(String logEntry) {
        return writeLogEntryToFile(logEntry);
    }

    /**
     * Saves active initiative data by writing it to a file.
     *
     * @param data The active initiative data to be saved.
     * @return A message indicating whether the active initiative data was saved
     * successfully or if there was an error.
     * @author Jansson Anton
     * @Date 2025-04-16
     */
    @Override
    default String saveActiveIntiative(String data) {
        return writeActiveInitiativeToFile(data);
    }
}