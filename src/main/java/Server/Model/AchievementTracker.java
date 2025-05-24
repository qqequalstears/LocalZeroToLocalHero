package Server.Model;

import Client.Model.Achievement;
import Client.Model.Initiative.Children.CarPool;
import Client.Model.Initiative.Children.GarageSale;
import Client.Model.Initiative.Children.Gardening;
import Client.Model.Initiative.Children.ToolSharing;
import Client.Model.Initiative.Parent.Initiative;

import Server.Controller.FileHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton class responsible for tracking and improving achievements.
 * <p>
 * Each type of initiative is associated with a specific improvement score,
 * which contributes to the progress of a user's achievements.
 *
 * @author Jansson Anton
 * @Date 2025-05-03
 */

public class AchievementTracker {
    private static AchievementTracker instance;
    private static final Map<Class<? extends Initiative>, Integer> initiativeMap = new HashMap<>();

    // Static block initializing the improvement scores for each initiative type.
    static { //TODO this static block + map (map above) might need to be moved to a more fitting file / integrated in another way @Jansson 2025-05-03
        initiativeMap.put(CarPool.class, 1);
        initiativeMap.put(GarageSale.class, 2);
        initiativeMap.put(Gardening.class, 3);
        initiativeMap.put(ToolSharing.class, 4);
    }

    /**
     * Improves the progress of all achievements in the provided list based on the
     * given initiative's improvement score.
     *
     * @param achievements the list of achievements to update
     * @param initiative   the initiative that was completed
     * @return a summary string indicating the amount of progress added to each achievement
     * @author Jansson Anton
     * @Date 2025-05-03
     */
    public String improveAchievements(List<Achievement> achievements, Initiative initiative) {
        for (Achievement achievement : achievements) {
            improveAchievement(achievement, determineAchievementImprovementScore(initiative));
        }
        return "All achievements had progress improved by: " + determineAchievementImprovementScore(initiative);
    }

    public String improveAchievementCSV(Initiative initiative) {
        int improvementScore = determineAchievementImprovementScore(initiative);

        String currentAchievements = FileHandler.getInstance().fetchAllAchievementsData();
        String[] splitLines = currentAchievements.split("\n");
        List<String> allAchievementLines = new ArrayList<>();


        boolean found = false;

        for (int i = 1; i < splitLines.length; i++) {
            String line = splitLines[i];
            String[] columnParts = line.split(",");

            if (columnParts[1].trim().equalsIgnoreCase(initiative.getCategory().trim()) && columnParts[4].trim().equalsIgnoreCase(initiative.getLocation())) {
                int currentProgress = Integer.parseInt(columnParts[3].trim());
                currentProgress += improvementScore;
                columnParts[3] = String.valueOf(currentProgress);
                found = true;

                line = String.join(",", columnParts);
            }
            allAchievementLines.add(line);
        }
        if (!found) {
            String newID = splitLines.length + 1 + "";
            String newDescription = "ZeroToHero";
            String newLocation = initiative.getLocation();
            String newLine = String.join(",", newID, initiative.getCategory(), newDescription, String.valueOf(improvementScore), newLocation);
            allAchievementLines.add(newLine);
        }
        FileHandler.getInstance().writeAchievementsToFile(String.join("\n", allAchievementLines));
        return "Achievement " + initiative.getCategory() + " had progress improved by: " + improvementScore;
    }

    /**
     * Updates an individual achievement's progress by the specified improvement rate.
     *
     * @param achievement     the achievement to update
     * @param improvementRate the amount by which to increase progress
     * @return a string describing the progress update
     * @author Jansson Anton
     * @Date 2025-05-03
     */
    private String improveAchievement(Achievement achievement, int improvementRate) {
        achievement.setProgress(achievement.getProgress() + improvementRate);
        return "Achievement" + achievement.getName() + "had progress improved by: " + improvementRate;
    }


    /**
     * Determines the improvement score associated with a specific initiative.
     *
     * @param initiative the initiative whose score is to be looked up
     * @return the improvement score; returns 0 if the initiative type is not recognized
     * @author Jansson Anton
     * @Date 2025-05-03
     */
    private int determineAchievementImprovementScore(Initiative initiative) {
        return initiativeMap.getOrDefault(initiative.getClass(), 0);
    }

    /**
     * Returns the singleton instance of the AchievementTracker.
     * <p>
     * If the instance does not already exist, it will be created.
     *
     * @return the singleton instance
     * @author Jansson Anton
     * @Date 2025-05-03
     */
    public static synchronized AchievementTracker getInstance() {
        if (instance == null) {
            instance = new AchievementTracker();
        }
        return instance;
    }

    public JSONArray getAchievementsForLocation(String location) {
        String achievementsData = FileHandler.getInstance().fetchAllAchievementsData();
        String[] lines = achievementsData.split("\n");

        org.json.JSONArray achievementArray = new org.json.JSONArray();
        for (int i = 1; i < lines.length; i++) {
            String[] columns = lines[i].split(",");
            if (columns[4].trim().equalsIgnoreCase(location.trim())) {
                JSONObject achievement = new JSONObject();
                achievement.put("id", columns[0].trim());
                achievement.put("category", columns[1].trim());
                achievement.put("description", columns[2].trim());
                achievement.put("progress", columns[3].trim());
                achievementArray.put(achievement);
            }
        }
        return achievementArray;
    }
}
