package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Reprezentuje zestaw treningowy w aplikacji Fitness Tracker.
 */
public class Workout {
    private String name;
    private List<Exercise> exercises;

    // Konstruktor
    public Workout(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Workout name cannot be null or empty.");
        }
        this.name = name;
        this.exercises = new ArrayList<>();
    }

    public Workout(String name, String group) {
    }

    // Gettery i settery
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Workout name cannot be null or empty.");
        }
        this.name = name;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    // Metody operacyjne
    public void addExercise(Exercise exercise) {
        if (exercise == null) {
            throw new IllegalArgumentException("Exercise cannot be null.");
        }
        exercises.add(exercise);
    }

    public void removeExercise(Exercise exercise) {
        exercises.remove(exercise);
    }

    // Metoda toString
    @Override
    public String toString() {
        return "Workout{" +
                "name='" + name + '\'' +
                ", exercises=" + exercises +
                '}';
    }

    // Metody equals i hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Workout workout = (Workout) o;
        return Objects.equals(name, workout.name) && Objects.equals(exercises, workout.exercises);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, exercises);
    }

    public void setGroup(String newGroup) {

    }
}
