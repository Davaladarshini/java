import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DiaryBackend {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // Add a new entry
    public static String addEntry(String entry) throws IOException {
        if (entry == null || entry.trim().isEmpty()) {
            throw new IllegalArgumentException("Entry cannot be empty.");
        }

        String timestamp = LocalDateTime.now().format(DATE_FORMAT);
        String fileName = "D" + timestamp.replace(":", "-").replace(" ", "_") + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("[" + timestamp + "]\n" + entry + "\n");
        }

        return fileName;
    }

    // View a specific entry
    public static String viewEntry(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new FileNotFoundException("Entry not found.");
        }

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    // Edit an existing entry
    public static void editEntry(String fileName, String newContent) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(newContent);
        }
    }

    // Delete a specific entry
    public static boolean deleteEntry(String fileName) {
        File file = new File(fileName);
        return file.exists() && file.delete();
    }

    // Get a list of all diary entry files
    public static String[] getDiaryFiles() {
        File folder = new File(".");
        File[] files = folder.listFiles((dir, name) -> name.startsWith("D") && name.endsWith(".txt"));

        if (files == null || files.length == 0) {
            return new String[0];
        }

        String[] fileNames = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            fileNames[i] = files[i].getName();
        }
        return fileNames;
    }
}
