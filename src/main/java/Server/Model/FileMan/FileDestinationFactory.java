package Server.Model.FileMan;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A factory class for managing file destinations within the application.
 * This class provides methods to retrieve file paths for various data files
 * used in the application, such as user data, achievements, logs, and active
 * initiatives.
 *
 * @author Jansson Anton
 * @Date 2025-04-17
 */
public class FileDestinationFactory {
    private static final String BASE_DIRECTORY = "src/main/java/Server/fileStorage";
    private final static String USERS_DATA_FILENAME = "users.csv";
    private final static String ACHIEVEMENT_DATA_FILENAME = "achievements.csv";
    private final static String LOG_DATA_FILENAME = "log.csv";
    private final static String ACTIVE_INITIATIVE_DATA_FILENAME = "activeIntiative.csv";

    private static FileDestinationFactory instance;

    private FileDestinationFactory() {    }

    /**
     * Retrieves the singleton instance of the FileDestinationFactory.
     * Ensures that only one instance of the factory is created.
     *
     * @return The singleton instance of FileDestinationFactory.
     * @author Jansson Anton
     * @Date 2025-04-17
     */
    public static FileDestinationFactory getInstance() {
        if (instance == null) {
            synchronized (FileDestinationFactory.class) {
                    instance = new FileDestinationFactory();
                }
        }
        return instance;
    }

    /**
     * Constructs the file path for a given file name within the base directory.
     *
     * @param fileName The name of the file for which the path is to be constructed.
     * @return The full path to the specified file as a Path object.
     * @author Jansson Anton
     * @Date 2025-04-17
     */
    private static Path getFilePath(String fileName) {
        Path projectPath = Paths.get("").toAbsolutePath(); // Get the absolute path of the project directory
        Path baseDirectory = projectPath.resolve(BASE_DIRECTORY);
        return baseDirectory.resolve(fileName);
    }

    /**
     * Retrieves the user data file.
     *
     * @return A File object representing the user data file (users.csv) path.
     * @author Jansson Anton
     * @Date 2025-04-17
     */
    public static File getUserDataFile() {
        return new File(getFilePath(USERS_DATA_FILENAME).toString());
    }

    /**
     * Retrieves the achievement data file.
     *
     * @return A File object representing the achievement data file
     * (achievements.csv) path.
     * @author Jansson Anton
     * @Date 2025-04-17
     */
    public static File getAchievementDataFile() {
        return new File(getFilePath(ACHIEVEMENT_DATA_FILENAME).toString());
    }

    /**
     * Retrieves the log data file.
     *
     * @return A File object representing the log data file (log.csv) path.
     * @author Jansson Anton
     * @Date 2025-04-17
     */
    public static File getLogDataFile() {
        return new File(getFilePath(LOG_DATA_FILENAME).toString());
    }

    /**
     * Retrieves the active initiative data file.
     *
     * @return A File object representing the active initiative data file
     * (activeInitiative.csv) path.
     * @author Jansson Anton
     * @Date 2025-04-17
     */
    public static File getActiveInitiativeDataFile() {
        return new File(getFilePath(ACTIVE_INITIATIVE_DATA_FILENAME).toString());
    }
}