package Server.Controller;

import Server.Model.FileMan.IDataSaver;
import Server.Model.FileMan.WriteToFile;

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
        if (instance == null) {
            synchronized (FileHandler.class) {
                if (instance == null) {
                    instance = new FileHandler();
                }
            }
        }
        return instance;
    }

    // Mock method for this class. This is not a real method, but it is used to
    // demonstrate how one might save data to a file.
    public String saveAllUsersc(String dString) {
        IDataSaver dataSaver = new WriteToFile();
        dataSaver.saveUser(dString);
        return "Data saved successfully";
    }
}
