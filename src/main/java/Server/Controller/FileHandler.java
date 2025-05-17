package Server.Controller;

import Client.Model.User;
import Server.Model.FileMan.*;
import Server.Model.FileMan.Proxy.FileAppenderProxy;
import Server.Model.FileMan.Proxy.FileReaderProxy;
import Server.Model.FileMan.Proxy.FileWriterProxy;

import java.util.List;

//TODO denna klassen är inte klar. Syftet är att den ska hantera filhantering för att 
//spara data samt läsa/hämta data (med hjälp av IDataSaver och IDataFetcher).
//Den är bara en mock för att visa hur den kan se ut. 
//Klassen beöver fler metoder med rimlig logik och mer funktionalitet.
//  * @date 2025-04-24 @Jansson Anton
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
        //  return ReaderFiles.getInstance().fetchAllUsers();
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
}
