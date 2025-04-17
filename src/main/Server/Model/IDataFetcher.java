package Server.Model;
/**
 * The {@code IDataFetcher} interface defines methods for retrieving user and achievement data from a data source.
 * It provides functionality to fetch all user data, all achievement data, and specific user achievement data.
 * <p>
 * This interface is designed to be implemented by classes that handle data fetching operations, such as reading
 * from files or databases. It abstracts the details of data retrieval, allowing for flexibility in the underlying
 * implementation.
 * @author Jansson Anton
 * @Date 2025-04-14
 */
public interface IDataFetcher {

    /**
     * Fetches all user data from the data source.
     *
     * @return A string containing all user data.
     */
    String fetchAllUserData();

    /**
     * Fetches all achievement data from the data source.
     *
     * @return A string containing all achievement data.
     * @author Jansson Anton
     * @Date 2025-04-14
     */
    String fetchAchievementsData();

    /**
     * Fetches all user achievement data from the data source.
     *
     * @return A string containing all user achievement data.
     * @author Jansson Anton
    * @Date 2025-04-14
     */
    String fetchUserAchievementsData();

    /**
     * Fetches achievement data for a specific user from the data source.
     *
     * @param userName The name of the user whose achievement data is to be fetched.
     * @return A string containing the achievement data for the specified user.
     * @author Jansson Anton
     * @Date 2025-04-14
     */
    String fetchUserAchievementsData(String userName);
}
