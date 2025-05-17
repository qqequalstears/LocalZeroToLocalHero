package Server.Model.FileMan.Proxy;

import Client.Model.User;
import Server.Model.FileMan.IDataFetcher;
import Server.Model.FileMan.ReaderFiles;

import java.util.List;

public class FileReaderProxy implements IDataFetcher {

    private static FileReaderProxy instance;

    private FileReaderProxy() {
    }

    public static synchronized FileReaderProxy getInstance() {
        if (instance == null) {
            instance = new FileReaderProxy();
        }
        return instance;
    }

    @Override
    public String fetchAllUserData() {
        return ReaderFiles.getInstance().fetchAllUserData();
    }

    @Override
    public List<User> fetchAllUsers() {
        return ReaderFiles.getInstance().fetchAllUsers();
    }

    @Override
    public String fetchAllAchievementsData() {
        return ReaderFiles.getInstance().fetchAllActiveIntiativeData();
    }

    @Override
    public String fetchAllUserAchievementsData() {
        return ReaderFiles.getInstance().fetchAllAchievementsData();
    }

    @Override
    public String fetchUserAchievementsData(String userName) {
        return ReaderFiles.getInstance().fetchUserAchievementsData(userName);
    }

    @Override
    public String fetchAllLogData() {
        return ReaderFiles.getInstance().fetchAllLogData();
    }

    @Override
    public String fetchAllActiveIntiativeData() {
        return ReaderFiles.getInstance().fetchAllActiveIntiativeData();
    }
}
