package main;

import controller.TrainSessionController;
import controller.WorkoutController;
import view.TrainSessionFormView;
import view.WorkoutFormView;

public class Main {
    public static void main(String[] args) {
        // Tworzenie widoków
        WorkoutFormView workoutFormView = new WorkoutFormView();
        TrainSessionFormView trainSessionFormView = new TrainSessionFormView();

        // Tworzenie kontrolerów
        WorkoutController workoutController = new WorkoutController(workoutFormView);
        TrainSessionController trainSessionController = new TrainSessionController(trainSessionFormView);

        // Łączenie kontrolerów
        workoutController.setWorkoutAddedCallback(trainSessionController::addWorkout);

        // Wyświetlanie widoków
        workoutFormView.setVisible(true);
        trainSessionFormView.setVisible(true);
    }
}
