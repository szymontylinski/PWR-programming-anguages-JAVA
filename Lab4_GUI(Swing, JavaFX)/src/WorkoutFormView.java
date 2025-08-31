package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Widok GUI do tworzenia i zarządzania treningami.
 */
public class WorkoutFormView extends JFrame {
    private JTextField workoutNameField;
    private JComboBox<String> groupComboBox;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JList<String> workoutList;
    private DefaultListModel<String> listModel;

    public WorkoutFormView() {
        setTitle("Workout Manager");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Formularz treningu
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.add(new JLabel("Workout Name:"));
        workoutNameField = new JTextField();
        formPanel.add(workoutNameField);

        formPanel.add(new JLabel("Group:"));
        groupComboBox = new JComboBox<>(new String[]{"Cardio", "Strength", "Flexibility", "Other"});
        formPanel.add(groupComboBox);

        addButton = new JButton("Add Workout");
        formPanel.add(addButton);

        // Lista treningów
        listModel = new DefaultListModel<>();
        workoutList = new JList<>(listModel);
        JScrollPane listScrollPane = new JScrollPane(workoutList);

        // Przyciski zarządzania treningami
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // Układ okna
        add(formPanel, BorderLayout.NORTH);
        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Gettery
    public String getWorkoutName() {
        return workoutNameField.getText();
    }

    public String getSelectedGroup() {
        return (String) groupComboBox.getSelectedItem();
    }

    public int getSelectedWorkoutIndex() {
        return workoutList.getSelectedIndex();
    }

    public void setWorkoutName(String name) {
        workoutNameField.setText(name);
    }

    public void addWorkoutToList(String workout) {
        listModel.addElement(workout);
    }

    public void removeWorkoutFromList(int index) {
        listModel.remove(index);
    }

    public void updateWorkoutInList(int index, String workout) {
        listModel.set(index, workout);
    }

    public void addAddButtonListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }

    public void addEditButtonListener(ActionListener listener) {
        editButton.addActionListener(listener);
    }

    public void addDeleteButtonListener(ActionListener listener) {
        deleteButton.addActionListener(listener);
    }
}
