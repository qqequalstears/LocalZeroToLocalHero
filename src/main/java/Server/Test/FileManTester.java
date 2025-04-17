package Server.Test;

import Server.Model.AppendToFile;
import Server.Model.IDataFetcher;
import Server.Model.IDataSaver;
import Server.Model.IDataWriter;
import Server.Model.ISavableObject;
import Server.Model.ReadFromFile;
import Server.Model.WriteToFile;


/**
 * File just for local testing
 * @Jansson
 */

public class FileManTester {

    public static void main(String[] args) {
        System.out.println("Starting test \n----------------NEW TEST-----------------");

        test1();

        test2();

        test3();
    }

    private static String test1() {
        IDataWriter writer = WriteToFile.getInstance();
        IDataSaver appender = AppendToFile.getInstance();
        IDataFetcher fetcher = ReadFromFile.getInstance();

        writer.saveUser("id1,Kalle,KalleK,20,500,6");
        writer.saveAchievements("test");
        writer.saveLog("1,test");
        writer.saveActiveIntiative("1,test");

        appender.saveUser("id2,fred,haj,20,500,6");
        appender.saveUser("id3,maj,dog,20,500,6");
        appender.saveAchievements("id2,fred,haj,20,500,6");
        appender.saveAchievements("id3,maj,dog,20,500,6");
        appender.saveLog("id2,fred,haj,20,500,6");
        appender.saveLog("id3,maj,dog,20,500,6");
        appender.saveActiveIntiative("id2,fred,haj,20,500,6");
        appender.saveActiveIntiative("id3,maj,dog,20,500,6");

        System.out.println(fetcher.fetchAllUserData());
        System.out.println(fetcher.fetchAllAchievementsData());
        System.out.println(fetcher.fetchAllLogData());
        System.out.println(fetcher.fetchAllActiveIntiativeData());

        return "haj hopp";
    }

    private static void test2() {
        System.out.println("\n----------------NEW TEST-----------------");
        IDataWriter writer = WriteToFile.getInstance();
        IDataSaver appender = AppendToFile.getInstance();
        IDataFetcher fetcher = ReadFromFile.getInstance();

        writer.saveUser("");
        System.out.println(fetcher.fetchAllUserData());
    }

    private static void test3() {
        System.out.println("\n----------------NEW TEST-----------------");
        IDataSaver appender = AppendToFile.getInstance();
        IDataFetcher fetcher = ReadFromFile.getInstance();
        ISavableObject logEnteryObject = new ISavableObject() {
            @Override
            public String getSavableString() {
                return test1();
            }
        };

        String logEntery = logEnteryObject.getSavableString();

        appender.saveLog(logEntery);
        System.out.println(fetcher.fetchAllLogData());
    }
}
