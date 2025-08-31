package view;

import model.Workout;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainView extends JFrame {
    private DefaultListModel<Workout> workoutListModel;
    private JList<Workout> workoutList;

    public MainView() {
        super("Fitness Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(1200, 700);

        // Kolor tła aplikacji
        getContentPane().setBackground(new Color(220, 240, 255));

        // Panel listy treningów
        workoutListModel = new DefaultListModel<>();
        loadWorkoutsFromFile(); // Wczytaj dane z pliku przy starcie
        workoutList = new JList<>(workoutListModel);
        workoutList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Kolor tła i tekstu listy
        workoutList.setBackground(new Color(200, 230, 255));
        workoutList.setForeground(Color.BLACK);

        JScrollPane workoutScrollPane = new JScrollPane(workoutList);
        workoutScrollPane.setPreferredSize(new Dimension(300, 700));
        workoutScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(workoutScrollPane, BorderLayout.WEST);

        // Panel przycisków
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(180, 210, 255)); // Kolor tła panelu przycisków

        JButton addWorkoutButton = new JButton("Add Workout");
        styleButton(addWorkoutButton);
        addWorkoutButton.addActionListener(e -> addWorkout());

        JButton editWorkoutButton = new JButton("Edit Workout");
        styleButton(editWorkoutButton);
        editWorkoutButton.addActionListener(e -> editWorkout());

        JButton deleteWorkoutButton = new JButton("Delete Workout");
        styleButton(deleteWorkoutButton);
        deleteWorkoutButton.addActionListener(e -> deleteWorkout());

        JButton saveWorkoutsButton = new JButton("Save Workouts");
        styleButton(saveWorkoutsButton);
        saveWorkoutsButton.addActionListener(e -> saveWorkoutsToFile());

        JButton manageSessionsButton = new JButton("Manage Sessions");
        styleButton(manageSessionsButton);
        manageSessionsButton.addActionListener(e -> manageSessions());

        buttonPanel.add(addWorkoutButton);
        buttonPanel.add(editWorkoutButton);
        buttonPanel.add(deleteWorkoutButton);
        buttonPanel.add(saveWorkoutsButton);
        buttonPanel.add(manageSessionsButton);

        add(buttonPanel, BorderLayout.NORTH);

        setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(100, 150, 255)); // Kolor tła przycisku
        button.setForeground(Color.WHITE); // Kolor tekstu
        button.setFocusPainted(false);
        button.setBorderPainted(false);
    }

    private void addWorkout() {
        String name = JOptionPane.showInputDialog(this, "Enter workout name:");
        if (name == null || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Workout name cannot be empty.");
            return;
        }

        String location = JOptionPane.showInputDialog(this, "Enter workout location:");
        if (location == null || location.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Workout location cannot be empty.");
            return;
        }

        workoutListModel.addElement(new Workout(name, location));
    }

    private void editWorkout() {
        Workout selectedWorkout = workoutList.getSelectedValue();
        if (selectedWorkout == null) {
            JOptionPane.showMessageDialog(this, "Please select a workout to edit.");
            return;
        }

        String newName = JOptionPane.showInputDialog(this, "Edit workout name:", selectedWorkout.getName());
        if (newName != null && !newName.trim().isEmpty()) {
            selectedWorkout.setName(newName);
        }

        String newLocation = JOptionPane.showInputDialog(this, "Edit workout location:", selectedWorkout.getLocation());
        if (newLocation != null && !newLocation.trim().isEmpty()) {
            selectedWorkout.setLocation(newLocation);
        } else {
            JOptionPane.showMessageDialog(this, "Location cannot be empty.");
        }

        workoutList.repaint(); // Odświeżenie listy
    }

    private void deleteWorkout() {
        Workout selectedWorkout = workoutList.getSelectedValue();
        if (selectedWorkout == null) {
            JOptionPane.showMessageDialog(this, "Please select a workout to delete.");
            return;
        }
        workoutListModel.removeElement(selectedWorkout);
    }

    private void saveWorkoutsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("workouts.dat"))) {
            List<Workout> workouts = new ArrayList<>();
            for (int i = 0; i < workoutListModel.getSize(); i++) {
                workouts.add(workoutListModel.getElementAt(i));
            }
            oos.writeObject(workouts);
            JOptionPane.showMessageDialog(this, "Workouts saved successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving workouts.");
        }
    }

    private void loadWorkoutsFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("workouts.dat"))) {
            List<Workout> workouts = (List<Workout>) ois.readObject();
            workoutListModel.addAll(workouts);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No previous workouts found. Starting fresh.");
        }
    }

    private void manageSessions() {
        Workout selectedWorkout = workoutList.getSelectedValue();
        if (selectedWorkout == null) {
            JOptionPane.showMessageDialog(this, "Please select a workout to manage sessions.");
            return;
        }
        new SessionView(selectedWorkout);
    }
}
