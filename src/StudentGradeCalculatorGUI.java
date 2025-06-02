// this will render the gui components for the frontend
// this class will inherit from the JFrame class
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class StudentGradeCalculatorGUI extends JFrame implements ActionListener {
    // ─── (1) NEW FIELDS FOR “STUDENT NAME”, “SCORES INPUT”, AND DISPLAY AREA ───────────────────
    private JTextField nameField;
    private JTextField scoresField;
    private JButton addButton;
    private JButton reportButton;
    private JTextArea reportArea;
    private JScrollPane reportScrollPane;
    // ──────────────────────────────────────────────────────────────────────────────────────────────

    // In-memory list of Student objects
    static class Student {
        String name;
        double average;
        char letterGrade;

        Student(String name, double average, char letterGrade) {
            this.name = name;
            this.average = average;
            this.letterGrade = letterGrade;
        }
    }

    private List<Student> students;

    public StudentGradeCalculatorGUI() {
        // this will render frame and add a title as well
        super("Student Grade Calculator");

        // ─── (A) FRAME SETUP ────────────────────────────────────────────────────
        // Set the size of the GUI
        setSize(650, 700);

        // Make it so people can't resize it
        setResizable(false);

        // Setting the layout to be null so we have control of component bounds
        setLayout(null);

        // Terminate the program when X is pressed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Centers the GUI on screen
        setLocationRelativeTo(null);
        // ────────────────────────────────────────────────────────────────────────

        // Initialize list to hold Student objects
        students = new ArrayList<>();

        // Rendering all GUI components
        addGuiComponents();

        // ─── (B) MAKE THE WINDOW VISIBLE ───────────────────────────────────────
        setVisible(true);
        // ────────────────────────────────────────────────────────────────────────
    }

    private void addGuiComponents() {
        // Create title label text
        JLabel titleLabel = new JLabel("Student Grade Calculator");
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 32));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 10, 650, 40);
        add(titleLabel);

        // create “Student Name” label
        JLabel nameLabel = new JLabel("Student Name:");
        nameLabel.setFont(new Font("Dialog", Font.PLAIN, 24));
        nameLabel.setBounds(25, 70, 250, 30);
        add(nameLabel);

        // student name input field
        nameField = new JTextField();
        nameField.setFont(new Font("Dialog", Font.PLAIN, 24));
        nameField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        nameField.setBounds(250, 70, 350, 30);
        add(nameField);

        // create “Scores” label
        JLabel scoresLabel = new JLabel("Scores (comma‐separated):");
        scoresLabel.setFont(new Font("Dialog", Font.PLAIN, 24));
        scoresLabel.setBounds(25, 130, 350, 30);
        add(scoresLabel);

        // scores input field
        scoresField = new JTextField();
        scoresField.setFont(new Font("Dialog", Font.PLAIN, 24));
        scoresField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        scoresField.setBounds(350, 130, 250, 30);
        add(scoresField);

        // ─── (2) “ADD STUDENT” BUTTON ───────────────────────────────────────────────
        addButton = new JButton("Add Student");
        addButton.setFont(new Font("Dialog", Font.PLAIN, 24));
        addButton.setBackground(Color.ORANGE);
        addButton.setBounds(25, 200, 250, 50);
        add(addButton);
        addButton.addActionListener(this);
        // ────────────────────────────────────────────────────────────────────────────

        // ─── (3) “SHOW REPORT” BUTTON ──────────────────────────────────────────────
        reportButton = new JButton("Show Report");
        reportButton.setFont(new Font("Dialog", Font.PLAIN, 24));
        reportButton.setBackground(Color.ORANGE);
        reportButton.setBounds(300, 200, 250, 50);
        add(reportButton);
        reportButton.addActionListener(this);
        // ────────────────────────────────────────────────────────────────────────────

        // create the report text area (read-only)
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
        reportArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // add scrollability to the report area
        reportScrollPane = new JScrollPane(reportArea);
        reportScrollPane.setBounds(25, 280, 600, 350);
        add(reportScrollPane);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == addButton) {
            // Read name and scores text
            String name = nameField.getText().trim();
            String scoresText = scoresField.getText().trim();

            // if either field is empty, show a warning
            if (name.isEmpty() || scoresText.isEmpty()) {
                JOptionPane.showMessageDialog(
                        StudentGradeCalculatorGUI.this,
                        "Enter both Student Name and Scores.",
                        "Input Error",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // Parse comma‐separated scores
            String[] parts = scoresText.split(",");
            double sum = 0;
            for (String part : parts) {
                try {
                    double score = Double.parseDouble(part.trim());
                    sum += score;
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            StudentGradeCalculatorGUI.this,
                            "Invalid score: \"" + part.trim() + "\"\nUse numbers separated by commas.",
                            "Input Error",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }
            }

            double average = sum / parts.length;
            char letter = computeLetterGrade(average);

            // Add new Student to the list
            students.add(new Student(name, average, letter));

            // Clear input fields for next entry
            nameField.setText("");
            scoresField.setText("");

            // Confirmation dialog
            JOptionPane.showMessageDialog(
                    StudentGradeCalculatorGUI.this,
                    "Added: " + name + " (Avg: " + String.format("%.2f", average) + ", Grade: " + letter + ")",
                    "Student Added",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } else if (src == reportButton) {
            // Build report text
            if (students.isEmpty()) {
                reportArea.setText("No students added yet.");
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-20s %-8s %-6s%n", "Name", "Average", "Grade"));
            sb.append("---------------------------------------------\n");

            for (Student s : students) {
                sb.append(String.format("%-20s %6.2f   %4c%n", s.name, s.average, s.letterGrade));
            }

            reportArea.setText(sb.toString());
        }
    }

    // Simple letter‐grade mapping
    private char computeLetterGrade(double avg) {
        if (avg >= 90) return 'A';
        else if (avg >= 80) return 'B';
        else if (avg >= 70) return 'C';
        else if (avg >= 60) return 'D';
        else return 'F';
    }

    public static void main(String[] args) {
        // Launch the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new StudentGradeCalculatorGUI());
    }
}
