package model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Reprezentuje cel treningowy użytkownika dla konkretnego ćwiczenia.
 */
public class UserExercise {
    private Exercise exercise;
    private float goal;
    private LocalDate goalDeadline;

    // Konstruktor
    public UserExercise(Exercise exercise, float goal, LocalDate goalDeadline) {
        if (exercise == null) {
            throw new IllegalArgumentException("Exercise cannot be null.");
        }
        if (goal <= 0) {
            throw new IllegalArgumentException("Goal must be greater than 0.");
        }
        if (goalDeadline == null || goalDeadline.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Goal deadline must be a future date.");
        }
        this.exercise = exercise;
        this.goal = goal;
        this.goalDeadline = goalDeadline;
    }

    // Gettery i settery
    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        if (exercise == null) {
            throw new IllegalArgumentException("Exercise cannot be null.");
        }
        this.exercise = exercise;
    }

    public float getGoal() {
        return goal;
    }

    public void setGoal(float goal) {
        if (goal <= 0) {
            throw new IllegalArgumentException("Goal must be greater than 0.");
        }
        this.goal = goal;
    }

    public LocalDate getGoalDeadline() {
        return goalDeadline;
    }

    public void setGoalDeadline(LocalDate goalDeadline) {
        if (goalDeadline == null || goalDeadline.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Goal deadline must be a future date.");
        }
        this.goalDeadline = goalDeadline;
    }

    // Metoda toString
    @Override
    public String toString() {
        return "UserExercise{" +
                "exercise=" + exercise.getName() +
                ", goal=" + goal +
                ", goalDeadline=" + goalDeadline +
                '}';
    }

    // Metody equals i hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserExercise that = (UserExercise) o;
        return Float.compare(that.goal, goal) == 0 &&
                Objects.equals(exercise, that.exercise) &&
                Objects.equals(goalDeadline, that.goalDeadline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exercise, goal, goalDeadline);
    }
}
