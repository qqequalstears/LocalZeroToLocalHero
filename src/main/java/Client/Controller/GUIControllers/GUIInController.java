package Client.Controller.GUIControllers;


import Client.Controller.GUIControllers.CreateInitiativeController.CreateInitiativeController;
import Client.Controller.GUIControllers.Home.HomeCentreController;
import Client.Controller.GUIControllers.Home.HomeLeftController;
import Client.Controller.GUIControllers.Home.HomeTopController;
import Client.Controller.GUIControllers.Intitiative.InitiativeController;
import Client.Controller.GUIControllers.LoginController.LoginController;
import Client.Controller.GUIControllers.Notifications.NotificationController;
import Client.Controller.GUIControllers.UserInfo.UserInfoController;
import Client.Model.Initiative.Parent.Initiative;
import Client.Controller.GUIControllers.Log.LogCentreController;
import Client.Model.Achievement;
import Client.Model.Initiative.Parent.Initiative;
import Client.Model.Notifications;
import Client.View.Achievement.AchievementStage;
import Client.View.CreateInitiative.CreateInitiativeStage;
import Client.View.Home.HomeStage;
import Client.View.Log.LogStage;
import Client.View.Login.LogInStage;
import Client.View.Notification.NotificationStage;
import Client.View.StageCreator;
import Client.View.UserInfo.UserInfoStage;
import Client.View.UserNotifier;
import Client.View.ViewInitiative.ViewInitiativeStage;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;

public class GUIInController {
    private Map<String, StageCreator> stageCreators;
    private static GUIInController instance;
    private String currentlySelectedInitiative = null;
    private List<Achievement> achievements;
    private List<String> logs;

    private GUIInController() {
        stageCreators = new ConcurrentHashMap<>();

        stageCreators.put("HOMESTAGE", () -> new HomeStage().createStage());
        stageCreators.put("LOGINSTAGE", () -> new LogInStage().createStage());
        stageCreators.put("INITIATIVESTAGE", () -> new InitiativeController());
        stageCreators.put("CREATEINITIATIVE", () -> new CreateInitiativeStage().createStage());
        stageCreators.put("NOTIFICATIONS", () -> new NotificationStage().createStage());
        stageCreators.put("ACHIEVEMENTSTAGE", () -> new AchievementStage().createStage());
        stageCreators.put("OPENINITIATIVESTAGE", () -> new ViewInitiativeStage().createStage());
        stageCreators.put("USERINFOSTAGE", () -> new UserInfoStage().createStage());
        stageCreators.put("LOGSTAGE", () -> new LogStage().createStage());
    }

    public void createStage(String stageToCreate) {
        StageCreator creator = stageCreators.get(stageToCreate);
        creator.createStage();
    }

    public void notifyUser(String message) {
        UserNotifier userNotifier = new UserNotifier();
        userNotifier.informUser(message);
    }

    public void successfulLogIn() {
        FxController loginController = GUIControllerRegistry.getInstance().get(LoginController.class.getName());
        Platform.runLater(() -> {
            loginController.closeStage();
            createStage("HOMESTAGE");
        });
    }

    public void showUserInfo(String userMail, String userName, String userLocation, List<String> userRoles, boolean isAdmin) {
        Platform.runLater(() -> {
            createStage("USERINFOSTAGE");
            UserInfoController userInfoController = (UserInfoController) GUIControllerRegistry.getInstance().get(UserInfoController.class.getName());
            if (!isAdmin) {
                userInfoController.disableAdminPrivileges();
            }
            StringBuilder rolesSB = new StringBuilder();
            for (String role : userRoles) {
                rolesSB.append(role + ", ");
            }
            String roles = rolesSB.toString();
            userInfoController.setInformation(userName, userLocation, userMail, roles);
        });
    }

    public static synchronized GUIInController getInstance() {
        if (instance == null) {
            instance = new GUIInController();
        }
        return instance;
    }

    public void newNotification() {
        if (GUIControllerRegistry.getInstance().contains(NotificationController.class.getName())) {
            NotificationController notificationController = (NotificationController) GUIControllerRegistry.getInstance().get(NotificationController.class.getName());
            Platform.runLater(notificationController::updateNotifcations);
        } else {
            HomeTopController homeTopController = (HomeTopController) GUIControllerRegistry.getInstance().get(HomeTopController.class.getName());
            if (homeTopController != null) {
                Platform.runLater(homeTopController::notifyUser);
            }
        }
    }

    public void achievement(List<Achievement> achievements) {
        this.achievements = achievements;
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Achievement/AchievementCentre.fxml"));
            createStage("ACHIEVEMENTSTAGE");
        });
    }

    public void logout() {
        FxController homeController = GUIControllerRegistry.getInstance().get(HomeTopController.class.getName());
        homeController.closeStage();
        createStage("LOGINSTAGE");
    }

    public void successfulInitiativeCreation() {
        FxController createInitiativeController = GUIControllerRegistry.getInstance().get(CreateInitiativeController.class.getName());
        Platform.runLater(() -> {
            createInitiativeController.closeStage();
        });
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public void responseLogs(List<String> logs){
        this.logs = logs;
        Platform.runLater(() -> {
            LogCentreController logCentreController = (LogCentreController)GUIControllerRegistry.getInstance().get(LogCentreController.class.getName());
            logCentreController.addLogsToUI(logs);
        });
    }

    public List<String> getLogs(){
        return logs;
    }

    public String getCurrentlySelectedInitiative() {
        return currentlySelectedInitiative;
    }

    public void setCurrentlySelectedInitiative(String currentlySelectedInitiative) {
        this.currentlySelectedInitiative = currentlySelectedInitiative;
    }

    public void updateClients(List<String> onlineClients) {
        HomeCentreController homeCentreController = (HomeCentreController) GUIControllerRegistry.getInstance().get(HomeCentreController.class.getName());
        Platform.runLater(() -> {
            homeCentreController.setOnlineUsers(onlineClients);
        });
    }

    public Initiative getSelectedInitiativeObject() {
        return GUIOutController.getInstance().getConnectionController().getSpecificInitiative(currentlySelectedInitiative);
    }

    public String getSelectedInitiativeType() {
        return GUIOutController.getInstance().getConnectionController().getCurrentInitiativeType(currentlySelectedInitiative);
    }

    public void populateViewSelectedInitiativeSceen() {

    }

    public void updateInitiatives(List<String> titles) {
        System.out.println("REACHED GUICONTROLLER ---------------------------------------------------------------");
        Platform.runLater(() -> {
            HomeLeftController homeLeftController = (HomeLeftController) GUIControllerRegistry.getInstance().get(HomeLeftController.class.getName());
            if (homeLeftController != null) {
                homeLeftController.updateInitiatives(titles);
            }
        });

    }


}
