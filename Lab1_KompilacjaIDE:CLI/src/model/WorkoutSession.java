package model;

import java.io.Serializable;
import java.time.LocalDate;

public class WorkoutSession implements Serializable {
    private Workout workout;      // Powiązany trening
    private LocalDate date;       // Data sesji
    private String goal;          // Cel treningowy
    private String result;        // Wynik treningowy
    private String mood;          // Samopoczucie

    public WorkoutSession(Workout workout, LocalDate date, String goal, String result, String mood) {
        if (workout == null) {
            throw new IllegalArgumentException("Workout cannot be null.");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null.");
        }
        if (goal == null || goal.trim().isEmpty()) {
            throw new IllegalArgumentException("Goal cannot be empty.");
        }
        if (result == null || result.trim().isEmpty()) {
            throw new IllegalArgumentException("Result cannot be empty.");
        }
        if (mood == null || mood.trim().isEmpty()) {
            throw new IllegalArgumentException("Mood cannot be empty.");
        }

        this.workout = workout;
        this.date = date;
        this.goal = goal;
        this.result = result;
        this.mood = mood;
    }

    public Workout getWorkout() {
        return workout;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getGoal() {
        return goal;
    }

    public String getResult() {
        return result;
    }

    public String getMood() {
        return mood;
    }

    @Override
    public String toString() {
        return "Workout at: " + workout.getLocation() +
                ", Date: " + date +
                ", Goal: " + goal +
                ", Result: " + result +
                ", Mood: " + mood;
    }
}
