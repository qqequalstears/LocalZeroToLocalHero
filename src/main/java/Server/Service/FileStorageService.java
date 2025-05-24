package Server.Service;

import java.io.*;

public class FileStorageService {
    private static final String DATA_DIR = "data";
    private static final String MESSAGES_DIR = DATA_DIR + "/messages";
    private static final String NOTIFICATIONS_DIR = DATA_DIR + "/notifications";
    
    public FileStorageService() {
        createDirectories();
    }
    
    private void createDirectories() {
        new File(MESSAGES_DIR).mkdirs();
        new File(NOTIFICATIONS_DIR).mkdirs();
    }
    
    public void appendToFile(String filename, String content) {
        try {
            File file = getFile(filename);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(content);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readFile(String filename) {
        StringBuilder content = new StringBuilder();
        try {
            File file = getFile(filename);
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public void deleteFile(String filename) {
        File file = getFile(filename);
        if (file.exists()) {
            file.delete();
        }
    }
    
    private File getFile(String filename) {
        if (filename.startsWith("messages")) {
            return new File(MESSAGES_DIR, filename);
        } else if (filename.startsWith("notifications")) {
            return new File(NOTIFICATIONS_DIR, filename);
        }
        return new File(DATA_DIR, filename);
    }
} 