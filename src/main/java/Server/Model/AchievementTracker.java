package Server.Model;

import Client.Model.Achievement;
import Client.Model.Initiative.Children.CarPool;
import Client.Model.Initiative.Children.GarageSale;
import Client.Model.Initiative.Children.Gardening;
import Client.Model.Initiative.Children.ToolSharing;
import Client.Model.Initiative.Parent.Initiative;

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
}
