package controller;

import model.Workout;
import view.WorkoutFormView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Kontroler zarządzający treningami.
 */
public class WorkoutController {
    private List<Workout> workouts;
    private WorkoutFormView workoutFormView;
    private Consumer<Workout> workoutAddedCallback;

    public WorkoutController(WorkoutFormView workoutFormView) {
        this.workoutFormView = workoutFormView;
        this.workouts = new ArrayList<>();

        // Nasłuchiwanie akcji przycisków
        workoutFormView.addAddButtonListener(e -> addWorkout());
        workoutFormView.addEditButtonListener(e -> editWorkout());
        workoutFormView.addDeleteButtonListener(e -> deleteWorkout());
    }

    public void setWorkoutAddedCallback(Consumer<Workout> callback) {
        this.workoutAddedCallback = callback;
    }

    private void addWorkout() {
        String name = workoutFormView.getWorkoutName();
        String group = workoutFormView.getSelectedGroup();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(workoutFormView, "Workout name cannot be empty!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Workout workout = new Workout(name, group);
        workouts.add(workout);
        workoutFormView.addWorkoutToList(workout.getName());

        // Powiadamianie o dodanym treningu
        if (workoutAddedCallback != null) {
            workoutAddedCallback.accept(workout);
        }

        JOptionPane.showMessageDialog(workoutFormView, "Workout added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }


// Edycja treningu
    private void editWorkout() {
        int selectedIndex = workoutFormView.getSelectedWorkoutIndex();

        if (selectedIndex < 0) {
            JOptionPane.showMessageDialog(workoutFormView, "Please select a workout to edit!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String newName = workoutFormView.getWorkoutName();
        String newGroup = workoutFormView.getSelectedGroup();

        if (newName.isEmpty()) {
            JOptionPane.showMessageDialog(workoutFormView, "Workout name cannot be empty!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Workout workout = workouts.get(selectedIndex);
        workout.setName(newName);
        workout.setGroup(newGroup);
        workoutFormView.updateWorkoutInList(selectedIndex, workout.getName());
        JOptionPane.showMessageDialog(workoutFormView, "Workout updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    // Usuwanie treningu
    private void deleteWorkout() {
        int selectedIndex = workoutFormView.getSelectedWorkoutIndex();

        if (selectedIndex < 0) {
            JOptionPane.showMessageDialog(workoutFormView, "Please select a workout to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        workouts.remove(selectedIndex);
        workoutFormView.removeWorkoutFromList(selectedIndex);
        JOptionPane.showMessageDialog(workoutFormView, "Workout deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
