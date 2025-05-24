package Server.Testing;

import java.io.File;

import Server.Model.FileMan.AppendToFile;
import Server.Model.FileMan.FileDestinationFactory;
import Server.Model.FileMan.IDataFetcher;
import Server.Model.FileMan.IDataWriter;
import Server.Model.FileMan.ReaderFiles;
import Server.Model.FileMan.WriteToFile;

/**
 * The FileManTesting class is responsible for testing the functionality of the
 * FileMan package.
 * It includes methods to test the FileDestinationFactory, DataWriter, DataAppender,
 * and DataReader classes.
 * <p>
 * This class serves as a testing utility to ensure that the file management
 * components are working correctly.
 * <p>
 * This is a class primarity used for manual testing and debugging purposes. 
 * It is not intended for production use and should be removed or disabled in the future.
 * <p>
 * @author Jansson Anton
 * @Date 2025-04-17
 */
public class FileManTesting {

    public static void main(String[] args) {
        System.out.println("Testing started...");
        System.out.println("Testing FileDestinationFactory...");
        testFileDestinationFactory();
        System.out.println("Testing FileDestinationFactory completed.");
        System.out.println("Testing DataWriter and DataAppender...");
        testDataWriteNAppend();
        System.out.println("Testing DataWriter and DataAppender completed.");
        System.out.println("Testing DataReader...");
        testDataRead();
        System.out.println("Testing DataReader completed.");

        System.out.println("Testing completed.");

    }

    private static void testDataWriteNAppend() {
        IDataWriter dataWriter = WriteToFile.getInstance();
        AppendToFile dataAppender = AppendToFile.getInstance();

        String timeStamp=System.currentTimeMillis()+"";

        dataWriter.writeUsersToFile("Test, User, Data, "+timeStamp);
        dataWriter.writeAchievementsToFile("Test, Achievement, Data, "+timeStamp);
        dataWriter.writeLogEntryToFile("Test, Log, Data, "+timeStamp);
        dataWriter.writeActiveInitiativeToFile("Test, Active, Intiative, Data, "+timeStamp);

        dataAppender.appendToUsersFile("Test, User, Data, Append, "+timeStamp);
        dataAppender.appendToUsersFile("Test, User, Data, Append, "+timeStamp);
        dataAppender.appendToAchievementsFile("Test, Achievement, Data, Append, "+timeStamp);
        dataAppender.appendToAchievementsFile("Test, Achievement, Data, Append, "+timeStamp);
        dataAppender.appendLogEnteryToFile("Test, Log, Data, Append, "+timeStamp);
        dataAppender.appendLogEnteryToFile("Test, Log, Data, Append, "+timeStamp);
        dataAppender.appendActiveIntiativeToFile("Test, Active, Intiative, Data, Append, "+timeStamp);
        dataAppender.appendActiveIntiativeToFile("Test, Active, Intiative, Data, Append, "+timeStamp);
    }

    private static void testDataRead() {
        IDataFetcher dataReader = ReaderFiles.getInstance();

        System.out.println(dataReader.fetchAllUserData());
        System.out.println(dataReader.fetchAllAchievementsData());
        System.out.println((dataReader.fetchAllLogData()));
        System.out.println(dataReader.fetchAllActiveIntiativeData());
    }

    private static void testFileDestinationFactory() {
        FileDestinationFactory factory = FileDestinationFactory.getInstance();
        File userDataFile = factory.getUserDataFile();

        System.out.println("User Data File Path: " + userDataFile.getAbsolutePath());
        System.out.println("User Data File Exists: " + userDataFile.exists());

    }

}
