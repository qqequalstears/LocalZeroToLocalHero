package Server.Testing;

import Client.Model.Achievement;
import Client.Model.Initiative.Children.CarPool;
import Client.Model.Initiative.Children.GarageSale;
import Client.Model.Initiative.Children.Gardening;
import Client.Model.Initiative.Children.ToolSharing;
import Client.Model.Initiative.Parent.Initiative;

import java.util.ArrayList;
import java.util.List;
/**
 * The {@code AchievementTrackerTesting} class provides simple test cases for validating the functionality
 * of achievement progress updates through various initiative types.
 * <p>
 * It simulates user interactions with initiatives (e.g., CarPool, Gardening, etc.) and verifies
 * if the corresponding achievements are updated correctly when {@code improveAchievements()} is called.
 * <p>
 *     This is a basic unit-style test class using `main()` for manual validation and console output.
 * </p>
 * Please note that this clas is only used for manual testing of functionality for the {@code AchievementTrackerTesting}
 * class and related functionality
 *
 * @author     Jansson Anton
 * @Date 2025-05-03
 */
public class AchievementTrackerTesting {

    public static void main(String[] args) {
        System.out.println("Testing started...");
        System.out.println("Test1:" + test1());
        System.out.println("Test2:" + test2());
        System.out.println("Testing completed.");
    }

    private static List<Achievement> helpSetup(String name) {
        List<Achievement> achievementsToAdd = new ArrayList<>();
        achievementsToAdd.add(new Achievement(name + "1"));
        achievementsToAdd.add(new Achievement(name + "2"));
        achievementsToAdd.add(new Achievement(name + "3"));
        achievementsToAdd.add(new Achievement(name + "4"));
        return achievementsToAdd;
    }


    private static String test1() {
//        CarPool cp = new CarPool("Title1", "a desc", "haj chaparral", "4?", "00", "01", "6", "Carpool", true);


      /*  for (Achievement achievement : cp.getAchievements()) {
            System.out.println(achievement.getProgress());
        }

        cp.improveAchievements();

        System.out.println("----Pre improve^^----------Post improve vv--------------");

        for (Achievement achievement : cp.getAchievements()) {
            System.out.println(achievement.getProgress());
        }*/

        return "Done";

    }


    private static String test2() {
        /*CarPool cp = new CarPool("Title1", "a desc", "haj chaparral", "4?", "00", new ArrayList<>(), new ArrayList<>(), false, helpSetup("cp"));
        Gardening g = new Gardening("Title1", "a desc", "haj chaparral", "4?", "00", new ArrayList<>(), new ArrayList<>(), false, helpSetup("g"));
        ToolSharing ts = new ToolSharing("Title1", "a desc", "haj chaparral", "4?", "00", new ArrayList<>(), new ArrayList<>(), false, helpSetup("ts"));
        GarageSale gs = new GarageSale("Title1", "a desc", "haj chaparral", "4?", "00", new ArrayList<>(), new ArrayList<>(), false, helpSetup("gs"));
*/
        ArrayList<Initiative> list = new ArrayList<>();
  /*      list.add(cp);
        list.add(gs);
        list.add(g);
        list.add(ts);
*/
        for (Initiative initiative : list) {
            for (Achievement achievement : initiative.getAchievements()) {
                System.out.println(achievement.getName() + " imp:" + achievement.getProgress());
            }
            System.out.println(".................................................");
        }
        loop(list);
        printAfter(list);
        loop(list);
        printAfter(list);
        return "Done";

    }

    private static void loop(ArrayList<Initiative> list) {
        System.out.println("----Pre improve^^----------Post improve vv--------------");
        for (Initiative initiative : list) {
            initiative.improveAchievements();
        }
    }

    private static void printAfter(ArrayList<Initiative> list) {
        for (Initiative initiative : list) {
            for (Achievement achievement : initiative.getAchievements()) {
                System.out.println(achievement.getName() + " imp:" + achievement.getProgress());
            }
        }
    }
}
