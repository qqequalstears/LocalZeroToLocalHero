package Server.Model.FileMan.Proxy;

import Server.Model.FileMan.AppendToFile;
import Server.Model.FileMan.IDataAppender;

public class FileAppenderProxy implements IDataAppender {
    private static FileAppenderProxy instance;

    private FileAppenderProxy() {
    }

    public static synchronized FileAppenderProxy getInstance() {
        if (instance == null) {
            instance = new FileAppenderProxy();
        }
        return instance;
    }

    @Override
    public String appendToUsersFile(String data) {
        return AppendToFile.getInstance().appendToUsersFile(data);
    }

    @Override
    public String appendToAchievementsFile(String data) {
        return AppendToFile.getInstance().appendToAchievementsFile(data);
    }

    @Override
    public String appendLogEnteryToFile(String logEntry) {
        return AppendToFile.getInstance().appendLogEnteryToFile(logEntry);
    }

    @Override
    public String appendActiveIntiativeToFile(String data) {
        return AppendToFile.getInstance().appendActiveIntiativeToFile(data);
    }
}
