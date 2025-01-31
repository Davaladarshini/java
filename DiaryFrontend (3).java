import java.awt.*;
import java.io.IOException;
import javax.swing.*;

public class DiaryFrontend {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(DiaryFrontend::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        // Create main frame
        JFrame frame = new JFrame("Personal Diary");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(230, 230, 250));

        // Panel for buttons on the left
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(new Color(240, 225, 245)); 

        // Create buttons with custom styles
        JButton addButton = createStyledButton("Add Entry");
        JButton viewButton = createStyledButton("View Entries");
        JButton editButton = createStyledButton("Edit Entry");
        JButton deleteButton = createStyledButton("Delete Entry");

        // Add spacing and buttons to the panel
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(viewButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(editButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(deleteButton);

        // Text area for displaying content
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(new Color(230, 230, 250));
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Add components to the frame
        frame.add(buttonPanel, BorderLayout.WEST);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Button actions
        addButton.addActionListener(e -> addEntry(textArea));
        viewButton.addActionListener(e -> viewEntries(textArea));
        editButton.addActionListener(e -> editEntry(textArea));
        deleteButton.addActionListener(e -> deleteEntry(textArea));

        // Show frame
        frame.setVisible(true);
    }

    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        
        button.setFont(new Font("Lucida Handwriting", Font.BOLD, 14));
        button.setBackground(new Color(150, 150, 250)); // Royal blue background
        button.setForeground(Color.BLACK); // White font color
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(30, 30, 30)));
        return button;
    }

    // Add a new diary entry
    private static void addEntry(JTextArea textArea) {
        String entry = JOptionPane.showInputDialog(null, "Enter your diary entry:", "Add Entry", JOptionPane.PLAIN_MESSAGE);
        if (entry == null || entry.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Entry cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String fileName = DiaryBackend.addEntry(entry);
            JOptionPane.showMessageDialog(null, "Entry saved successfully in " + fileName, "Success", JOptionPane.INFORMATION_MESSAGE);
            textArea.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error saving the entry: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // View all diary entries
    private static void viewEntries(JTextArea textArea) {
        String[] files = DiaryBackend.getDiaryFiles();
        if (files.length == 0) {
            JOptionPane.showMessageDialog(null, "No entries found.", "Info", JOptionPane.INFORMATION_MESSAGE);
            textArea.setText("");
            return;
        }

        String selectedFile = (String) JOptionPane.showInputDialog(null, "Select an entry to view:",
                "View Entry", JOptionPane.QUESTION_MESSAGE, null, files, files[0]);

        if (selectedFile != null) {
            try {
                String content = DiaryBackend.viewEntry(selectedFile);
                textArea.setText("File: " + selectedFile + "\n" + content);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error reading the entry: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Edit an existing diary entry
    private static void editEntry(JTextArea textArea) {
        String[] files = DiaryBackend.getDiaryFiles();
        if (files.length == 0) {
            JOptionPane.showMessageDialog(null, "No entries found to edit.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String selectedFile = (String) JOptionPane.showInputDialog(null, "Select an entry to edit:",
                "Edit Entry", JOptionPane.QUESTION_MESSAGE, null, files, files[0]);

        if (selectedFile != null) {
            try {
                String currentContent = DiaryBackend.viewEntry(selectedFile);
                String newContent = JOptionPane.showInputDialog(null, "Edit the entry:", "Edit Entry", JOptionPane.PLAIN_MESSAGE, null, null, currentContent).toString();

                if (newContent != null && !newContent.trim().isEmpty()) {
                    DiaryBackend.editEntry(selectedFile, newContent);
                    JOptionPane.showMessageDialog(null, "Entry updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    textArea.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Entry cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error editing the entry: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Delete a specific diary entry
    private static void deleteEntry(JTextArea textArea) {
        String[] files = DiaryBackend.getDiaryFiles();
        if (files.length == 0) {
            JOptionPane.showMessageDialog(null, "No entries found to delete.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String selectedFile = (String) JOptionPane.showInputDialog(null, "Select an entry to delete:",
                "Delete Entry", JOptionPane.QUESTION_MESSAGE, null, files, files[0]);

        if (selectedFile != null) {
            int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this entry?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = DiaryBackend.deleteEntry(selectedFile);
                if (success) {
                    JOptionPane.showMessageDialog(null, "Entry deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    textArea.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to delete the entry.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
