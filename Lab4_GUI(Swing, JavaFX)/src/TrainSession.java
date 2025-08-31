package model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Reprezentuje sesję treningową w aplikacji Fitness Tracker.
 */
public class TrainSession {
    private LocalDate date;
    private Workout workout;

    // Konstruktor
    public TrainSession(LocalDate date, Workout workout) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null.");
        }
        if (workout == null) {
            throw new IllegalArgumentException("Workout cannot be null.");
        }
        this.date = date;
        this.workout = workout;
    }

    public TrainSession(String selectedWorkout, String date, double weight, int duration) {
    }

    // Gettery i settery
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null.");
        }
        this.date = date;
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(Workout workout) {
        if (workout == null) {
            throw new IllegalArgumentException("Workout cannot be null.");
        }
        this.workout = workout;
    }

    // Metoda toString
    @Override
    public String toString() {
        return "TrainSession{" +
                "date=" + date +
                ", workout=" + workout.getName() +
                '}';
    }

    // Metody equals i hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainSession that = (TrainSession) o;
        return Objects.equals(date, that.date) && Objects.equals(workout, that.workout);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, workout);
    }
}
