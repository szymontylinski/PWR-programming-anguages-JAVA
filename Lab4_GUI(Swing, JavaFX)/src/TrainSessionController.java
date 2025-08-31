package controller;

import model.TrainSession;
import model.Workout;
import view.TrainSessionFormView;

import javax.swing.*;
import java.util.*;

/**
 * Kontroler zarządzający sesjami treningowymi.
 */
public class TrainSessionController {
    private Map<String, List<TrainSession>> sessions;
    private TrainSessionFormView trainSessionFormView;

    public TrainSessionController(TrainSessionFormView trainSessionFormView) {
        this.sessions = new HashMap<>();
        this.trainSessionFormView = trainSessionFormView;

        trainSessionFormView.addAddButtonListener(e -> addSession());
        trainSessionFormView.addDeleteButtonListener(e -> deleteSession());
    }

    public void addWorkout(Workout workout) {
        if (workout != null) {
            trainSessionFormView.addWorkoutToComboBox(workout.getName());
        }
    }


    private void addSession() {
        String selectedWorkout = trainSessionFormView.getSelectedWorkout();
        String date = trainSessionFormView.getDate();
        String weightText = trainSessionFormView.getWeight();
        String durationText = trainSessionFormView.getDuration();

        if (selectedWorkout == null || selectedWorkout.isEmpty()) {
            JOptionPane.showMessageDialog(trainSessionFormView, "Please select a workout!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double weight = Double.parseDouble(weightText);
            int duration = Integer.parseInt(durationText);

            TrainSession session = new TrainSession(selectedWorkout, date, weight, duration);
            sessions.putIfAbsent(selectedWorkout, new ArrayList<>());
            sessions.get(selectedWorkout).add(session);

            trainSessionFormView.addSessionToList(session.toString());
            JOptionPane.showMessageDialog(trainSessionFormView, "Session added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(trainSessionFormView, "Weight and duration must be numeric!", "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSession() {
        int selectedIndex = trainSessionFormView.getSelectedSessionIndex();
        String selectedWorkout = trainSessionFormView.getSelectedWorkout();

        if (selectedWorkout == null || selectedIndex < 0) {
            JOptionPane.showMessageDialog(trainSessionFormView, "Please select a session to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        sessions.get(selectedWorkout).remove(selectedIndex);
        trainSessionFormView.removeSessionFromList(selectedIndex);
        JOptionPane.showMessageDialog(trainSessionFormView, "Session deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
