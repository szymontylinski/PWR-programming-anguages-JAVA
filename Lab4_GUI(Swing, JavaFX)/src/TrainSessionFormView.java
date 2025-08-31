package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Widok GUI do zarządzania sesjami treningowymi.
 */
public class TrainSessionFormView extends JFrame {
    private DefaultComboBoxModel<String> workoutModel;
    private JComboBox<String> workoutComboBox;
    private JTextField dateField;
    private JTextField weightField;
    private JTextField durationField;
    private JButton addButton;
    private JButton deleteButton;
    private JList<String> sessionList;
    private DefaultListModel<String> listModel;

    public TrainSessionFormView() {
        setTitle("Training Session Manager");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Formularz sesji treningowej
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.add(new JLabel("Workout:"));

        workoutModel = new DefaultComboBoxModel<>();
        workoutComboBox = new JComboBox<>(workoutModel);
        formPanel.add(workoutComboBox);

        formPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        dateField = new JTextField();
        formPanel.add(dateField);

        formPanel.add(new JLabel("Weight (kg):"));
        weightField = new JTextField();
        formPanel.add(weightField);

        formPanel.add(new JLabel("Duration (min):"));
        durationField = new JTextField();
        formPanel.add(durationField);

        addButton = new JButton("Add Session");
        formPanel.add(addButton);

        // Lista sesji treningowych
        listModel = new DefaultListModel<>();
        sessionList = new JList<>(listModel);
        JScrollPane listScrollPane = new JScrollPane(sessionList);

        // Przyciski zarządzania sesjami
        JPanel buttonPanel = new JPanel(new GridLayout(1, 1, 10, 10));
        deleteButton = new JButton("Delete");
        buttonPanel.add(deleteButton);

        // Układ okna
        add(formPanel, BorderLayout.NORTH);
        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Gettery i setterzy
    public String getSelectedWorkout() {
        return (String) workoutComboBox.getSelectedItem();
    }

    public void addWorkoutToComboBox(String workout) {
        workoutModel.addElement(workout);
    }

    public String getDate() {
        return dateField.getText();
    }

    public String getWeight() {
        return weightField.getText();
    }

    public String getDuration() {
        return durationField.getText();
    }

    public int getSelectedSessionIndex() {
        return sessionList.getSelectedIndex();
    }

    public void addSessionToList(String session) {
        listModel.addElement(session);
    }

    public void removeSessionFromList(int index) {
        listModel.remove(index);
    }

    public void addAddButtonListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }

    public void addDeleteButtonListener(ActionListener listener) {
        deleteButton.addActionListener(listener);
    }
}
