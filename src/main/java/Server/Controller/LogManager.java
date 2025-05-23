package Server.Controller;

import org.json.JSONArray;
import org.json.JSONObject;

public class LogManager {

    public void newLogEntry(JSONObject jsonObject){
        String email = jsonObject.getString("email");
        String logEntry = jsonObject.getString("logEntry");
        String time = jsonObject.getString("date");
        String logEntryString = email + "," + logEntry + "," + time;

      //  AppendToFile.getInstance().appendLogEnteryToFile(logEntryString);
        FileHandler.getInstance().appendLogEntryToFile(logEntryString);
    }

    public JSONObject requestLog(JSONObject jsonObject){
        String email = jsonObject.getString("email");
       // String log = ReaderFiles.getInstance().fetchAllLogData();
        String log =FileHandler.getInstance().fetchAllLogData();
        JSONArray logArray = getLogsForUser(email, log);
        JSONObject response = new JSONObject();
        response.put("type", "logResponse");
        response.put("log", logArray);
        return response;
    }

    public JSONArray getLogsForUser(String email, String log){
        String[] logEntries = log.split("\n");
        JSONArray logArray = new JSONArray();
        for (int i = 0; i < logEntries.length; i++) {
            String[] parts = logEntries[i].split(",");
            if (parts[0].equals(email)) {
                JSONObject logEntry = new JSONObject();
                logEntry.put("email", parts[0]);
                logEntry.put("logEntry", parts[1]);
                logEntry.put("date", parts[2]);
                logArray.put(logEntry);
            }
        }
        return logArray;
    }
}
