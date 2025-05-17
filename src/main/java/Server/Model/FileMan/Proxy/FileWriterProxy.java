package Server.Model.FileMan.Proxy;

import Server.Model.FileMan.IDataWriter;
import Server.Model.FileMan.WriteToFile;

public class FileWriterProxy implements IDataWriter {

    private static FileWriterProxy instance;

    private FileWriterProxy() {
    }

    public static synchronized FileWriterProxy getInstance() {
        if (instance == null) {
            instance = new FileWriterProxy();
        }
        return instance;
    }

    @Override
    public String writeUsersToFile(String data) {
        return WriteToFile.getInstance().writeUsersToFile(data);
    }

    @Override
    public String writeAchievementsToFile(String data) {
        return WriteToFile.getInstance().writeAchievementsToFile(data);
    }

    @Override
    public String writeLogEntryToFile(String logEntry) {
        return WriteToFile.getInstance().writeLogEntryToFile(logEntry);
    }

    @Override
    public String writeActiveInitiativeToFile(String data) {
        return WriteToFile.getInstance().writeActiveInitiativeToFile(data);
    }
}
