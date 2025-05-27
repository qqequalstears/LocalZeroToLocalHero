package Server.Controller;

import Client.Model.Initiative.Children.CarPool;
import Client.Model.Initiative.Children.GarageSale;
import Client.Model.Initiative.Children.Gardening;
import Client.Model.Initiative.Children.ToolSharing;
import Client.Model.Initiative.Parent.Initiative;
import Client.Model.User;
import Common.Controller.Utility.Packager;
import Server.Controller.Authorization.AuthorizationController;
import Server.Service.MessageService;
import Server.Service.FileStorageService;
import Server.Service.NotificationService;
import Server.Model.AchievementTracker;
import Server.Model.FileMan.ReaderFiles;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.Socket;
import java.io.*;
import java.util.List;

public class ConnectionController {

    private ConnectionListener connectionListener;
    private ClientUpdater clientUpdater;
    private Packager packager;
    private AuthorizationController authorizationController;
    private InitiativeManager initiativeManager;
    private MessageService messageService;
    private FileStorageService fileStorageService;
    private NotificationService notificationService;
    private UserManager userManager;
    private LogManager logManager;

    public ConnectionController() {
        authorizationController = new AuthorizationController(this);
        initiativeManager = new InitiativeManager();
        packager = new Packager();
        userManager = new UserManager();
        logManager = new LogManager();
        this.clientUpdater = new ClientUpdater();
        this.connectionListener = new ConnectionListener(2343, this);
        this.fileStorageService = new FileStorageService();
        this.notificationService = new NotificationService(clientUpdater, fileStorageService);
        this.messageService = new MessageService(clientUpdater, fileStorageService, notificationService);
    }

    public synchronized void addConnection(Socket socket) {
        try {
            ClientConnection clientConnection = new ClientConnection(socket, this);
            clientUpdater.addClient(clientConnection);
            new Thread(clientConnection).start();
        } catch (Exception e) {
            System.out.println("Error creating client connection: " + socket.getInetAddress());
            e.printStackTrace();
        }
        System.out.println("Client connected: " + socket.getInetAddress());
    }

    public synchronized void revealIntention(Object object, ClientConnection sender) {
        String jsonString = (String) object;
        JSONObject jsonObject = new JSONObject(jsonString);
        System.out.println("[DEBUG] revealIntention called with: " + jsonObject);
        String intention = (String) jsonObject.get("type");
        System.out.println("Intention is " + intention);

        switch (intention) {
            case "login":
                handleLogin(jsonObject, sender);
                break;
            case "logout":
                handleLogout(jsonObject);
                break;
            case "register":
                handleRegister(jsonObject, sender);
                break;
            case "createInitiative":
                handleCreateInitiative(jsonObject, sender);
                break;
            case "joinInitiative":
                handleJoinInitiative(jsonObject, sender);
                break;
            case "leaveInitiative":
                handleLeaveInitiative(jsonObject, sender);
                break;
            case "addComment":
                handleAddComment(jsonObject, sender);
                break;
            case "replyComment":
                handleReplyComment(jsonObject, sender);
                break;
            case "likeComment":
                handleLikeComment(jsonObject, sender);
                break;
            case "getNeighborhoodInitiatives":
                System.out.println("[DEBUG] Processing getNeighborhoodInitiatives request");
                handleGetNeighborhoodInitiatives(jsonObject, sender);
                break;
            case "sendMessage":
                messageService.handleNewMessage(jsonObject);
                break;
            case "collectUserInfo":
                collectUserRoles(jsonObject, sender);
                break;
            case "updateRoles":
                userManager.updateRoles(jsonObject, this);
                break;
            case "requestAchievements":
                String email = jsonObject.getString("email");
                sendAchievementForLocation(email, sender);
                break;
            case "newLogEntry":
                logManager.newLogEntry(jsonObject);
                requestLog(jsonObject, sender);
                break;
            case "requestLog":
                requestLog(jsonObject, sender);
                break;
            case "getInitiatives":
                sendAllActiveInitiatives(sender);
                break;
            case "testNotification":
                // Test notification functionality
                String testEmail = jsonObject.optString("email", "test@test.com");
                String testMessage = jsonObject.optString("message", "Test notification from server");
                System.out.println("[DEBUG] Sending test notification to: " + testEmail);
                sendNotification(testMessage, testEmail);
                break;
            default:
                System.out.println("Intention was not found: " + intention);
                break;
        }
    }

    private void handleLogin(JSONObject jsonObject, ClientConnection sender) {
        String mail = (String) jsonObject.get("mail");
        boolean successfulLogin = authorizationController.tryLogin(jsonObject, clientUpdater);
        sendLoginStatus(sender, mail, successfulLogin);

        if (successfulLogin) {
            deliverStoredNotifications(mail, sender);
        }
    }

    private void deliverStoredNotifications(String mail, ClientConnection sender) {
        System.out.println("[DEBUG] Delivering stored notifications for: " + mail);
        String filename = "notifications_" + mail + ".txt";
        String notifications = fileStorageService.readFile(filename);
        System.out.println("[DEBUG] Read notifications content: '" + notifications + "'");
        
        if (!notifications.isEmpty()) {
            String[] notificationLines = notifications.split("\n");
            System.out.println("[DEBUG] Found " + notificationLines.length + " notification lines");
            
            for (String notification : notificationLines) {
                if (!notification.trim().isEmpty()) {
                    System.out.println("[DEBUG] Sending stored notification: " + notification);
                    JSONObject notificationPackage = new JSONObject();
                    notificationPackage.put("type", "notification");
                    notificationPackage.put("notification", notification.trim());
                    sender.sendObject(notificationPackage.toString());
                }
            }
            fileStorageService.deleteFile(filename);
            System.out.println("[DEBUG] Stored notifications delivered and file deleted");
        } else {
            System.out.println("[DEBUG] No stored notifications found for: " + mail);
        }
    }

    private void handleLogout(JSONObject jsonObject) {
        String mail = (String) jsonObject.get("mail");
        clientUpdater.removeOnlineClient(mail);
    }

    private void handleRegister(JSONObject jsonObject, ClientConnection sender) {
        boolean successfulRegister = authorizationController.tryRegister(jsonObject);
        String mail = (String) jsonObject.get("mail");
        sendRegisterStatus(sender, mail, successfulRegister);
    }

    private void handleCreateInitiative(JSONObject jsonObject, ClientConnection sender) {
        System.out.println("[DEBUG] handleCreateInitiative called with: " + jsonObject.toString());
        boolean success = initiativeManager.createNewInitiative(jsonObject);
        System.out.println("[DEBUG] Initiative creation result: " + success);
        if (success) {
            notifyOfAchievement(jsonObject);
            // Broadcast updated initiatives to all clients
            System.out.println("[DEBUG] Broadcasting updated initiatives to all clients");
            broadcastUpdatedInitiatives();
        }
        System.out.println("[DEBUG] Sending create initiative status to client: " + (success ? "success" : "failure"));
        sendCreateInitiativeStatus(success, sender);
    }

    public void sendNotification(String notification, String mailToReceiver) {
        System.out.println("[DEBUG] sendNotification called for: " + mailToReceiver + " with: " + notification);
        
        // Validate inputs
        if (notification == null || notification.trim().isEmpty() || mailToReceiver == null || mailToReceiver.trim().isEmpty()) {
            System.out.println("[DEBUG] Invalid notification parameters, skipping");
            return;
        }
        
        ClientConnection receiver = clientUpdater.getClientConnection(mailToReceiver);
        System.out.println("[DEBUG] Receiver connection: " + (receiver != null ? "found" : "not found"));
        
        if (receiver != null) {
            // Send to online user
            JSONObject notificationPackage = new JSONObject();
            notificationPackage.put("type", "notification");
            notificationPackage.put("notification", notification);
            receiver.sendObject(notificationPackage.toString());
            System.out.println("[DEBUG] Notification sent to online user: " + mailToReceiver);
        } else {
            // Store notification for offline user using FileStorageService
            String filename = "notifications_" + mailToReceiver + ".txt";
            fileStorageService.appendToFile(filename, notification);
            System.out.println("[DEBUG] Notification stored for offline user: " + mailToReceiver);
        }
    }

    private void sendCreateInitiativeStatus(boolean success, ClientConnection creator) {
        JSONObject sucess = new JSONObject();
        String status = "unSuccessfulInitiativeCreation";
        if (success) {
            status = "SuccessfulInitiativeCreation";
        }
        sucess.put("type", status);
        System.out.println("[DEBUG] Sending initiative creation status: " + status);
        creator.sendObject(sucess.toString());
        System.out.println("[DEBUG] Initiative creation status sent");
    }

    private void sendRegisterStatus(ClientConnection sender, String mail, boolean successfulRegister) {
        JSONObject sucess = new JSONObject();
        String status = "unSuccessfulRegister";
        if (successfulRegister) {
            status = "successfulLogin";
            clientUpdater.addOnlineClient(mail, sender);
        }
        sucess.put("type", status);
        sender.sendObject(sucess.toString());

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sendEveryUser();
    }

    private void sendLoginStatus(ClientConnection sender, String mail, boolean successfulLogin) {
        JSONObject sucess = new JSONObject();
        String status = "unSuccessfulLogin";
        if (successfulLogin) {
            status = "successfulLogin";
            clientUpdater.addOnlineClient(mail, sender);
        }
        sucess.put("type", status);
        sender.sendObject(sucess.toString());

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sendEveryUser();
    }

    private void sendAchievementForLocation(String email, ClientConnection sender) {
        String location = FileHandler.getInstance().fetchOneUserLocationData(email);
        JSONArray achievementList = AchievementTracker.getInstance().getAchievementsForLocation(location);
        JSONObject achievementPackage = new JSONObject();
        achievementPackage.put("type", "achievementsLocation");
        achievementPackage.put("achievements", achievementList);

        sender.sendObject(achievementPackage.toString());
    }

    private void requestLog(JSONObject jsonObject, ClientConnection sender) {
        JSONObject logResponse = logManager.requestLog(jsonObject);
        sender.sendObject(logResponse.toString());
    }

    private void sendEveryUser() {
        List<User> users = FileHandler.getInstance().getUsers();

        JSONArray userArray = new JSONArray();
        for (User user : users) {
            JSONObject userJson = new JSONObject();
            userJson.put("email", user.getEmail());
            userArray.put(userJson);
        }

        JSONObject listOfUsers = new JSONObject();
        listOfUsers.put("type", "updateClients");
        listOfUsers.put("listOfUsers", userArray);

        List<ClientConnection> onlineClients = clientUpdater.getClientConnections();
        if (onlineClients != null) {
            for (ClientConnection client : onlineClients) {
                client.sendObject(listOfUsers.toString());
            }
        }
    }

    private void collectUserRoles(JSONObject jsonObject, ClientConnection connection) {
        String connectionMail = (String) jsonObject.get("connectionMail");
        JSONObject userRolesJSON = userManager.collectUserInfo(jsonObject);
        boolean connectionIsAdmin = userManager.isAdmin(connectionMail);
        userRolesJSON.put("isAdmin", connectionIsAdmin);

        connection.sendObject(userRolesJSON.toString());
    }

    private void notifyOfAchievement(JSONObject jsonObject) {
        String location = jsonObject.getString("location");
        String category = jsonObject.getString("category");
        List<User> users = FileHandler.getInstance().getUsers();
        for (User user : users) {
            if (user.getLocation().equalsIgnoreCase(location)) {
                String mail = user.getEmail();
                String notification = "New progress on: " + category;
                sendNotification(notification, mail);
            }
        }
    }

    private void sendAllActiveInitiatives(ClientConnection sender) {
        System.out.println("[DEBUG] sendAllActiveInitiatives called");
        List<Initiative> allActiveInitiatives = FileHandler.getInstance().getAllActiveInitiatives();
        System.out.println("[DEBUG] Found " + allActiveInitiatives.size() + " initiatives to send");
        JSONArray allActiveInitiativesArray = new JSONArray();

        // Send ALL initiatives (both public and private) for community visibility
        for (int i = 0; i < allActiveInitiatives.size(); i++) {
            Initiative initiative = allActiveInitiatives.get(i);
            System.out.println("[DEBUG] Processing initiative " + i + ": " + initiative.getTitle() + " (type: " + initiative.getClass().getSimpleName() + ")");
            JSONObject initiativeJson = new JSONObject();

            if (initiative instanceof CarPool) {
                System.out.println("[DEBUG] Creating JSON for CarPool: " + initiative.getTitle());
                initiativeJson = packager.createJsonForInitiativeCarPool((CarPool) initiative);
            } else if (initiative instanceof GarageSale) {
                System.out.println("[DEBUG] Creating JSON for GarageSale: " + initiative.getTitle());
                initiativeJson = packager.createJsonForInitiativeGarageSale((GarageSale) initiative);
            } else if (initiative instanceof Gardening) {
                System.out.println("[DEBUG] Creating JSON for Gardening: " + initiative.getTitle());
                initiativeJson = packager.createJsonForInitiativeGargening((Gardening) initiative);
            } else if (initiative instanceof ToolSharing) {
                System.out.println("[DEBUG] Creating JSON for ToolSharing: " + initiative.getTitle());
                initiativeJson = packager.createJsonForInitiativeToolSharing((ToolSharing) initiative);
            } else {
                System.out.println("[DEBUG] Unknown initiative type for: " + initiative.getTitle() + " (type: " + initiative.getClass().getName() + ")");
                continue;
            }

            System.out.println("[DEBUG] Created JSON with keys: " + initiativeJson.keySet());
            allActiveInitiativesArray.put(initiativeJson);
        }

        JSONObject response = new JSONObject();
        response.put("type", "updateInitiatives");
        response.put("listOfInitiatives", allActiveInitiativesArray);

        System.out.println("[DEBUG] Sending updateInitiatives with " + allActiveInitiativesArray.length() + " initiatives");
        sender.sendObject(response.toString());
    }

    private void handleJoinInitiative(JSONObject jsonObject, ClientConnection sender) {
        String userEmail = jsonObject.getString("userEmail");
        String initiativeTitle = jsonObject.getString("initiativeTitle");
        System.out.println("[DEBUG] handleJoinInitiative - User: " + userEmail + ", Initiative: " + initiativeTitle);
        
        List<Initiative> initiatives = FileHandler.getInstance().getAllActiveInitiatives();
        System.out.println("[DEBUG] Total initiatives found: " + initiatives.size());
        Initiative targetInitiative = null;
        
        for (Initiative initiative : initiatives) {
            System.out.println("[DEBUG] Checking initiative: " + initiative.getTitle());
            if (initiative.getTitle().equals(initiativeTitle)) {
                targetInitiative = initiative;
                System.out.println("[DEBUG] Found target initiative!");
                break;
            }
        }
        
        JSONObject response = new JSONObject();
        if (targetInitiative != null) {
            System.out.println("[DEBUG] Current participants: " + targetInitiative.getParticipants());
            System.out.println("[DEBUG] Is full: " + targetInitiative.isFull());
            System.out.println("[DEBUG] Already contains user: " + targetInitiative.getParticipants().contains(userEmail));
            
            boolean joinResult = targetInitiative.join(userEmail);
            System.out.println("[DEBUG] Join result: " + joinResult);
            
            if (joinResult) {
                // Update persistence
                initiativeManager.updateInitiativeParticipants(targetInitiative);
                
                // Notify existing participants
                for (String participantEmail : targetInitiative.getParticipants()) {
                    if (!participantEmail.equals(userEmail)) {
                        String notification = userEmail + " joined the initiative: " + initiativeTitle;
                        sendNotification(notification, participantEmail);
                    }
                }
                
                response.put("type", "joinInitiativeSuccess");
                response.put("message", "Successfully joined initiative");
            } else {
                // Check specific reason for failure
                if (targetInitiative.getParticipants().contains(userEmail)) {
                    response.put("type", "joinInitiativeFailure");
                    response.put("message", "You are already a participant in this initiative");
                } else if (targetInitiative.isFull()) {
                    response.put("type", "joinInitiativeFailure");
                    response.put("message", "Initiative is full");
                } else {
                    response.put("type", "joinInitiativeFailure");
                    response.put("message", "Failed to join initiative");
                }
            }
        } else {
            System.out.println("[DEBUG] Target initiative not found!");
            response.put("type", "joinInitiativeFailure");
            response.put("message", "Initiative not found");
        }
        
        sender.sendObject(response.toString());
        // Send updated initiatives to all clients
        broadcastUpdatedInitiatives();
    }

    private void handleLeaveInitiative(JSONObject jsonObject, ClientConnection sender) {
        String userEmail = jsonObject.getString("userEmail");
        String initiativeTitle = jsonObject.getString("initiativeTitle");
        
        List<Initiative> initiatives = FileHandler.getInstance().getAllActiveInitiatives();
        Initiative targetInitiative = null;
        
        for (Initiative initiative : initiatives) {
            if (initiative.getTitle().equals(initiativeTitle)) {
                targetInitiative = initiative;
                break;
            }
        }
        
        JSONObject response = new JSONObject();
        if (targetInitiative != null && targetInitiative.leave(userEmail)) {
            // Update persistence
            initiativeManager.updateInitiativeParticipants(targetInitiative);
            
            response.put("type", "leaveInitiativeSuccess");
            response.put("message", "Successfully left initiative");
        } else {
            response.put("type", "leaveInitiativeFailure");
            response.put("message", "Failed to leave initiative");
        }
        
        sender.sendObject(response.toString());
        // Send updated initiatives to all clients
        broadcastUpdatedInitiatives();
    }

    private void handleAddComment(JSONObject jsonObject, ClientConnection sender) {
        System.out.println("[DEBUG] handleAddComment called with: " + jsonObject.toString());
        String userEmail = jsonObject.getString("userEmail");
        String initiativeTitle = jsonObject.getString("initiativeTitle");
        String commentContent = jsonObject.getString("content");
        String commentId = java.util.UUID.randomUUID().toString();
        System.out.println("[DEBUG] Comment details - User: " + userEmail + ", Initiative: " + initiativeTitle + ", Content: " + commentContent);
        
        List<Initiative> initiatives = FileHandler.getInstance().getAllActiveInitiatives();
        Initiative targetInitiative = null;
        
        for (Initiative initiative : initiatives) {
            if (initiative.getTitle().equals(initiativeTitle)) {
                targetInitiative = initiative;
                break;
            }
        }
        
        JSONObject response = new JSONObject();
        if (targetInitiative != null) {
            // Check if user is a participant
            if (!targetInitiative.getParticipants().contains(userEmail)) {
                response.put("type", "addCommentFailure");
                response.put("message", "You must join the initiative to comment");
                sender.sendObject(response.toString());
                return;
            }
            
            Initiative.Comment comment = new Initiative.Comment(commentId, userEmail, commentContent, null);
            targetInitiative.addComment(comment);
            
            // Update persistence
            initiativeManager.updateInitiativeComments(targetInitiative);
            
            // Notify all participants
            for (String participantEmail : targetInitiative.getParticipants()) {
                if (!participantEmail.equals(userEmail)) {
                    String notification = userEmail + " commented on initiative: " + initiativeTitle;
                    sendNotification(notification, participantEmail);
                    System.out.println("[DEBUG] Comment notification sent to participant: " + participantEmail);
                } else {
                    System.out.println("[DEBUG] Skipping self-comment notification for: " + participantEmail);
                }
            }
            
            response.put("type", "addCommentSuccess");
            response.put("commentId", commentId);
        } else {
            response.put("type", "addCommentFailure");
            response.put("message", "Initiative not found");
        }
        
        sender.sendObject(response.toString());
        broadcastUpdatedInitiatives();
    }

    private void handleReplyComment(JSONObject jsonObject, ClientConnection sender) {
        System.out.println("[DEBUG] handleReplyComment called with: " + jsonObject.toString());
        String userEmail = jsonObject.getString("userEmail");
        String initiativeTitle = jsonObject.getString("initiativeTitle");
        String commentContent = jsonObject.getString("content");
        String parentCommentId = jsonObject.getString("parentCommentId");
        String replyId = java.util.UUID.randomUUID().toString();
        System.out.println("[DEBUG] Reply details - User: " + userEmail + ", Initiative: " + initiativeTitle + ", Parent: " + parentCommentId);
        
        List<Initiative> initiatives = FileHandler.getInstance().getAllActiveInitiatives();
        Initiative targetInitiative = null;
        
        for (Initiative initiative : initiatives) {
            if (initiative.getTitle().equals(initiativeTitle)) {
                targetInitiative = initiative;
                break;
            }
        }
        
        JSONObject response = new JSONObject();
        if (targetInitiative != null) {
            // Check if user is a participant
            if (!targetInitiative.getParticipants().contains(userEmail)) {
                response.put("type", "replyCommentFailure");
                response.put("message", "You must join the initiative to reply");
                sender.sendObject(response.toString());
                return;
            }
            
            Initiative.Comment reply = new Initiative.Comment(replyId, userEmail, commentContent, parentCommentId);
            
            // Find parent comment and add reply
            Initiative.Comment parentComment = findCommentById(targetInitiative, parentCommentId);
            if (parentComment != null) {
                parentComment.addReply(reply);
                
                // Update persistence
                initiativeManager.updateInitiativeComments(targetInitiative);
                
                // Notify original comment author (but not if replying to own comment)
                if (!parentComment.getAuthorEmail().equals(userEmail)) {
                    String notification = userEmail + " replied to your comment on initiative: " + initiativeTitle;
                    sendNotification(notification, parentComment.getAuthorEmail());
                    System.out.println("[DEBUG] Reply notification sent to: " + parentComment.getAuthorEmail());
                } else {
                    System.out.println("[DEBUG] Skipping self-reply notification");
                }
                
                response.put("type", "replyCommentSuccess");
                response.put("replyId", replyId);
            } else {
                response.put("type", "replyCommentFailure");
                response.put("message", "Parent comment not found");
            }
        } else {
            response.put("type", "replyCommentFailure");
            response.put("message", "Initiative not found");
        }
        
        sender.sendObject(response.toString());
        broadcastUpdatedInitiatives();
    }

    private void handleLikeComment(JSONObject jsonObject, ClientConnection sender) {
        System.out.println("[DEBUG] handleLikeComment called with: " + jsonObject.toString());
        String userEmail = jsonObject.getString("userEmail");
        String initiativeTitle = jsonObject.getString("initiativeTitle");
        String commentId = jsonObject.getString("commentId");
        System.out.println("[DEBUG] Like details - User: " + userEmail + ", Initiative: " + initiativeTitle + ", Comment: " + commentId);
        
        List<Initiative> initiatives = FileHandler.getInstance().getAllActiveInitiatives();
        Initiative targetInitiative = null;
        
        for (Initiative initiative : initiatives) {
            if (initiative.getTitle().equals(initiativeTitle)) {
                targetInitiative = initiative;
                break;
            }
        }
        
        JSONObject response = new JSONObject();
        if (targetInitiative != null) {
            // Check if user is a participant
            if (!targetInitiative.getParticipants().contains(userEmail)) {
                response.put("type", "likeCommentFailure");
                response.put("message", "You must join the initiative to like comments");
                sender.sendObject(response.toString());
                return;
            }
            
            Initiative.Comment comment = findCommentById(targetInitiative, commentId);
            if (comment != null && comment.like(userEmail)) {
                // Update persistence
                initiativeManager.updateInitiativeComments(targetInitiative);
                
                // Notify comment author (but not if liking own comment)
                if (!comment.getAuthorEmail().equals(userEmail)) {
                    String notification = userEmail + " liked your comment on initiative: " + initiativeTitle;
                    sendNotification(notification, comment.getAuthorEmail());
                    System.out.println("[DEBUG] Like notification sent to: " + comment.getAuthorEmail());
                } else {
                    System.out.println("[DEBUG] Skipping self-like notification");
                }
                
                response.put("type", "likeCommentSuccess");
                response.put("likes", comment.getLikes());
            } else {
                response.put("type", "likeCommentFailure");
                response.put("message", "Already liked or comment not found");
            }
        } else {
            response.put("type", "likeCommentFailure");
            response.put("message", "Initiative not found");
        }
        
        sender.sendObject(response.toString());
        broadcastUpdatedInitiatives();
    }

    private void handleGetNeighborhoodInitiatives(JSONObject jsonObject, ClientConnection sender) {
        System.out.println("[DEBUG] handleGetNeighborhoodInitiatives called");
        String userEmail = jsonObject.getString("userEmail");
        String userLocation = FileHandler.getInstance().fetchOneUserLocationData(userEmail);
        System.out.println("[DEBUG] User: " + userEmail + ", Location: " + userLocation);
        
        List<Initiative> allInitiatives = FileHandler.getInstance().getAllActiveInitiatives();
        System.out.println("[DEBUG] Total initiatives: " + allInitiatives.size());
        JSONArray neighborhoodInitiatives = new JSONArray();
        
        for (Initiative initiative : allInitiatives) {
            System.out.println("[DEBUG] Initiative: " + initiative.getTitle() + 
                             ", Location: " + initiative.getLocation() + 
                             ", IsPublic: " + initiative.isPublic() + 
                             ", Participants: " + initiative.getParticipants());
            
            // Include if it's in the same location and either public or user is a participant
            boolean sameLocation = initiative.getLocation().equalsIgnoreCase(userLocation);
            boolean isPublicOrParticipant = initiative.isPublic() || initiative.getParticipants().contains(userEmail);
            
            System.out.println("[DEBUG] Same location: " + sameLocation + ", Public or participant: " + isPublicOrParticipant);
            
            if (sameLocation && isPublicOrParticipant) {
                System.out.println("[DEBUG] Including initiative: " + initiative.getTitle());
                JSONObject initiativeJson = new JSONObject();
                if (initiative instanceof CarPool) {
                    initiativeJson = packager.createJsonForInitiativeCarPool((CarPool) initiative);
                } else if (initiative instanceof GarageSale) {
                    initiativeJson = packager.createJsonForInitiativeGarageSale((GarageSale) initiative);
                } else if (initiative instanceof Gardening) {
                    initiativeJson = packager.createJsonForInitiativeGargening((Gardening) initiative);
                } else if (initiative instanceof ToolSharing) {
                    initiativeJson = packager.createJsonForInitiativeToolSharing((ToolSharing) initiative);
                }
                neighborhoodInitiatives.put(initiativeJson);
            }
        }
        
        System.out.println("[DEBUG] Found " + neighborhoodInitiatives.length() + " neighborhood initiatives");
        
        JSONObject response = new JSONObject();
        response.put("type", "neighborhoodInitiatives");
        response.put("initiatives", neighborhoodInitiatives);
        
        sender.sendObject(response.toString());
    }

    private Initiative.Comment findCommentById(Initiative initiative, String commentId) {
        for (Initiative.Comment comment : initiative.getCommentList()) {
            if (comment.getId().equals(commentId)) {
                return comment;
            }
            // Check replies recursively
            Initiative.Comment found = findCommentInReplies(comment, commentId);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private Initiative.Comment findCommentInReplies(Initiative.Comment comment, String commentId) {
        for (Initiative.Comment reply : comment.getReplies()) {
            if (reply.getId().equals(commentId)) {
                return reply;
            }
            Initiative.Comment found = findCommentInReplies(reply, commentId);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private void broadcastUpdatedInitiatives() {
        System.out.println("[DEBUG] broadcastUpdatedInitiatives called");
        List<ClientConnection> onlineClients = clientUpdater.getClientConnections();
        System.out.println("[DEBUG] Number of online clients: " + (onlineClients != null ? onlineClients.size() : 0));
        if (onlineClients != null) {
            for (ClientConnection client : onlineClients) {
                System.out.println("[DEBUG] Sending updated initiatives to client");
                sendAllActiveInitiatives(client);
            }
        }
    }

}
