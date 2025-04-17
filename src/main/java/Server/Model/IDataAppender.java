package Server.Model;

/**
 * The {@code IDataAppender} interface extends the {@code IDataSaver} interface
 * and provides methods for appending various types of data to existing files.
 * This interface is designed to handle operations related to appending user
 * data,
 * achievements data, log entries, and active initiative data to their
 * respective files.
 * 
 * <p>
 * The default methods in this interface provide implementations for saving data
 * by delegating the operation to the corresponding append methods.
 * 
 * <p>
 * Note: The naming of methods in this interface is subject to potential
 * refactoring
 * for better clarity and alignment with scenarios outside of file writing.
 * 
 * @author Jansson Anton
 * @Date 2025-04-16
 */
public interface IDataAppender extends IDataSaver {

    // TODO might need to refactor the naming of some methods. The current naming is
    // more a placeholder/first draft of the potential names for the methods.
    // 2025-04-14 @Jansson

    // TODO instead of "write" and "append", "save" might be better naming --> more
    // clear for scenarios outside of file writing 2025-04-15 @Jansson

    /**
     * Appends the given user data to an existing file.
     *
     * @param data The user data to be appended to the file.
     * @return A message indicating whether the user data was appended successfully
     *         or if there was an error.
     * @author Jansson Anton
     * @Date 2025-04-14
     */
    String appendToUsersFile(String data);

    /**
     * Appends the given achievements data to an existing file.
     *
     * @param data The achievements data to be appended to the file.
     * @return A message indicating whether the achievements data was appended
     *         successfully or if there was an error.
     * @author Jansson Anton
     * @Date 2025-04-14
     */
    String appendToAchievementsFile(String data);

    /**
     * Appends the given log entry to an existing file.
     *
     * @param logEntry The log entry to be appended to the file.
     * @return A message indicating whether the log entry was appended successfully
     *         or if there was an error.
     * @author Jansson Anton
     * @Date 2025-04-16
     */
    String appendLogEnteryToFile(String logEntry);

    /**
     * Appends the given active initiative data to an existing file.
     *
     * @param data The active initiative data to be appended to the file.
     * @return A message indicating whether the active initiative data was appended
     *         successfully or if there was an error.
     * @author Jansson Anton
     * @Date 2025-04-16
     */
    String appendActiveIntiativeToFile(String data);

    /**
     * Saves the given user data by appending it to the users file.
     *
     * @param data The user data to be saved.
     * @return A message indicating whether the user data was saved successfully
     *         or if there was an error.
     * @author Jansson Anton
     * @Date 2025-04-16
     */
    @Override
    default String saveUser(String data) {
        return appendToUsersFile(data);
    }

    /**
     * Saves the given achievements data by appending it to the achievements file.
     *
     * @param data The achievements data to be saved.
     * @return A message indicating whether the achievements data was saved
     *         successfully or if there was an error.
     * @author Jansson Anton
     * @Date 2025-04-16
     */
    @Override
    default String saveAchievements(String data) {
        return appendToAchievementsFile(data);
    }

    /**
     * Saves the given log entry by appending it to the log file.
     *
     * @param logEntry The log entry to be saved.
     * @return A message indicating whether the log entry was saved successfully
     *         or if there was an error.
     * @author Jansson Anton
     * @Date 2025-04-16
     */
    @Override
    default String saveLog(String logEntry) {
        return appendLogEnteryToFile(logEntry);
    }

    /**
     * Saves the given active initiative data by appending it to the active
     * initiatives file.
     *
     * @param data The active initiative data to be saved.
     * @return A message indicating whether the active initiative data was saved
     *         successfully or if there was an error.
     * @author Jansson Anton
     * @Date 2025-04-16
     */
    @Override
    default String saveActiveIntiative(String data) {
        return appendActiveIntiativeToFile(data);
    }

}
