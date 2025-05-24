package Server.Controller;

import Client.Model.Initiative.Parent.Initiative;
import Client.Model.User;
import Server.Model.FileMan.*;
import Server.Model.FileMan.Proxy.FileAppenderProxy;
import Server.Model.FileMan.Proxy.FileReaderProxy;
import Server.Model.FileMan.Proxy.FileWriterProxy;

import java.util.List;


public class FileHandler {
    private static FileHandler instance;

    private FileHandler() {
        // Private constructor to prevent instantiation
    }

    public static synchronized FileHandler getInstance() {
        synchronized (FileHandler.class) {
            if (instance == null) {
                instance = new FileHandler();
            }
        }
        return instance;
    }

    // Mock method for this class. This is not a real method, but it is used to
    // demonstrate how one might save data to a file.
    public String saveAllUsers(String dString) {
        IDataSaver dataSaver = FileWriterProxy.getInstance();
        dataSaver.saveUser(dString);
        return "Data saved successfully";
    }

    public List<User> getUsers() {
        //return ReaderFiles.getInstance().fetchAllUsers();
        //return FileReaderProxy.getInstance().fetchAllUsers();
        IDataFetcher dataFetcher = FileReaderProxy.getInstance();
        return dataFetcher.fetchAllUsers();
    }

    public String registerUser(String csvContent) {
        IDataSaver dataAppender = FileAppenderProxy.getInstance();
        //test.appendToUsersFile(csvContent);
        dataAppender.saveUser(csvContent);
        return "Data saved successfully";

    }

    public String createInitiative(String csvContent) {
        IDataSaver dataAppender = FileAppenderProxy.getInstance();
        dataAppender.saveActiveIntiative(csvContent);
        return "Data saved successfully";

    }

    public String replaceRoles(String mail, List<String> newRoles) {
        String csvContent = updateUsersNewRoles(mail, newRoles);
        //WriteToFile.getInstance().replaceUsers(csvContent);
        //  FileWriterProxy.getInstance().writeUsersToFile(csvContent);
        IDataSaver dataSaver = FileWriterProxy.getInstance();
        dataSaver.saveUser(csvContent);
        return "Roles replaced successfully";
    }


    /**
     * @param mail
     * @param newRoles
     * @return
     * @author Anton Persson
     */
    public String updateUsersNewRoles(String mail, List<String> newRoles) {
        IDataFetcher dataFetcher = FileReaderProxy.getInstance();
        String csvContent = dataFetcher.fetchAllUserData();
        String[] lines = csvContent.split("\n");

        StringBuilder updatedCSV = new StringBuilder();
        //  updatedCSV.append(lines[0]).append("\n");

        for (int i = 1; i < lines.length; i++) {
            String[] contents = lines[i].split(",");
            if (contents.length < 5) {
                continue;
            }
            String email = contents[0].trim();
            if (email.equals(mail)) {
                String joinedRoles = String.join(" - ", newRoles);
                StringBuilder updatedLine = new StringBuilder();
                for (int j = 0; j < 4; j++) {
                    updatedLine.append(contents[j]).append(",");
                }
                updatedLine.append(joinedRoles);
                updatedCSV.append(updatedLine).append("\n");
            } else {
                updatedCSV.append(lines[i]).append("\n");
            }
        }
        return updatedCSV.toString();
    }

    public void updateAchievements(String csvContent) { //TODO oanvänd metod?
        AppendToFile.getInstance().appendToAchievementsFile(csvContent);
    }

    public List<Initiative> getAllActiveInitiatives() {

        return ReaderFiles.getInstance().fetchAllActiveInitiatives();
    }

    public String fetchAllLogData() {
        IDataFetcher dataFetcher = FileReaderProxy.getInstance();
        return dataFetcher.fetchAllLogData();
    }

    public void appendLogEntryToFile(String logEntryString) {
        IDataSaver dataSaver = FileAppenderProxy.getInstance();
        dataSaver.saveLog(logEntryString);
    }

    public String fetchOneUserLocationData(String email) {
        IDataFetcher dataFetcher = FileReaderProxy.getInstance();
        return dataFetcher.fetchOneUserLocationData(email);
    }

    public String fetchAllAchievementsData() {
        IDataFetcher dataFetcher = FileReaderProxy.getInstance();
        return dataFetcher.fetchAllAchievementsData();
    }

    public String writeAchievementsToFile(String data) {
        IDataSaver dataSaver = FileWriterProxy.getInstance();
        return dataSaver.saveAchievements(data);
    }
}
